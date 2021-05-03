package com.android1.android1_notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Random;

public class NotesFragment extends Fragment {

    private boolean isLandscape;

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    // вызывается после создания макета фрагмента, здесь мы проинициализируем список
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
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
            final int fi = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCoatOfNote(fi);
                }
            });
        }
    }

    //TODO #1_Not_important ИСКЛЮЧИТЬ ПОВТОР ЦВЕТОВ ДРУГ ЗА ДРУГОМ
    private void setNoteColor(TextView tv) {
        int[] notes_colors = getResources().getIntArray(R.array.notes_colors);
        int note_color;
        int size_colors_arr = notes_colors.length;
        if (size_colors_arr <= 0) {
            note_color = R.color.yellow_note;
        } else {
            Random random = new Random();
            int ind_note_color = random.nextInt(size_colors_arr);
            note_color = notes_colors[ind_note_color];
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

        // Если можно показать текст заметки рядом, сделаем это
        if (isLandscape) {
            showLandCoatOfNote(0);
        }
    }

    private void showCoatOfNote(int index) {
        if (isLandscape) {
            showLandCoatOfNote(index);
        } else {
            showPortCoatOfNote(index);
        }
    }

    // Показать заметки в ландшафтной ориентации
    private void showLandCoatOfNote(int index) {
        // Создаём новый фрагмент с текущей позицией для открытия заметки
        CoatOfNoteFragment detail = CoatOfNoteFragment.newInstance(index);

        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.coat_of_note, detail);  // замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    // Открыть заметку в портретной ориентации.
    private void showPortCoatOfNote(int index) {
        // Откроем вторую activity
        Intent intent = new Intent();
        intent.setClass(getActivity(), CoatOfNoteActivity.class);
        // и передадим туда параметры
        intent.putExtra(CoatOfNoteFragment.ARG_INDEX, index);
        startActivity(intent);
    }

}