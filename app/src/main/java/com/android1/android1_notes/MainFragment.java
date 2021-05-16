package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true); // Данный фрагм. примет участие в добавлении опций в основное меню
        initPopupMenu(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);
        // inflater, дружочек-пирожочек, это соединение xml с объектом, "надувание" xml т.о - помни
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Toast.makeText(getContext(), "Chosen add", Toast.LENGTH_SHORT).show();
            // Реаигируем на код по добавлению
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private void initPopupMenu(View view) {
        TextView textView = view.findViewById(R.id.textView);
        textView.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            Menu menu = popupMenu.getMenu();
//            menu.findItem(R.id.item2_popup).setVisible(false);
//            menu.add(0, 123456, 12, R.string.new_menu_item_added);
            menu.add(0, 123456, 30, R.string.new_menu_item_added);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                switch (id) {
                    case R.id.item1_popup:
                        Toast.makeText(getContext(), "Chosen popup item 1", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.item2_popup:
                        Toast.makeText(getContext(), "Chosen popup item 2", Toast.LENGTH_SHORT).show();
                        return true;
                    case 123456:
                        Toast.makeText(getContext(), "Chosen new item added", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            });
            popupMenu.show();
        });
    }
}