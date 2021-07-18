package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
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
        initPopupMenu(view);
        setHasOptionsMenu(true); // Регестрируем меню! Не забываем
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

    private void initPopupMenu(View view) {
        copyPopupMenu(view);
        sharePopupMenu(view);
        moreActionsPopupMenu(view);
    }

    private void copyPopupMenu(View view) {
        ImageButton btnCopyNote = view.findViewById(R.id.buttonCopyNote);
        btnCopyNote.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_copy__note, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                toastOnOptionsItemSelected("Скопировано в буфер");
                return true;
            });
            popupMenu.show();
        });
    }

    private void sharePopupMenu(View view) {
        ImageButton btnCopyNote = view.findViewById(R.id.buttonShareNote);
        btnCopyNote.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_share__note, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                toastOnOptionsItemSelected("Поделиться / Открыть через..");
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
                    case R.id.label_for_note__popup_note:
                        toastOnOptionsItemSelected("Установить новую метку");
                        return true;
                    case R.id.pin_to_top__popup_note:
                        toastOnOptionsItemSelected("Закрепить вверху списка");
                        return true;
                    case R.id.search_in_note__popup_note:
                        toastOnOptionsItemSelected("Поиск в заметке");
                        return true;
                    case R.id.info_details__popup_note:
                        Toast.makeText(getContext(), "Информация о заметке", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.rename__popup_note:
                        Toast.makeText(getContext(), "Переименовать заметку", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.delete__popup_note:
                        Toast.makeText(getContext(), "Удалить заметку", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            });
            popupMenu.show();
        });
    }

    private void toastOnOptionsItemSelected(CharSequence text) {
        Toast toast = Toast.makeText(getContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END, 0, 0);
        toast.show();
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

}
