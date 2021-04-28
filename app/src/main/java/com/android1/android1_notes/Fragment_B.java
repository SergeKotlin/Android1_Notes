package com.android1.android1_notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_B extends Fragment {
    private Button increaseCounterBtn;
    private TextView counterTextView;
    private int currentCounterValue = 0;

    @Override
    // для сохранения (savedInstanceState) состояния при вращении экрана (смене ориентации) в фрагментах есть очень удобный способ:
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // одна строчка =)
        // Однако! Фокус работает лишь в случае "железного создания" фрагмента во вьюхе - см. * activity_main.xml
        // Потому что setFragmentB() в MainActivity (программное создание) постояно обновляет fragment_b.xml
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflater - доступ к layout; container - активити, куда конектится фрагмент

        // По умолчанию: return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment__b, container, false);
        //attachToRoot - всегда false (ВРУЧНУЮ! Default = true). Иное - редкий кейс для ручного управления для каких-то тестов.. Нам не нужно.
    } // Возвращаем View

    // Получаем View
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOnClickForIncreaseCounter();
        setCurrentValue();
    }

    private void initViews(View view) {
        increaseCounterBtn = view.findViewById(R.id.second_button_increaseCounter);
        counterTextView = view.findViewById(R.id.counterTextView);
    }

    private void setOnClickForIncreaseCounter() {
        increaseCounterBtn.setOnClickListener(v -> {
            currentCounterValue = Integer.parseInt(counterTextView.getText().toString());
            String newText = String.valueOf(++currentCounterValue);
            counterTextView.setText(newText);
        });
    }

    private void setCurrentValue() {
        counterTextView.setText(String.valueOf(currentCounterValue));
    }
}

