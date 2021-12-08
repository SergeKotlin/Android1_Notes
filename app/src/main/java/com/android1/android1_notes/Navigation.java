package com.android1.android1_notes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android1.android1_notes.ui.MainFragment;

public class Navigation {

    private final FragmentManager fragmentManager;
    private boolean isLandscape;

    protected Navigation(FragmentManager fragmentManager, boolean isLandscape){
        this.fragmentManager = fragmentManager; //Получить менеджер фрагментов ( извне getSupportFragmentManager() )
        this.isLandscape = isLandscape;
    }

    public void addFragment(Fragment fragment, boolean useBackStack, boolean isNoteFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Открыть транзакцию
        replacingFragment(fragment, fragmentTransaction, isNoteFragment);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit(); // Закрыть транзакцию
    }

    private void replacingFragment(Fragment fragment, FragmentTransaction fragmentTransaction, boolean isNoteFragment) {
        if (isLandscape) {
            if (isNoteFragment) {
                fragmentTransaction.replace(R.id.note_container, fragment);
            } else {
                fragmentTransaction.replace(R.id.fragment_container, fragment);
            }
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }
    }

    //Todo Это ужас! Так нельзя делать.
    public void refreshMain() {
        new MainActivity().supportNavigation(MainFragment.newInstance(), true);
    }
}