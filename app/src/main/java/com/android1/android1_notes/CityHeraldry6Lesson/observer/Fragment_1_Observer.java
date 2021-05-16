package com.android1.android1_notes.CityHeraldry6Lesson.observer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android1.android1_notes.CityHeraldry6Lesson.R;

public class Fragment_1_Observer extends Fragment implements Observer {

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1__observer, container, false);
        textView = view.findViewById(R.id.textView);
        return view;
    }


    // Как только пришло событие, обработаем его
    @Override
    public void updateText(String text) {
        textView.setText(text);
    }
}