package com.android1.android1_notes;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem search = menu.findItem(R.id.search__main_menu);
        SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реакция на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG)
                        .show();
                return true;
            }
            // реакция на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                //  и сюда повесим, чтобы показывало печать в real_time

                // Слишком медленно показывает. А скорости у нас только две - 2 секунды и 3,5. Нужен другой подход.
                /*Toast toast_real_time_search = Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT);
                toast_real_time_search.setGravity(Gravity.BOTTOM, 0, 0);
                toast_real_time_search.show();*/
                return true;
            }
        });
        return true; // false - руль фрагменту, true - рулим здесь
    }

    // Обработаем нажатия пунктов меню:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // getItemId() - берём id из нажатого item
        switch (item.getItemId()) {
            case (R.id.add_note__main_menu):
                toastOnOptionsItemSelected("Добавление новой заметки");
                // Заглушка //TODO addFragment(new NoteFragment());
                return true;
            case (R.id.list_view_choice__main_menu):
                toastOnOptionsItemSelected("Выбор вида представления");
                return true;
            case (R.id.some_item):
                toastOnOptionsItemSelected("Новая фича");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toastOnOptionsItemSelected(CharSequence text) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END, 0, 0);
        toast.show();
    }
}

// Функционал. В целом - хочу надёжность сохранения заметок.  (Резервирование. Гибкую корзину. И резерирование корзины)
// По графике - фрагменты-мозаика с текстом/текстом+названием, с разным размером и количеством в ряд.
// Хочу кнопку копирования заметки + стрелки-клавиши для быстрого выделения фрагмента текста. Картинки мне не нужны в заметках (но я - не все), рисование можно.
// По удержанию - перемещение, выделение заметок, либо на первое время - копирование.
// Хорошая штука - ярлыки и цветовая диференциация, аналог каталогов файловой организации.

// Итого: поиск +всплывающее меню: копировать, сохранить в файл;  поделиться; +всплывающее меню: ярлык, закреп ввверху - меню на заметке;
// ✓ копирование заметки, поделиться, ярлык, закреп, +переименовать  - контекстное меню списка, //TODO: мультивыделение в списке
// ✓ поиск, +всплывающее меню: сортировки - меню на вьюхе списка,
// ✓ +меню приложения на списке - новая заметка, поиск, +вид отображения
//
// NavigationDrawer - аватарка, скины, список и счётчики ярлыков, список изменений (в т.ч сохранения в файл),
// размер занятой памяти.. и объём заметок приложением, корзина(архив), резервные стеки и их размер/объём.


// (Оригинальное задание:)
/* ✓1. Подумайте о функционале вашего приложения заметок. Какие экраны там могут быть, помимо основного со списком заметок?
   ✓ Как можно использовать меню и всплывающее меню в вашем приложении?
        Не обязательно сразу пытаться реализовать весь этот функционал, достаточно создать макеты и структуру,
   ✓    [от себя:] Задать все кнопки и меню. Украсить, скомпоновать, заменить пикчами часть надписей. Чисто разметить.
   ✓    а реализацию пока заменить на заглушки или всплывающие уведомления (Toast).
   ✓ Используйте подход Single Activity для отображения экранов.
    ✓   В качестве ПРИМЕРА: на главном экране приложения у вас список всех заметок, при нажатии на заметку открывается экран с этой заметкой.
    ✓   В меню главного экрана у вас есть иконка поиска по заметкам и сортировка.
    ✓   В меню «Заметки» у вас есть иконки «Переслать» (или «Поделиться»), «Добавить ссылку или фотографию к заметке».
   ✓2. Создайте боковое навигационное меню для своего приложения и добавьте туда хотя бы один экран, например «Настройки» или «О приложении».
   ✓3.(*доп.) Создайте полноценный заголовок для NavigationDrawer’а. К примеру, аватарка пользователя, его имя и какая-то дополнительная информация.*/
// Serega, sure