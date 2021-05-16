package com.android1.android1_notes.CityHeraldry6Lesson;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android1.android1_notes.CityHeraldry6Lesson.observer.FragmentMain_Observer;
import com.android1.android1_notes.CityHeraldry6Lesson.observer.Fragment_1_Observer;
import com.android1.android1_notes.CityHeraldry6Lesson.observer.Fragment_2_Observer;
import com.android1.android1_notes.CityHeraldry6Lesson.observer.Publisher;
import com.android1.android1_notes.CityHeraldry6Lesson.observer.PublisherGetter;

// В этом классе только создаём фрагменты и размещаем их на экране. Также подпишем два фрагмента
// в класс Publisher, чтобы потом оповещать их об изменениях. Метод GetPublisher нужен,
// только чтобы передать главному фрагменту обработчик подписок (Publisher).

public class MainActivity_Observer extends AppCompatActivity implements PublisherGetter {

    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__observer);

        // Создаём фрагменты
        Fragment_1_Observer fragment1 = new Fragment_1_Observer();
        Fragment_2_Observer fragment2 = new Fragment_2_Observer();
        FragmentMain_Observer fragmentMain = new FragmentMain_Observer();
        // Подписываем фрагменты
        publisher.subscribe(fragment1);
        publisher.subscribe(fragment2);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        // Добавить фрагменты
        fragmentTransaction.add(R.id.fragment_main, fragmentMain);
        fragmentTransaction.add(R.id.fragment_1, fragment1);
        fragmentTransaction.add(R.id.fragment_2, fragment2);
        // Закрыть транзакцию
        fragmentTransaction.commit();
    }

    // Снимем с activity обязанность по передаче событий классу Publisher.
    // Главный фрагмент будет использовать его для передачи событий
    @Override
    public Publisher getPublisher() {
        return publisher;
    }
    // Метод GetPublisher нужен, только чтобы передать главному фрагменту обработчик подписок (Publisher).
}