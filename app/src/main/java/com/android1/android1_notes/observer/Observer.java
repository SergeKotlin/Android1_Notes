package com.android1.android1_notes.observer;

import com.android1.android1_notes.data.CardData;

// Поскольку мы планируем обмен между фрагментами, нам надо создать паттерн «Наблюдатель»,
// при помощи которого мы и будем обмениваться информацией

// Вместо Observer - можно воспользоваться EVENT BUS

// Паттерн Наблюдатель. Интерфейс наблюдателя:
public interface Observer {
    void updateCardData(CardData cardData);
}
