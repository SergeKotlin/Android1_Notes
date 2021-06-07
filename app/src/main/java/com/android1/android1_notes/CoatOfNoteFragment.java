package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

public class CoatOfNoteFragment extends Fragment {
// Это код для фрагмента заметки заметки fragment_coat_of_note.xml
// TODO Фрагмент то есть - да сетим в новую активити.. Исправить для п-па «Single Activity»

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

        setHasOptionsMenu(true); // Подключение меню для фрагмента
        initPopupMenu(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_section__main_menu, menu);
        // Default: super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.note_section__main_menu) {
            Toast.makeText(getContext(), "Секция меню заметки", Toast.LENGTH_LONG)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPopupMenu(View view) {
        copyPopupMenu(view);
        moreActionsPopupMenu(view);
        listOptionsPopupMenu(view);
    }

    private void copyPopupMenu(View view) {
        ImageButton btnCopyNote = view.findViewById(R.id.buttonCopyNote);
        btnCopyNote.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_copy__note, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                Toast.makeText(getContext(), "Скопировано в буфер", Toast.LENGTH_SHORT)
                        .show();
                return true;
            });
            popupMenu.show();
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void moreActionsPopupMenu(View view) {
        ImageButton btnMoreActions = view.findViewById(R.id.buttonMoreActionsNote);
        btnMoreActions.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_more_actions__note, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                switch (id) {
                    case R.id.search_in_note__popup_note:
                        Toast.makeText(getContext(), "Поиск в заметке", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.share_note__popup_note:
                        Toast.makeText(getContext(), "Поделиться", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.export_to_file__popup_note:
                        Toast.makeText(getContext(), "Сохранить как..", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.delete__popup_note:
                        Toast.makeText(getContext(), "Удалить заметку", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.info_details__popup_note:
                        Toast.makeText(getContext(), "Информация о заметке", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            });
            popupMenu.show();
        });
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    private void listOptionsPopupMenu(View view) {
        ImageButton btnListOptions = view.findViewById(R.id.buttonCompassNote);
        btnListOptions.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_compass__note, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                switch (id) {
                    case R.id.label_for_note__popup_note:
                        Toast.makeText(getContext(), "Установить новую метку", Toast.LENGTH_SHORT).show();
                        view.findViewById(R.id.noteList).setBackgroundColor(R.color.lightblue_note);
                        return true;
                    case R.id.pin_to_top__popup_note:
                        Toast.makeText(getContext(), "Закрепить вверху списка", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            });
            popupMenu.show();
        });
    }
}
