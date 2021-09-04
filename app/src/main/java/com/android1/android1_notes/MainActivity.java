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
import com.android1.android1_notes.observer.Publisher;
import com.android1.android1_notes.ui.MainFragment;
import com.android1.android1_notes.ui.StartFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /* Возможен Upgrade для popBackStack(name, flags) к определённому состоянию. 9-ый урок Аникина, 2 февраля
    private static final String FRAGMENT_NOTES_LIST_TAG = "NotesList";
    private static final String FRAGMENT_NOTE_TAG = "Note"; */

    private boolean isLandscape;
    private Navigation navigation;
    private final Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readSettings();
        initView();
        supportNavigation(StartFragment.newInstance(), false); // Наполняем список заметок
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return toolbar;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                    supportNavigation(new SettingsFragment(), true);
                    return true;
                case R.id.action_main:
                    supportNavigation(MainFragment.newInstance(), true);
                    return true;
            }
            return false;
        }

    public void supportNavigation(Fragment fragment, boolean useBackStack) {
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        navigation = new Navigation(getSupportFragmentManager(), isLandscape);
        getNavigation().addFragment(fragment, useBackStack, false);
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

    public Publisher getPublisher() {
        return publisher;
    }

}

// Out of a course, Revision.
/* Critical moments:
   ✓ Большой текст заметки перекрывает календарь. Календарь стоит перевести в диалоговое окно.
   - Паршивые дублирующиеся меню фрагментов.. Надо с ними разобраться наконец.
   Upgrade:
   - Хочу, чтобы заметка редактировалось таки сбоку в ландшафте - но при бэке список обновлялся.
   - Раз у меня есть цвета - хочу запрогать выбор цвета(метки) пользователем. Это не д.б сложно.
   - Переделать ввод данных на диалоговое окно. [Возможно, много возни. Но можно лишь в портрете]
   - Если совсем делать нечего - убрать логин при повороте экрана. Are SharedPreference is welcome?
    */
// Serega, sure
