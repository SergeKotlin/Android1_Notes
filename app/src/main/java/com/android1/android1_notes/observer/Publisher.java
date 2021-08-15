package com.android1.android1_notes.observer;

import com.android1.android1_notes.data.CardData;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private final List<Observer> observers; // Все обозреватели

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
    public void notifySingle(CardData cardData) {
        for (Observer observer : observers) {
            observer.updateCardData(cardData);
            unsubscribe(observer);
        }
        // Примечание
        /* У нас будет разовый наблюдатель, каждый раз будем его регистрировать, поскольку фрагмент
           будет создаваться каждый раз при редактировании записи. После отсылки сообщения будем
           отписывать наблюдатель.*/
    }
}