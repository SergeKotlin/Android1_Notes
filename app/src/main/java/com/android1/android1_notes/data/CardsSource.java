package com.android1.android1_notes.data;

// Для получения данных для списка карточек работаем через интерфейс, делая возможным изменить поступление данных - например, из БД - а не из локальных ресурсов
// Проще говоря - соединяем массив (данные) с Note
public interface CardsSource {
    CardsSource init(CardsSourceResponse cardsSourceResponse); // Для асинхронных операций чтения из облака Firestore

    CardData getCardData(int position);
    int size();

    void deleteCardData(int position);
    void updateCardData(int position, CardData cardData);
    void addCardData(CardData cardData);
}