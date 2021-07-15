package com.android1.android1_notes.CityHeraldry6Lesson;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android1.android1_notes.CityHeraldry6Lesson.event_bus.EventBus;
import com.android1.android1_notes.CityHeraldry6Lesson.event_bus.events.ButtonClickedEvent;
import com.squareup.otto.Subscribe;

public class CitiesFragment extends Fragment {

    public static final String CURRENT_CITY = "CurrentCity";
//    private int currentPosition = 0;    // Текущая позиция (выбранный город)
    private City currentCity;    // Текущая позиция (выбранный город)
    private boolean isLandscape;

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    // вызывается после создания макета фрагмента, здесь мы проинициализируем список
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    //
    // Почитаем Event, что "пульнули" в CatOfArmsFragment (для примера с EventBus)
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);

    }
    // Обязательно нужно отписываться от слушания Event'а.
    // Чтобы не было crash на повторной подписке на объект слушания
    @Override
    public void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }
    //

    // создаём список городов на экране из массива в ресурсах
    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] cities = getResources().getStringArray(R.array.cities);

        LayoutInflater layoutInflater = getLayoutInflater(); // При помощи этого объекта будем доставать элементы, спрятанные в item.xml

        for(int i=0; i < cities.length; i++){
            String city = cities[i];

            View item = layoutInflater.inflate(R.layout.item, layoutView, false); // Достаём элемент из item.xml
            TextView tv = item.findViewById(R.id.textView); // Находим в этом элементе TextView
            tv.setText(city);
            layoutView.addView(item);

            /*TextView tv = new TextView(getContext()); // В этом цикле создаём элемент TextView, заполняем значениями и добавляем на экран.
            tv.setText(city);
            tv.setTextSize(30);
            layoutView.addView(tv);*/

            final int fi = i; // не можем внутрь анонимного класса передать не final - иначе гонка потоков
            tv.setOnClickListener(v -> {
//                    currentPosition = fi;
//                    showCoatOfArms(currentPosition);
                currentCity = new City(fi, getResources().getStringArray(R.array.cities)[fi]);
                showCoatOfArms(currentCity);
            });
        }
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putInt(CURRENT_CITY, currentPosition);
        outState.putParcelable(CURRENT_CITY, currentCity);
        super.onSaveInstanceState(outState);
    }

    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет расположить рядом герб в другом фрагменте
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции.
//            currentPosition = savedInstanceState.getInt(CURRENT_CITY, 0);
            currentCity = savedInstanceState.getParcelable(CURRENT_CITY);
        } else {
            // Если восстановить не удалось, то сделаем объект с первым индексом
            currentCity = new City(0, getResources().getStringArray(R.array.cities)[0]);
        }

        // Если можно нарисовать рядом герб, то сделаем это
        if (isLandscape) {
//            showLandCoatOfArms(0);
            showLandCoatOfArms(currentCity);
        }
    }

//    private void showCoatOfArms(int index) {
    private void showCoatOfArms(City currentCity) {
        if (isLandscape) {
//            showLandCoatOfArms(index);
            showLandCoatOfArms(currentCity);
        } else {
//            showPortCoatOfArms(index);
            showPortCoatOfArms(currentCity);
        }
    }

    // Показать герб в ландшафтной ориентации
//    private void showLandCoatOfArms(int index) {
    private void showLandCoatOfArms(City currentCity) {
        // Создаём новый фрагмент с текущей позицией для вывода герба
//        CoatOfArmsFragment detail = CoatOfArmsFragment.newInstance(index);
        CoatOfArmsFragment detail = CoatOfArmsFragment.newInstance(currentCity);
        // newInstance - создание фрагмгента в статичном методе

        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // - сэнсэй заметил, что вернее было бы внутри фрагмента просто взять ChildFragmentManager
        // т.е: FragmentManager fragmentManager = getChildFragmentManager();
        // Фрагменты должны свободно жить в приложении, как самоизолированные юниты,
        // с потенциальной возможностью быть вызванными в любой активити, без жёстких привязок
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.coat_of_arms, detail);  // замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    // Показать герб в портретной ориентации.
//    private void showPortCoatOfArms(int index) {
    private void showPortCoatOfArms(City currentCity) {
        // Откроем вторую activity
        Intent intent = new Intent();
        intent.setClass(getActivity(), CoatOfArmsActivity.class);
        // и передадим туда параметры
//        intent.putExtra(CoatOfArmsFragment.ARG_INDEX, index);
        intent.putExtra(CoatOfArmsFragment.ARG_CITY, currentCity);
        startActivity(intent);
    }

    //
    // Публичный метод слушания Event'а (для примера с EventBus)
    @Subscribe
    public void onButtonClickedEvent(ButtonClickedEvent event) {
        Toast.makeText(requireContext(), String.valueOf(event.count),
                Toast.LENGTH_SHORT).show();
    }

}