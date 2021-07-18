package com.android1.android1_notes;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android1.android1_notes.data.CardData;
import com.android1.android1_notes.data.CardsSource;

// Класс адаптера. Соединяет данные с их отображением. Через встроенный класс ViewHolder показывает данные в пользовательском интерфейсе
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final static String TAG = "MainAdapter";
    private CardsSource dataSource; // Любая списковская структура данных, и элемент списка во вьюхе м.б любым - кроме фрагментов, они не допускаются
    private OnItemClickListener itemClickListener; // Слушатель, устанавливается извне

    public MainAdapter(CardsSource dataSource) { // Передаём в конструктор источник данных (массив. А м.б и запрос к БД)
        this.dataSource = dataSource;
    }

    // Создадим новый пользовательский элемент
    @Override @NonNull
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { // Запускается менеджером
        View v = LayoutInflater.from(viewGroup.getContext()) // Создаём новый элемент пользовательского интерфейса
                .inflate(R.layout.list_item, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder");
        /* Здесь можно установить всякие параметры */
        return new ViewHolder(v);
    }

    // Заменим данные в пользовательском интерфейсе
    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder viewHolder, int i) { // Вызывается менеджером, подгружает данные и заполняет представления
        // Получить элемент из источника данных (БД, интернет...) и вывести на экран
//        viewHolder.getTextView().setText(dataSource[i]);
        viewHolder.setData(dataSource.getCardData(i));
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() { // Вызывается менеджером. Возвращает размер данных
        return dataSource.size();
    }

    // Сеттер слушателя нажатий
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс обработки нажатий (как в ListView)
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    // Этот класс хранит связь между данными и элементами View (сложные данные могут потребовать несколько View на один пункт списка)
    public class ViewHolder extends RecyclerView.ViewHolder {

        //        private TextView textView;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.listItemTextView);

            // Обработчик нажатий на этом ViewHolder
            name.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition()+1);
                }
            });

            ((MainActivity)itemView.getContext()).registerForContextMenu(name); // Регистрируем контекстное меню
        }

        public void setData(CardData cardData){
            name.setText(cardData.getName());
            name.setTextColor(Color.parseColor(cardData.getColor()));
        }
    }
}