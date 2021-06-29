package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        initPopupMenu(view);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    private void initPopupMenu(View view) {
        TextView text = view.findViewById(R.id.textView);
        text.setOnClickListener(v -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
            Menu menu = popupMenu.getMenu();
            menu.findItem(R.id.item2_popup).setVisible(false);
            menu.add(0, 123456, 12, R.string.new_menu_item_added);
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
        registerForContextMenu(text);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Toast.makeText(getContext(), "Chosen add", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.context, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_context_main) {
            Toast.makeText(getContext(), "Chosen context add", Toast.LENGTH_SHORT).show();
            return true;
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.add_context_main:
                Toast.makeText(getContext(), "Chosen context add", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.search_context_main:
                Toast.makeText(getContext(), "Chosen context search", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
