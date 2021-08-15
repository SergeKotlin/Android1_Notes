package com.android1.android1_notes.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.android1.android1_notes.MainActivity;
import com.android1.android1_notes.R;
import com.android1.android1_notes.data.CardData;
import com.android1.android1_notes.observer.Publisher;

import java.util.Calendar;
import java.util.Date;

public class CardFragment extends Fragment {

    private static final String ARG_CARD_DATA = "Param_CardData";

    private static CardData note;
    private Publisher publisher;

    private AppCompatEditText noteNameView;
    private AppCompatEditText editTextNote;
    private DatePicker datePicker;

    // Для редактирования данных
    // Ps я не заморачивался, editInfo выступает лишь признаком для перегрузки фабрики на редактирование
    public static CardFragment newInstance(CardData cardData, String editInfo) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
            // Важное примечание!:
            /* Аргументы могут передаваться либо через args.putParcelable(), либо через .putSerializable(),
               просто так нельзя чё-то передать,типа через ссылку. Просто так - даже кошки не рождаются ;) */
        args.putParcelable(ARG_CARD_DATA, cardData);
            // args.putParcelable(editInfo, cardData); // можно прислать свой параметр вместо константы
        fragment.setArguments(args);
        return fragment;
            // Важное примечание!:
            /* Сложный вариант метода, т.к метод статичный - нужно осуществить передачу данных, через аргуметы,
               в настоящий фрагмент - т.е в действителньный объект.
               Конструкторы во Фрагменте лучше НЕ ДЕЛАТЬ  - заметил сэнсэй Владимир.
               Конструктор принимает на вход много информации, context.. & etc - так что его переопределение крайне не рекомендовано. */
    }

    // Фабричный метод создания фрагмента (Фрагменты рекомендуется создавать через фабричные методы)
    public static CardFragment newInstance(CardData data) {
        CardFragment fragment = new CardFragment(); // создание
        CardFragment.note = data;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_CARD_DATA);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override @SuppressLint("Recycle")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false); // Получаем головной элемент из макета
        initView(view);
        if (note != null) { // если cardData пустая, то это добавление
            inflateView();
        }
        initPopupMenu(view);
        setHasOptionsMenu(true); // Регестрируем меню приложения для фрагмента! Не забываем
        return view;
    }

    // Здесь соберём данные из views
    @Override
    public void onStop() {
        super.onStop();
        note = collectCardData();
    }

    // Здесь передадим данные в паблишер
    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }

    private CardData collectCardData(){
        String name = this.noteNameView.getText().toString();
        String text = this.editTextNote.getText().toString();
        Date date = getDateFromDatePicker();

        String color;
        if (note != null){
            color = note.getColor();
        } else {
            color = "#EFD446"; // по умолчанию заметки жёлтые, R.color.yellow_note;
        }
        return new CardData(name, text, color, date);
    }

    // Получение даты из DatePicker
    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }

    private View initView(View view) {
        editTextNote = view.findViewById(R.id.textNote); // найти в контейнере элемент-заметку (куда сетить)
        noteNameView = view.findViewById(R.id.nameNote);
        datePicker = view.findViewById(R.id.inputDate);
        return view;
    }

    private void inflateView(){
        editTextNote.setText(note.getText());
        noteNameView.setText(note.getName());
        initDatePicker(note.getDate());
    }

    // Установка даты в DatePicker
    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
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
