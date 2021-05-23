package com.android1.android1_notes;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

public class CoatOfNoteFragment extends Fragment {
// Это код для фрагмента заметки fragment_coat_of_note.xml

    public static final String ARG_NOTE = "note";
    private Note note;

    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы.
    public static CoatOfNoteFragment newInstance(Note note) {
        CoatOfNoteFragment f = new CoatOfNoteFragment();    // создание

        // Передача параметра
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Таким способом можно получить головной элемент из макета
        View view = inflater.inflate(R.layout.fragment_coat_of_note, container, false);
        // найти в контейнере элемент-заметку (куда будем сетить)
        AppCompatEditText editTextCoatOfNote = view.findViewById(R.id.coat_of_note);
        // Получить из ресурсов массив заметок
        TypedArray notes = getResources().obtainTypedArray(R.array.notes);
        // Выбрать и установить по индексу подходящий
        editTextCoatOfNote.setText(notes.getText(note.getNoteIndex()));

        // По аналогии установить название города
        TypedArray notes_names = getResources().obtainTypedArray(R.array.notes_names);
        TextView noteNameView = view.findViewById(R.id.nameNotes);
        noteNameView.setText(notes_names.getText(note.getNoteIndex()));

        return view;
    }
}
