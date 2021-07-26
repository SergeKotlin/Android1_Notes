package com.android1.android1_notes.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CardData implements Parcelable {

    private final String noteName;
    private final String noteText;
    private final String noteColor;

    public CardData(String noteName, String noteText, String noteColor){
        this.noteName = noteName;
        this.noteText = noteText;
        this.noteColor = noteColor;
    }

    public CardData(String noteName, String noteText){
        this(noteName, noteText,"#EFD446"); // По умолчанию заметки жёлтые
    }

    // Перегрузим наш класс для передачи, обмена данных между фрагментами, а также для смены ориентации экрана
// Начало обслуживания Parcel
    protected CardData(Parcel in) {
        noteName = in.readString();
        noteText = in.readString();
        noteColor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getText());
        dest.writeString(getColor());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardData> CREATOR = new Creator<CardData>() {
        @Override
        public CardData createFromParcel(Parcel in) {
            return new CardData(in);
        }
        @Override
        public CardData[] newArray(int size) {
            return new CardData[size];
        }
    };
// Конец обслуживания Parcel.

    public String getName() {
        return noteName;
    }

    public String getText() {
        return noteText;
    }

    public String getColor() {
        return noteColor;
    }

}
