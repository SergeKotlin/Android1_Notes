package com.android1.android1_notes;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android1.android1_notes.data.CardData;
import com.android1.android1_notes.data.CardsSource;

// Класс адаптера. Соединяет данные с их отображением. Через встроенный класс ViewHolder показывает данные в пользовательском интерфейсе
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final static String TAG = "MainAdapter";
    private final CardsSource dataSource; // Любая списковская структура данных, и элемент списка во вьюхе м.б любым - кроме фрагментов, они не допускаются
    private OnItemClickListener itemClickListener; // Слушатель, устанавливается извне
    private final Fragment fragment; // Чтобы повесить контекстное меню, а также contextPosition
    private int contextPosition;

    public MainAdapter(CardsSource dataSource, Fragment fragment) { // Передаём в конструктор источник данных (массив. А м.б и запрос к БД)
        this.dataSource = dataSource;
        this.fragment = fragment;
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
        viewHolder.setData(dataSource.getCardData(i));
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() { // Вызывается менеджером. Возвращает размер данных
        return dataSource.size();
    }

    public int getContextPosition() {
        return contextPosition;
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

        private final TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.listItemTextView);

            // Обработчик нажатий на этом ViewHolder
            name.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition()+1); // Магическая +1, для приведения 0-го индекса массива к 1-ой позиции списка заметок
                }
            });

            // Обработчик долгого клика на заметки из списка
            name.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public boolean onLongClick(View v) {
                    contextPosition = getLayoutPosition();
                    itemView.showContextMenu(10, 10); // ! У меня была API 22, а showContextMenu() поддерживается с 24 версии.
                    // Я чё-т натыркал, чтобы AVD привести сразу к API 30.. (24 недоступна в списке)
                    return true;
                }
            });

//            ((MainActivity)itemView.getContext()).registerForContextMenu(name); // Регистрируем контекстное меню (вообще-то MainFragment, но получилось пока для Activity)
            registerContextMenu(itemView); // Регестрируем Context menu
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null){
                itemView.setOnLongClickListener(v -> {
                    // Важно!:
                    // А чё ты думаешь, и всё? Нет, это лишь дублёр события. Нужно объявить само действие, как долгий клик на элементе
                    contextPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView); // Регестрируем Context menu
            }
        }

        // Формируем список городов
        public void setData(CardData cardData){
            name.setText(cardData.getName());
            name.setTextColor(Color.parseColor(cardData.getColor()));
        }
    }
}