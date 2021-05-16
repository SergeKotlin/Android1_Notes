package com.android1.android1_notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavoriteFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_fragment, container, false);
    }

    // Чтобы сохранить данные для того случая, как фрагмент перестанет быть видимым - можно
    // переопределить onStop()
    @Override
    public void onStop() { // или onPause(). Но прежде, чем программировать их, стоит убедиться,
        // что они вообще вызываются - этим методы жизненного цикла
        // Можно убирать лишь видимость контейнеров с фрагментами - тогда жизненные циклы не будут затронуты
        // А может вообще телефон разрядиться))
        // Поэтому надёжнее сохранять - сразу после изменения данных. В идеале - в фоновом потоке
        Toast.makeText(requireContext(), "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }
}
