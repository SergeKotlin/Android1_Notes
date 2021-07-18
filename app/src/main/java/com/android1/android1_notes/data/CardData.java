package com.android1.android1_notes.data;

public class CardData {

    private final String noteName;
    private final String noteText;
    private final String noteColor;

    public CardData(String noteName, String noteText, String noteColor){
        this.noteName = noteName;
        this.noteText = noteText;
        this.noteColor = noteColor;
    }

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
