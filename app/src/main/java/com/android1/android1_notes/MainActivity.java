package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readSettings();
        initView();

        // Для кнопки "Назад"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Подключаем меню в приложении
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.action_search); // поиск пункта меню поиска
        // с findItem() можно много чего интересно сделать, например управлять Visible
        SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
            // реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                // повесим сюда тоже, чтобы показывала нашу печать в real_time =)
                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        return true; // Можно рулить меню внутри фрагмента, и возьмёс управление там, а тут вернём false
    }

    // Для обрабатывания нажатий:
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                addFragment(new SettingsFragment());
                return true;
            case R.id.action_main:
                addFragment(new MainFragment());
                return true;
            case R.id.action_favorite:
                addFragment(new FavoriteFragment());
                return true;

            // Для кнопки "Назад"
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),
                        getString(R.string.back_clicked), Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        initButtonMain();
        initButtonFavorite();
        initButtonSettings();
        initButtonBack();
    }

    // Метод нужен.. чтобы обнаружить visible-фрагменты, пробегаем циклом с конца - т.к стэк
    // К сожалению, только таким образом найти visible-фрагменты
    private void initButtonBack() {
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (Settings.isBackAsRemove){
                Fragment fragment = getVisibleFragment(fragmentManager);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            } else {
                fragmentManager.popBackStack();
            }
        });
    }

    private void initButtonSettings() {
        Button buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(v -> addFragment(new SettingsFragment()));
    }

    private void initButtonFavorite() {
        Button buttonFavorite = findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(v -> addFragment(new FavoriteFragment()));
    }

    private void initButtonMain() {
        Button buttonMain = findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(v -> addFragment(new MainFragment()));
    }

    private Fragment getVisibleFragment(FragmentManager fragmentManager){
        List<Fragment> fragments = fragmentManager.getFragments();
        int countFragments = fragments.size();
        for(int i = countFragments - 1; i >= 0; i--){
            Fragment fragment = fragments.get(i);
            if(fragment.isVisible())
                return fragment;
        }
        return null;
    }

    private void addFragment(Fragment fragment){

        //Получить менеджер фрагментов
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Удалить видимый фрагмент
        if (Settings.isDeleteBeforeAdd){
            Fragment fragmentToRemove = getVisibleFragment(fragmentManager);
            if (fragmentToRemove != null) {
                fragmentTransaction.remove(fragmentToRemove);
            }
        }

        // Добавить фрагмент
        if (Settings.isAddFragment) {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }

        // Добавить транзакцию в бэкстек
        if (Settings.isBackStack){
            fragmentTransaction.addToBackStack(null);
        }

        // Закрыть транзакцию
        fragmentTransaction.commit();
//        fragmentTransaction.commitNow(); - синхронный
//        fragmentTransaction.commitAllowingStateLoss(); - сохраняет состояние активити (например,
//        при изменении его во время commit)
//        fragmentTransaction.commitNowAllowingStateLoss() - оба верхних сразу
    }

    // Чтение настроек
    private void readSettings(){
        // Специальный класс для хранения настроек
        SharedPreferences sharedPref = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Считываем значения настроек
        Settings.isBackStack = sharedPref.getBoolean(Settings.IS_BACK_STACK_USED, false);
        Settings.isAddFragment = sharedPref.getBoolean(Settings.IS_ADD_FRAGMENT_USED, true);
        Settings.isBackAsRemove = sharedPref.getBoolean(Settings.IS_BACK_AS_REMOVE_FRAGMENT, true);
        Settings.isDeleteBeforeAdd = sharedPref.getBoolean(Settings.IS_DELETE_FRAGMENT_BEFORE_ADD, false);
    }
}
