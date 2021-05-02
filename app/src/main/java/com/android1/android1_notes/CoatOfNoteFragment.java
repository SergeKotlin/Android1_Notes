package com.android1.android1_notes;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

public class CoatOfNoteFragment extends Fragment {

    protected static final String ARG_INDEX = "index";
    private int index;

    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы.
    public static CoatOfNoteFragment newInstance(int index) {
        CoatOfNoteFragment f = new CoatOfNoteFragment();    // создание

        // Передача параметра
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Таким способом можно получить головной элемент из макета
        View view = inflater.inflate(R.layout.fragment_coat_of_note, container, false);
        // найти в контейнере элемент-заметку
        AppCompatEditText editTextCoatOfNote = view.findViewById(R.id.coat_of_note);
        // Получить из ресурсов массив заметок
        TypedArray notes = getResources().obtainTypedArray(R.array.notes);
        // Выбрать по индексу подходящий
        editTextCoatOfNote.setText(notes.getText(index));
        return view;
    }
}
