package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android1.android1_notes.data.Settings;
import com.android1.android1_notes.ui.MainFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /* Возможен Upgrade для popBackStack(name, flags) к определённому состоянию. 9-ый урок Аникина, 2 февраля
    private static final String FRAGMENT_NOTES_LIST_TAG = "NotesList";
    private static final String FRAGMENT_NOTE_TAG = "Note"; */
    private Navigation navigation;
    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readSettings();
        initView();
        navigation(MainFragment.newInstance(), false); // Наполняем список заметок
    }

    private void readSettings(){
        // Специальный класс для хранения настроек
        SharedPreferences sharedPref = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        // Считываем значения настроек
        Settings.isBackStack = sharedPref.getBoolean(Settings.IS_BACK_STACK_USED, true);
        Settings.isBlackTheme = sharedPref.getBoolean(Settings.IS_BLACK_THEME_USED, false);
    }

    private void initView() {
        // Регистрируем навигационное (левое) меню
        initDrawer(initToolbar());
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Обработка навигационного меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (navigateFragment(id)){
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });
    }

    @SuppressLint("NonConstantResourceId")
    private boolean navigateFragment(int id) {
            switch(id){
                case R.id.action_settings:
                    navigation(new SettingsFragment(), true);
                    return true;
                case R.id.action_main:
//                    navigation(new MainFragment(), true);
                    navigation(MainFragment.newInstance(), true); //TODO !!! Меню Навигации работает с MainFragment устаревшим способом
                    return true;
            }
            return false;
        }

    private void navigation(Fragment fragment, boolean useBackStack) {
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        navigation = new Navigation(getSupportFragmentManager(), isLandscape);
        getNavigation().addFragment(fragment, isLandscape, useBackStack);
    }

    // Действие кнопки назад в соответствии с хранимыми настройками
    private void initBackAsRemoveFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if ((id == android.R.id.home) && !Settings.isBackStack) {
            Fragment fragment = getVisibleFragment(fragmentManager);
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
        else {
            //TODO Если бэкстэк уже пуст, ничего не делать. Выход из приложения буднь в левом меню if (fragmentManager.getBackStackEntryCount() != 0) {}
            fragmentManager.popBackStack();
            /* Пояснение!:
              [для однострочной простоты спользуем replace() вместо настройки add()]
              Внутри созданной fragmentTransaction происходят действия .replace(),
              которые можно отправить в стек обратного вызова с помощью .addToBackStack(null).
              Если указать имя, можно будет перепрыгивать сразу к именованной транзакции с
              определённым фрагментом fragmentManager.popBackStack(name, flags). Где flags - признак,
              будем ли включать именованную транзакцию. 0 - фрагмент с именованной транзакцией, 1 или
              POP_BACK_STACK_INCLUSIVE - предыдущее состояние относительно именованной транзакции. */
        }
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

    // Здесь определяем меню приложения (активити)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        toSearching(menu);
        return true; // false - руль фрагменту, true - рулим здесь
    }

    private void toSearching(Menu menu) {
        MenuItem search = menu.findItem(R.id.search__main_menu); // поиск пункта меню поиска
        SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // реагирует на конец ввода поиска
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) { // реагирует на нажатие каждой клавиши
                return true;
            }
        });
    }

    // Меню активити
    @Override @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // getItemId() - берём id из нажатого item
        if (navigateFragment(id)) {
            return true;
        }
        switch (id) {
            /*case (R.id.add_note__main_menu):
                toastOnOptionsItemSelected("Добавление новой заметки");
                // Заглушка //TODO navigation(new NoteFragment(), true);
                return true;*/
            case (R.id.some_item):
                toastOnOptionsItemSelected("Новая фича");
                return true;
            //TODO Сортировка должна быть на main фрагменте, однако toasts срабатывают лишь на активити
            /*case R.id.sort_by_name_ascending__main_menu:
                toastOnOptionsItemSelected("Алфавитная сортировка - по возрастанию");
                return true;
            case R.id.sort_by_name_descending__main_menu:
                toastOnOptionsItemSelected("Алфавитная сортировка - по убыванию");
                return true;
            case R.id.sort_by_date_ascending__main_menu:
                toastOnOptionsItemSelected("Временная сортировка - по возрастанию");
                return true;
            case R.id.sort_by_date_descending__main_menu:
                toastOnOptionsItemSelected("Временная сортировка - по убыванию");
                return true;*/
        }
        initBackAsRemoveFragment(id);
        return super.onOptionsItemSelected(item);
    }

    private void toastOnOptionsItemSelected(CharSequence text) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END, 0, 0);
        toast.show();
    }

    public Navigation getNavigation() {
        return navigation;
    }

}

// Задание.
/* ✓ 1. Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его.
     2. Сделайте навигацию между фрагментами, также организуйте обмен данными между
        фрагментами.
     3. Создайте контекстное меню для изменения и удаления заметок.
     4. *Изучите, каким образом можно вызывать DatePicker в виде диалогового окна. Создайте
        текстовое поле, при нажатии на которое вызывалось бы диалоговое окно с DatePicker. */
// Serega, sure
