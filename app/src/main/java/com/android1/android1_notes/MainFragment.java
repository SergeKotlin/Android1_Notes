package com.android1.android1_notes;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android1.android1_notes.data.CardData;
import com.android1.android1_notes.data.CardsSource;
import com.android1.android1_notes.data.CardsSourceImpl;

public class MainFragment extends Fragment {

    private boolean isLandscape;

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_notes_list);
        CardsSource data = new CardsSourceImpl(getResources()).init(); // Получим источник данных для списка
        initRecyclerView(recyclerView, data);

        setHasOptionsMenu(true); // Регестрируем меню приложения для фрагмента! Не забываем
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO Этот участок содержит исключение на случай отсутствия заметки, следует учесть и в новой логике с CardViews
        /*// Определение, можно ли будет открыть рядом заметку в другом фрагменте
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            // Если восстановить не удалось, можно показать объект с первым индексом
            currentNote = new Note(0, getResources().getStringArray(R.array.notes)[0]);
        }*/

        //TODO Не работает. Не получается организовать верную передачу и обработку заметки между ориентациями

        // Если можно показать текст заметки рядом, сделаем это. Первое отображение
        // showNote(currentNote);
    }

    //TODO #to_Delete Помечаю на удаление. Кажется, больше не использую
    /*// Вызывается после создания макета фрагмента
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initList(view); // проинициализируем список заметок
    }*/

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //TODO Всё ещё содержит старую ошибку поворота экрана, жду Марию для HW7
//        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    // RecyclerView - размещает элементы списка, через Менеджера, а также делает запросы к Адаптеру на получение этих данных. Т.о. командует адаптером
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initRecyclerView(RecyclerView recyclerView, CardsSource data){
        recyclerView.setHasFixedSize(true); // Установка для повышения производительности системы (все эл-ты списка одинаковые по размеру, обработка ускорится)

        // Работаем со встроенным менеджером
        // Можно просто объявить менеджер в соотв-щем макете app:layoutManager="LinearLayoutManager" (ЕСЛИ менеджер стандартный)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


        MainAdapter adapter = new MainAdapter(data); // Установим адаптер
        recyclerView.setAdapter(adapter);

        //  Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,null));
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener((view, position) -> { // Установим слушателя
            toastOnItemClickListener(position);
            showNote(data.getCardData(position-1)); // Магическая -1, для приведения 1-ой позиции списка заметок к 0-му индекса массива
        });
    }

    private void showNote(CardData data) {
        CardFragment note = CardFragment.newInstance(data); // Создаём новый фрагмент с текущей позицией для открытия заметки
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //Получить менеджер фрагментов
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Открываем транзакцию по замене фрагмента
        fragmentTransaction.addToBackStack(null); // Перед показом заметки закинем лист заметок, с которого переходим, в БэкСтэк
        replacingFragment(note, fragmentTransaction);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void replacingFragment(CardFragment detail, FragmentTransaction fragmentTransaction) {
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        // Замена фрагмента. Если можно показать текст заметки рядом, сделаем это
        if (isLandscape) {
            fragmentTransaction.replace(R.id.note_container, detail);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, detail);
        }
        // TODO можно сделать мульти-оконное открытие заметок (опционально, по выбору).
        // fragmentTransaction.add(R.id.fragment_container, detail);
    }

    @SuppressLint("DefaultLocale")
    private void toastOnItemClickListener(int position) {
        Toast.makeText(getContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }*/

    //TODO Нерешено
    @Override @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO На ландщафтной ориентации добавленные меню дублируются, так как их фрагмент остаётся присутствовать на экране
        // Постоянно "пересоздаётся" экран/фрагмент при выборе меню
        // К тому же, фрагмент для списка заметок будто добавляется дважды, тогда это мешает и корректному SaveInstance
        int id = item.getItemId();
        switch (id) {
            case R.id.notes_view_choice__main_menu:
//                toastOnOptionsItemSelected("Выбор вида представления");
                return true;
            //TODO !!! Сортировка должна быть здесь, на main фрагменте, но toasts срабатывают лишь на активити
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void toastOnOptionsItemSelected(CharSequence text) {
        Toast toast = Toast.makeText(getContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END, 0, 0);
        toast.show();
    }*/

}
