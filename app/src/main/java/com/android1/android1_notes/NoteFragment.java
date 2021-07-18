package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.android1.android1_notes.data.Note;

public class NoteFragment extends Fragment {

    public static final String ARG_NOTE = "note";
    private Note note;

    // Фабричный метод создания фрагмента (Фрагменты рекомендуется создавать через фабричные методы)
    public static NoteFragment newInstance(Note note) {
        NoteFragment f = new NoteFragment(); // создание
        Bundle args = new Bundle(); // передача параметра
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

    @Override @SuppressLint("Recycle")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = showNote(inflater, container);
        setHasOptionsMenu(true); // Регестрируем меню! Не забываем
//        initPopupMenu(view);
        return view;
    }

    @SuppressLint("Recycle")
    private View showNote(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_note, container, false); // Получаем головной элемент из макета
        AppCompatEditText editTextCoatOfNote = view.findViewById(R.id.textNote); // найти в контейнере элемент-заметку (куда сетить)
        TypedArray notes = getResources().obtainTypedArray(R.array.notes); // получить из ресурсов массив заметок
        editTextCoatOfNote.setText(notes.getText(note.getNoteIndex())); // выбрать и установить по индексу подходящий текст
        TypedArray notes_names = getResources().obtainTypedArray(R.array.notes_names); // по аналогии установить название заметки
        TextView noteNameView = view.findViewById(R.id.nameNote);
        noteNameView.setText(notes_names.getText(note.getNoteIndex()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu, menu);
    }

    @Override @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.note_section__note_menu:
                toastOnOptionsItemSelected("Секция меню заметки");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toastOnOptionsItemSelected(CharSequence text) {
        Toast toast = Toast.makeText(getContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END, 0, 0);
        toast.show();
    }

}