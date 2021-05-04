package com.android1.android1_notes.CityHeraldry6Lesson.observer;

import java.util.ArrayList;
import java.util.List;

    // Обработчик подписок
    // – умеет подписывать наблюдателей, отписывать их и оповещать

public class Publisher {
    private List<Observer> observers; // Все обозреватели
    public Publisher() {
        observers = new ArrayList<>();
    }
    // Подписать
    public void subscribe(Observer observer) {
        observers.add(observer);
    }
    // Отписать
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }
    // Разослать событие
    public void notify(String text) {
        for (Observer observer : observers) {
            observer.updateText(text);
        }
    }
}
