package com.android1.android1_notes.data;

public class CardData {

    private final String noteName;
    private final String noteText;

    public CardData(String noteName, String noteText){
        this.noteName = noteName;
        this.noteText = noteText;
    }

    public String getName() {
        return noteName;
    }

    public String getText() {
        return noteText;
    }

}
