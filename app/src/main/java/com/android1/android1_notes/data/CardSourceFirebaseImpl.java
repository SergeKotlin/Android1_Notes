package com.android1.android1_notes.data;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Примечание:
/* Реализация интерфейса CardsSource, работающая с Firestore. Firestore — это
   документоориентированная база данных, в документе находятся наборы данных типа ключ-значение.
   Ключ всегда имеет строковый тип, значение может быть строковым, числовым, логическим, массивом,
   словарём или вложенным документом, который хранит, в свою очередь, пары ключ-значение.
   Документ также может хранить бинарные данные, но делать это не рекомендуется. Документ может
   содержать в себе и коллекции. Размер одного документа без учёта вложенных коллекций не может
   превышать 1 Мб. Уровень вложенности коллекций и документов — до 100 уровней. Коллекции могут
   содержать документы с разным набором полей, но делать так не рекомендуется. */
public class CardSourceFirebaseImpl implements CardsSource {

    private static final String CARDS_COLLECTION = "notes";
    private static final String TAG = "[NotesSourceFirebaseImpl]";

    private final FirebaseFirestore store = FirebaseFirestore.getInstance(); // База данных Firestore
    private final CollectionReference collection = store.collection(CARDS_COLLECTION); // Коллекция документов
    private List<CardData> cardsData = new ArrayList<CardData>(); // Загружаемый список карточек

    @Override @SuppressLint("LongLogTag")
    public CardsSource init(final CardsSourceResponse cardsSourceResponse) {
        // Получить всю коллекцию, отсортированную по полю «Дата»
        // При удачном считывании данных загрузим список карточек
        collection.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cardsData = new ArrayList<CardData>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> doc = document.getData();
                            String id = document.getId();
                            CardData cardData = CardDataMapping.toCardData(id,
                                    doc);
                            cardsData.add(cardData);
                        }
                        Log.d(TAG, "success " + cardsData.size() + " qnt");
                        cardsSourceResponse.initialized(CardSourceFirebaseImpl.this);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;
    }

    @Override
    public CardData getCardData(int position) {
        return cardsData.get(position);
    }

    @Override
    public int size() {
        if (cardsData == null){
            return 0;
        }
        return cardsData.size();
    }

    @Override
    public void deleteCardData(int position) {
        // Удалить документ с определённым идентификатором
        collection.document(cardsData.get(position).getId()).delete();
        cardsData.remove(position);
    }

    @Override
    public void updateCardData(int position, CardData cardData) {
        String id = cardData.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(CardDataMapping.toDocument(cardData));
    }

    @Override
    public void addCardData(final CardData cardData) {
        // Добавить документ
        collection.add(CardDataMapping.toDocument(cardData))
                .addOnSuccessListener(documentReference ->
                        cardData.setId(documentReference.getId()));
    }
}