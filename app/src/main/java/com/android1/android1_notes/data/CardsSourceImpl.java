package com.android1.android1_notes.data;

import android.content.res.Resources;

import com.android1.android1_notes.R;

import java.util.ArrayList;
import java.util.List;

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

        for (int i = 0; i < noteNames.length; i++) { // заполнение источника данных
            dataSource.add(new CardData(noteNames[i], noteTexts[i]));
        }
        return this;
    }

    public CardData getCardData(int position) {
        return dataSource.get(position);
    }

    public int size(){
        return dataSource.size();
    }

    public List<CardData>  getData(int position) {
        return dataSource;
    }
}
