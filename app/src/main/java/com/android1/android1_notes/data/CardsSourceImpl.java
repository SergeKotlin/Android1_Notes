package com.android1.android1_notes.data;

import android.content.res.Resources;

import com.android1.android1_notes.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CardsSourceImpl implements CardsSource {

    private final List<CardData> dataSource;
    private final Resources resources; // ресурсы приложения
    private int sizeOfNotesList = 5;

    public CardsSourceImpl(Resources resources) {
        dataSource = new ArrayList<>(sizeOfNotesList);
        this.resources = resources;
    }

    public CardsSourceImpl init(){
        String[] noteNames = resources.getStringArray(R.array.notes_names); // названия из ресурсов
        String[] noteTexts = resources.getStringArray(R.array.notes); // тексты заметок из ресурсов
        String[] noteColors = initNotesColors(noteNames.length); // цвета заметок из ресурсов

        for (int i = 0; i < noteNames.length; i++) { // заполнение источника данных
            dataSource.add(new CardData(noteNames[i], noteTexts[i], noteColors[i], Calendar.getInstance().getTime()));
        }
        return this;
    }

    //TODO #Is_not_important ИСКЛЮЧИТЬ ПОВТОР ЦВЕТОВ ДРУГ ЗА ДРУГОМ
    private String[] initNotesColors(int data_size) {
        String[] colors = resources.getStringArray(R.array.notes_colors);
        String note_color = "#EFD446"; // по умолчанию заметки жёлтые, R.color.yellow_note;
        int colors_number = colors.length;
        String[] result = new String[data_size];

        if (colors_number <= 0) {
            Arrays.fill(result, note_color);
        } else {
            for (int i = 0; i < result.length; i++) {
                Random random = new Random();
                int rand_value = random.nextInt(colors_number);
                result[i] = colors[rand_value];
            }
        }
        return result;
    };

    public CardData getCardData(int position) {
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }

    @Override
    public void deleteCardData(int position) {
        dataSource.remove(position);
    }

    @Override
    public void updateCardData(int position, CardData cardData) {
        dataSource.set(position, cardData);
    }

    @Override
    public void addCardData(CardData cardData) {
        dataSource.add(cardData);
        /* Примечание!:
        При выборе пункта добавления вызываем метод источника данных addCardData(),
        который добавит данные в список. Затем обязательно надо предупредить RecyclerView.Adapter,
        что мы добавили новую запись, чтобы эта запись была отрисована на экране, методом
        adapter.notifyItemInserted() с указанием позиции этого элемента.
        Метод RecyclerView.scrollToPosition() прыгнет на заданную позицию, таким образом мы увидим
        вновь добавленные данные. Если надо мягко перекрутить весь список, используем метод
        smoothScrollToPosition(), но надо помнить, что в этом случае RecyclerView прочитает все
        элементы до конца. Если же использовать scrollToPosition(), то прочитаются только элементы,
        находящиеся рядом с нужной позицией.*/
    }
}
