package com.android1.android1_notes.CityHeraldry6Lesson.event_bus;

import com.squareup.otto.Bus;

public class EventBus {
    private static Bus bus = null;

    // Сэнсэй говорит, синглтон - не зло, если им правильно пользоваться.
    // Это просто узко-профильный инструмент.
    // Он д.б. строго-обоснован, т.о. когда без него обойтись гораздо сложнее.
    // Напр., если нужно иметь один instance на всё приложение.
    // В нём не нужно хранить данные, активити, ответ сервера и т.д.

    public static Bus getBus() {
        if(bus == null) {
            bus = new Bus();
        }
        return bus;
    }
}
