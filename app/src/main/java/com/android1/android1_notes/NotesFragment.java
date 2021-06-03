package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Random;

public class NotesFragment extends Fragment {
// Эт главный код, для мэйн страницы
// Главная страница (activity_main.xml) со списком заметок надувается программно (насколько я понял :D) - из данных хранимых массивов,
// Здесь же управление ориентацией и показом второго фрагмента - с телом заметки

    public static final String CURRENT_NOTE = "CurrentNote";
    private Note currentNote;    // Выбранная заметка
    private boolean isLandscape;

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        initPopupMenu(view);
        return view;
    }

    // вызывается после создания макета фрагмента, здесь мы проинициализируем список
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.context_menu_main_page, menu);
    }

    private void initPopupMenu(View view) {
        searchPopupMenu(view);
        addPopupMenu(view);
        sortPopupMenu(view);
    }

    private void searchPopupMenu(View view) {
        ImageButton btnSearch = view.findViewById(R.id.buttonSearchNote);
        btnSearch.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_search_notes_list, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                Toast.makeText(getContext(), "Поиск", Toast.LENGTH_SHORT)
                        .show();
                return true;
            });
            popupMenu.show();
        });
    }

    private void addPopupMenu(View view) {
        ImageButton btnSearch = view.findViewById(R.id.buttonAddNewNote);
        btnSearch.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_add_notes_list, popupMenu.getMenu());
//            menu.findItem(R.id.item2_popup).setVisible(false);
//            menu.add(0, 123456, 12, R.string.new_menu_item_added);
//            menu.add(0, 123456, 30, R.string.new_menu_item_added);
            popupMenu.setOnMenuItemClickListener(item -> {
                Toast.makeText(getContext(), "Новая заметка", Toast.LENGTH_SHORT)
                        .show();
                return true;
            });
            popupMenu.show();
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void sortPopupMenu(View view) {
        ImageButton btnSearch = view.findViewById(R.id.buttonSortNotes);
        btnSearch.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_sort_notes_list, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                switch (id) {
                    case R.id.sort_by_name_ascending__popup_notes_list:
                        Toast.makeText(getContext(), "Алфавитная сортировка - по возрастанию", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sort_by_name_descending__popup_notes_list:
                        Toast.makeText(getContext(), "Алфавитная сортировка - по убыванию", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sort_by_date_ascending__popup_notes_list:
                        Toast.makeText(getContext(), "Временная сортировка - по возрастанию", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.sort_by_date_descending__popup_notes_list:
                        Toast.makeText(getContext(), "Временная сортировка - по возрастанию", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            });
            popupMenu.show();
        });
    }

    // создаём список городов на экране из массива в ресурсах
    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout)view;
        String[] notes = getResources().getStringArray(R.array.notes_names);

        // В этом цикле создаём элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        // Кроме того, создаём обработку касания на элемент
        for(int i=0; i < notes.length; i++){
            String note = notes[i];
            TextView tv = new TextView(getContext());
            tv.setText(note);
            tv.setTextSize(30);

            setNoteColor(tv);

            // не востребовано. Как пример конструкции просто  tv.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            // и это tv.setScrollY(0);
            layoutView.addView(tv);
            final int fi = i; // не можем внутрь анонимного класса передать не final - иначе гонка потоков
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentNote = new Note(fi, getResources().getStringArray(R.array.notes)[fi]);
                    showCoatOfNote(currentNote);
                }
            });
            registerForContextMenu(tv); // Для контекстного меню регистрируем таргет
        }
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    //TODO #1_Not_important ИСКЛЮЧИТЬ ПОВТОР ЦВЕТОВ ДРУГ ЗА ДРУГОМ
    private void setNoteColor(TextView tv) {
        String[] notes_colors = getResources().getStringArray(R.array.notes_colors);
        int note_color;
        int size_colors_arr = notes_colors.length;
        if (size_colors_arr <= 0) {
            note_color = R.color.yellow_note;
        } else {
            Random random = new Random();
            int ind_note_color = random.nextInt(size_colors_arr);
            note_color = Color.parseColor(notes_colors[ind_note_color]);
        }
        tv.setTextColor(note_color);
    }

    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет открыть рядом заметку в другом фрагменте
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            // Если восстановить не удалось, то сделаем объект с первым индексом
            currentNote = new Note(0, getResources().getStringArray(R.array.notes)[0]);
        }

        // Если можно показать текст заметки рядом, сделаем это
        if (isLandscape) {
            showLandCoatOfNote(currentNote);
        } else {
            showPortCoatOfNote(currentNote);
        }
    }

    private void showCoatOfNote(Note currentNote) {
        if (isLandscape) {
            showLandCoatOfNote(currentNote);
        } else {
            showPortCoatOfNote(currentNote);
        }
    }

    // Показать заметки в ландшафтной ориентации
    private void showLandCoatOfNote(Note currentNote) {
        // Создаём новый фрагмент с текущей позицией для открытия заметки
        CoatOfNoteFragment detail = CoatOfNoteFragment.newInstance(currentNote);

        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.coat_of_note, detail);  // замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    // Открыть заметку в портретной ориентации
    private void showPortCoatOfNote(Note currentNote) {
        // Откроем вторую activity
        Intent intent = new Intent();
        intent.setClass(getActivity(), CoatOfNoteActivity.class);
        // и передадим туда параметры
        intent.putExtra(CoatOfNoteFragment.ARG_NOTE, currentNote);
        startActivity(intent); //TODO ЭТО ЧТО ТАКОЕ? АЙ-Я-ЯЙ! БЕЗ НОВЫХ АКТИВИТИ. ФРАГМЕНТИ В СТАРУЮ.
        //TODO (НАЛИЧИЕ ТОГО ЖЕ МЕНЮ ПРИЛОЖЕНИЯ В НОВОМ ОКНЕ - ХОРОШАЯ ПОДСКАЗКА/ПАЛЕВО)))

        //TODO Кажется, задача проста - у нас есть фрагмент и новая активити, куда кидаю его.
        //TODO Т.о. активити сношу, а фрагмент реплейсю в первую и единственную активити.
    }

}