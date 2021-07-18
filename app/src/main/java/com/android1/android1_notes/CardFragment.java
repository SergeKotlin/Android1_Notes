package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.android1.android1_notes.data.CardData;

public class CardFragment extends Fragment {

    private static CardData note;
//    private static int position;

    // Фабричный метод создания фрагмента (Фрагменты рекомендуется создавать через фабричные методы)
    public static CardFragment newInstance(CardData data) {
        CardFragment f = new CardFragment(); // создание
        CardFragment.note = data;
//        CardFragment.position = position-1; // передача параметра

//        Bundle args = new Bundle(); // передача параметра
//        args.putParcelable("position", position);
//        f.setArguments(args);
        return f;
    }

    @Override @SuppressLint("Recycle")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = showNote(inflater, container);
//        setHasOptionsMenu(true); // Регестрируем меню! Не забываем
//        initPopupMenu(view);
        return view;
    }

    @SuppressLint("Recycle")
    private View showNote(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_note, container, false); // Получаем головной элемент из макета
        AppCompatEditText editTextNote = view.findViewById(R.id.textNote); // найти в контейнере элемент-заметку (куда сетить)
//        TypedArray notes = getResources().obtainTypedArray(R.array.notes); // получить из ресурсов массив заметок
        editTextNote.setText(note.getText());
//        editTextCoatOfNote.setText(notes.getText(position)); // выбрать и установить по индексу подходящий текст
//        TypedArray notes_names = getResources().obtainTypedArray(R.array.notes_names); // по аналогии установить название заметки
        TextView noteNameView = view.findViewById(R.id.nameNote);
        noteNameView.setText(note.getName());
        return view;
    }

}
