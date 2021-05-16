package com.android1.android1_notes.CityHeraldry6Lesson.observer;

    // Наблюдатель. Вызывается updateText, когда текст изменился, и нужно оповестить об этом событии

public interface Observer {
    void updateText(String text);
}
