package com.android1.android1_notes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {

    private final FragmentManager fragmentManager;
    private final boolean isLandscape;

    protected Navigation(FragmentManager fragmentManager, boolean isLandscape){
        this.fragmentManager = fragmentManager; //Получить менеджер фрагментов ( извне getSupportFragmentManager() )
        this.isLandscape = isLandscape;
    }

    public void addFragment(Fragment fragment, boolean isLandScape, boolean useBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Открыть транзакцию
        replacingFragment(fragment, fragmentTransaction);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit(); // Закрыть транзакцию
    }

    private void replacingFragment(Fragment fragment, FragmentTransaction fragmentTransaction) {
        if (isLandscape) {
            fragmentTransaction.replace(R.id.notes_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }
    }

}