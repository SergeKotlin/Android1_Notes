package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android1.android1_notes.data.CardData;
import com.android1.android1_notes.data.CardsSource;
import com.android1.android1_notes.data.CardsSourceImpl;

public class MainFragment extends Fragment {

    public static final String CURRENT_NOTE = "CurrentNote";
    private static CardData note;

    private CardsSource data;
    private MainAdapter adapter;
    private RecyclerView recyclerView;

    private boolean isLandscape;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);

        setHasOptionsMenu(true); // Регестрируем меню приложения для фрагмента! Не забываем
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation // Для определение, можно ли открыть рядом заметку в другом фрагменте
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(CURRENT_NOTE); // Восстановление текущей позиции
            showNote(note);
        }
        // Участок ниже содержал исключение на случай отсутствия заметки, мы учли его showNote проверкой data на null
        /*else { //TODO to_Delete
            // Если восстановить не удалось, можно показать объект с первым индексом
            note = new CardData(getResources().getStringArray(R.array.notes_names)[0], getResources().getStringArray(R.array.notes)[0], getResources().getStringArray(R.array.notes_colors)[2]);
        }*/
    }

    /*// Вызывается после создания макета фрагмента //TODO #to_Delete Помечаю на удаление. Кажется, больше не использую
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initList(view); // проинициализируем список заметок
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) { // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
        outState.putParcelable(CURRENT_NOTE, note);
        super.onSaveInstanceState(outState);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_notes_list);
        data = new CardsSourceImpl(getResources()).init(); // Получим источник данных для списка
        initRecyclerView();
    }

    // RecyclerView - размещает элементы списка, через Менеджера, а также делает запросы к Адаптеру на получение этих данных. Т.о. командует адаптером
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true); // Установка для повышения производительности системы (все эл-ты списка одинаковые по размеру, обработка ускорится)

        // Работаем со встроенным менеджером
        // Можно просто объявить менеджер в соотв-щем макете app:layoutManager="LinearLayoutManager" (ЕСЛИ менеджер стандартный)
        // Другой вариант - new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MainAdapter(data, this); // Установим адаптер
        recyclerView.setAdapter(adapter);

        //  Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,null));
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener((view, position) -> { // Установим слушателя
            toastOnItemClickListener(position);
            showNote(data.getCardData(position-1)); // Магическая -1, для приведения 1-ой позиции списка заметок к 0-му индекса массива
        });
    }

    private void showNote(CardData data) {
        if (data != null) {
            note = data; // для savedInstanceState (showNote вызывается из разных мест)
            CardFragment note = CardFragment.newInstance(data); // Создаём новый фрагмент с текущей позицией для открытия заметки
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //Получить менеджер фрагментов
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Открываем транзакцию по замене фрагмента
            fragmentTransaction.addToBackStack(null); // Перед показом заметки закинем лист заметок, с которого переходим, в БэкСтэк
//            fragmentTransaction.addToBackStack("NotesList");
            replacingFragment(note, fragmentTransaction);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }

    private void replacingFragment(CardFragment detail, FragmentTransaction fragmentTransaction) {
        // Замена фрагмента. Если можно показать текст заметки рядом, сделаем это
        if (isLandscape) {
            fragmentTransaction.replace(R.id.note_container, detail);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, detail);
        }
        // TODO можно сделать мульти-оконное открытие заметок (опционально, по выбору).
        // fragmentTransaction.add(R.id.fragment_container, detail);
    }

    @SuppressLint("DefaultLocale")
    private void toastOnItemClickListener(int position) {
        Toast.makeText(getContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    //TODO Нерешено
    @Override @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO На ландщафтной ориентации добавленные меню дублируются, так как их фрагмент остаётся присутствовать на экране
        // Постоянно "пересоздаётся" экран/фрагмент при выборе меню
        // К тому же, фрагмент для списка заметок будто добавляется дважды, тогда это мешает и корректному SaveInstance
        int id = item.getItemId();
        switch (id) {
            //TODO Здорово, только вот в массив нужно записывать тоже изменения (касается переиманования и удаления также)
            case (R.id.add_note__main_menu):
                toastOnOptionsItemSelected("Добавление новой заметки");
                data.addCardData(new CardData("Новая заметка", "Текст"));
                adapter.notifyItemInserted(data.size() -  1); // Говорит адаптеру добавить элемент в RecyclerView
                recyclerView.smoothScrollToPosition(data.size() -  1); // Упрощенный scrollToPosition()
                return true;
                /*FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment currentFragment =  fragmentManager.findFragmentById(R.id.fragment_container);
                fragmentTransaction.replace(R.id.fragment_container, currentFragment);
                fragmentTransaction.commit();*/
            case R.id.notes_view_choice__main_menu:
                toastOnOptionsItemSelected("Выбор вида представления");
                return true;
            //TODO !!! Сортировка должна быть здесь, на main фрагменте, но toasts срабатывают лишь на активити
        }
        return super.onOptionsItemSelected(item);
    }

    private void toastOnOptionsItemSelected(CharSequence text) {
        Toast toast = Toast.makeText(getContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END, 0, 0);
        toast.show();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_main, menu);
    }

    @Override @SuppressLint("NonConstantResourceId")
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getContextPosition();
        int id = item.getItemId();
        switch (id) {
            case R.id.copy_note__context_main:
                toastOnOptionsItemSelected("Заметка скопирована");
                return true;
            case R.id.share_note__context_main:
                toastOnOptionsItemSelected("Заметка передана / Открыта через..");
                return true;
            case R.id.new_label__context_main:
                toastOnOptionsItemSelected("Добавлена новая метка");
                return true;
            case R.id.pin_to_top__context_main:
                toastOnOptionsItemSelected("Заметка закреплена");
                return true;
            case R.id.search__context_main:
                toastOnOptionsItemSelected("Поиск в заметке");
                return true;
            case R.id.info__context_main:
                toastOnOptionsItemSelected("Детали заметки");
                return true;
            case R.id.rename__context_main:
                toastOnOptionsItemSelected("Заметка переименована");
                data.updateCardData(position,
                        new CardData("Заметка " + position,
                                data.getCardData(position).getText(),
                                data.getCardData(position).getColor()
//                              , Calendar.getInstance().getTime()
                        ));
                adapter.notifyItemChanged(position);
                return true;
            case R.id.delete__context_main:
                toastOnOptionsItemSelected("Заметка удалена");
                data.deleteCardData(position);
                adapter.notifyItemRemoved(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

}
