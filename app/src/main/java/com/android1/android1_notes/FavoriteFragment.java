package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavoriteFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.textView2);
        registerForContextMenu(textView); // при долгом нажатии
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

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.popup_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1_popup: {
                Toast.makeText(requireContext(), "Item 1", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.item2_popup: {
                Toast.makeText(requireContext(), "Item 2", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }
}
