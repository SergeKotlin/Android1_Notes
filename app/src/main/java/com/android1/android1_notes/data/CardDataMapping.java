package com.android1.android1_notes.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

// Примечание:
/* При изменении и удалении нам не надо получать результат, на этом этапе будем работать по
принципу «отправил и забыл». А вот при добавлении документа надо получить идентификатор новой
записи и присвоить его в соответствующее поле нового документа. */

// Класс преобразования данных в словарь и в объект
public class CardDataMapping {

    public static class Fields {
        public final static String NAME = "name";
        public final static String TEXT = "text";
        public final static String COLOR = "color";
        public final static String DATE = "data";
    }

    public static CardData toCardData(String id, Map<String, Object> doc) {
        Timestamp timeStamp = (Timestamp)doc.get(Fields.DATE);
        CardData answer = new CardData((String) doc.get(Fields.NAME),
                (String) doc.get(Fields.TEXT),
                (String) doc.get(Fields.COLOR),
                timeStamp.toDate());
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(CardData cardData){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.NAME, cardData.getName());
        answer.put(Fields.TEXT, cardData.getText());
        answer.put(Fields.COLOR, cardData.getColor());
        answer.put(Fields.DATE, cardData.getDate());
        return answer;
    }
}