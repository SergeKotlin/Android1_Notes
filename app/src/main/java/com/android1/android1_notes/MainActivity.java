package com.android1.android1_notes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
//    private Fragment fragment_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragmentB();
    }

    private void setFragmentB() {
        Fragment fragment_B = getSupportFragmentManager().findFragmentByTag("fragment_B");
        if (fragment_B == null) fragment_B = new Fragment_B();
        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragmentContainer, new Fragment_B())
                .replace(R.id.fragmentContainer, fragment_B)
                .addToBackStack("fragment_a") // добавляем тэг
                .commit(); // Фрагменты сэйвятся АСИНХРОННО!
                //.replace и .add – если в контейнере уже есть элемент, то это обязательно .replace

        /*getSupportFragmentManager().beginTransaction()
                .remove(fragment_B)
                .commit();*/
    }

    @Override
    public void onBackPressed() {
        // Специальный доступ для внесения какой-то своей логики.. Либо переопределение бесполезно.
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
        // getSupportFragmentManager().popBackStack("fragment_a", 0);
        // Одинаково хорошо будет - что с 0, что с фрагментом 'FragmentManager.POP_BACK_STACK_INCLUSIVE'
        // Флаг POP_BACK_STACK_INCLUSIVE позволяет аккуратно извлечь фрагмент из стэка, сохранив стэк (верх, над фрагмент)
        // - хотя это и противоречит принц-м Стэка.. но иногда м.б нужно.
            getSupportFragmentManager().popBackStack("fragment_a", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // А в случае с пустыми аргументами извлечётся просто последний фрагмент Стэка getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}