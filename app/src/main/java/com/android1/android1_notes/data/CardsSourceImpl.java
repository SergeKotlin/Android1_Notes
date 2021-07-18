package com.android1.android1_notes.data;

import android.content.res.Resources;

import com.android1.android1_notes.R;

import java.util.ArrayList;
import java.util.Arrays;
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
            dataSource.add(new CardData(noteNames[i], noteTexts[i], noteColors[i]));
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
}
