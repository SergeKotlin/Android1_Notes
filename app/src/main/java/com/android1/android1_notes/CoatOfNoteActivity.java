package com.android1.android1_notes;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CoatOfNoteActivity extends AppCompatActivity {
// Эта activity для открытия заметки в портретной ориентации - //TODO на удаление. Д.б. фрагменты и 1 активити

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__activity_coat_of_note);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Если устройство перевернули в альбомную ориентацию, то надо эту activity закрыть
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // Если эта activity запускается первый раз (с каждой новой заметкой первый раз),
            // то перенаправим параметр фрагменту
            CoatOfNoteFragment details = new CoatOfNoteFragment();
            details.setArguments(getIntent().getExtras());
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details).commit();
        }
    }
}