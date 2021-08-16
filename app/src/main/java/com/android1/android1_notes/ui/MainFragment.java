package com.android1.android1_notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android1.android1_notes.MainActivity;
import com.android1.android1_notes.Navigation;
import com.android1.android1_notes.R;
import com.android1.android1_notes.data.CardData;
import com.android1.android1_notes.data.CardSourceFirebaseImpl;
import com.android1.android1_notes.data.CardsSource;
import com.android1.android1_notes.observer.Observer;
import com.android1.android1_notes.observer.Publisher;

public class MainFragment extends Fragment implements OnRegisterContext {

    public static final String CURRENT_NOTE = "CurrentNote";
    private static CardData note;

    private CardsSource data;
    private MainAdapter adapter;
    private RecyclerView recyclerView;
    private static final int MY_DEFAULT_DURATION = 1000; // Для анимации, класс DefaultItemAnimator

    private boolean isLandscape;
    private Navigation navigation;
    private Publisher publisher;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        *//* Поскольку onCreateView запускается каждый раз при возврате в фрагмент,
        данные надо создавать один раз *//*
        data = new CardsSourceImpl(getResources()).init();  // Получим источник данных для списка
    }*/

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);

        data = new CardSourceFirebaseImpl().init(cardsData -> adapter.notifyDataSetChanged()); // Для установки данных после чтения из Firestore
        adapter.setDataSource(data);

        setHasOptionsMenu(true); // Регестрируем меню приложения для фрагмента! Не забываем
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation // Для определение, можно ли открыть рядом заметку в другом фрагменте
                == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(CURRENT_NOTE); // Восстановление текущей позиции
            showNote(note);
        }
        // Участок ниже содержал исключение на случай отсутствия заметки, мы учли его showNote проверкой data на null
        /*else { //TODO to_Delete
            // Если восстановить не удалось, можно показать объект с первым индексом
            note = new CardData(getResources().getStringArray(R.array.notes_names)[0], getResources().getStringArray(R.array.notes)[0], getResources().getStringArray(R.array.notes_colors)[2]);
        }*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) { // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
        outState.putParcelable(CURRENT_NOTE, note);
        super.onSaveInstanceState(outState);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_notes_list);
        initRecyclerView();
    }

    // RecyclerView - размещает элементы списка, через Менеджера, а также делает запросы к Адаптеру на получение этих данных. Т.о. командует адаптером
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true); // Установка для повышения производительности системы (все эл-ты списка одинаковые по размеру, обработка ускорится)

        // Работаем со встроенным менеджером
        // Можно просто объявить менеджер в соотв-щем макете app:layoutManager="LinearLayoutManager" (ЕСЛИ менеджер стандартный)
        // Другой вариант - new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MainAdapter(this); // Установим адаптер
        recyclerView.setAdapter(adapter);

        //  Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,null));
        recyclerView.addItemDecoration(itemDecoration);

        // Установим долгоиграющую анимацию
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        adapter.setOnItemClickListener((view, position) -> { // Установим слушателя
            toastOnItemClickListener(position);
            showNote(data.getCardData(position-1)); // Магическая -1, для приведения 1-ой позиции списка заметок к 0-му индекса массива
        });
    }

    private void showNote(CardData data) {
        if (data != null) {
            note = data; // для savedInstanceState (showNote вызывается из разных мест)
            CardFragment note = CardFragment.newInstance(data); // Создаём новый фрагмент с текущей позицией для открытия заметки
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); //Получить менеджер фрагментов
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Открываем транзакцию по замене фрагмента
            fragmentTransaction.addToBackStack(null); // Перед показом заметки закинем лист заметок, с которого переходим, в БэкСтэк
//            fragmentTransaction.addToBackStack("NotesList");
            replacingFragment(note, fragmentTransaction);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }

    private void replacingFragment(CardFragment detail, FragmentTransaction fragmentTransaction) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);

        //TODO На ландщафтной ориентации добавленные меню дублируются, так как их фрагмент остаётся присутствовать на экране
        // Будто постоянно "пересоздаётся" экран/фрагмент при выборе меню
    }

    private void toastOnOptionsItemSelected(CharSequence text) {
        Toast toast = Toast.makeText(getContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.END, 0, 0);
        toast.show();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_main, menu);
    }

    @Override @SuppressLint("NonConstantResourceId")
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId){
        switch (menuItemId){
        // App menu part:
            case (R.id.add_note__main_menu):
                toastOnOptionsItemSelected("Добавление новой заметки");
                navigation.addFragment(CardFragment.newInstance(note), isLandscape, true);
                publisher.subscribe(cardData -> {
                    data.addCardData(cardData);
                    adapter.notifyItemInserted(data.size() - 1); // Говорит адаптеру добавить элемент в RecyclerView
                    recyclerView.smoothScrollToPosition(data.size() - 1); // scroll для анимации
                    // recyclerView.scrollToPosition(data.size() - 1); // Упрощенный scroll
                });
                return true;
            case R.id.notes_view_choice__main_menu:
                toastOnOptionsItemSelected("Выбор вида представления");
                return true;
        // Context menu part:
            case R.id.copy_note__context_main:
                toastOnOptionsItemSelected("Заметка скопирована");
                return true;
            case R.id.share_note__context_main:
                toastOnOptionsItemSelected("Заметка передана / Открыта через..");
                return true;
            case R.id.new_label__context_main:
                toastOnOptionsItemSelected("Добавлена новая метка");
                return true;
            case R.id.pin_to_top__context_main:
                toastOnOptionsItemSelected("Заметка закреплена");
                return true;
            case R.id.search__context_main:
                toastOnOptionsItemSelected("Поиск в заметке");
                return true;
            case R.id.info__context_main:
                toastOnOptionsItemSelected("Детали заметки");
                return true;
            case R.id.rename__context_main:
                int updatePosition = adapter.getContextPosition();
                toastOnOptionsItemSelected("Заметка переименована");
                navigation.addFragment(CardFragment.newInstance(data.getCardData(updatePosition)),true, true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCardData(CardData cardData) {
                        data.updateCardData(updatePosition, cardData);
                        adapter.notifyItemChanged(updatePosition);
                    }
                });
                return true;
            case R.id.delete__context_main:
                int deletePosition = adapter.getContextPosition();
                toastOnOptionsItemSelected("Заметка удалена");
                data.deleteCardData(deletePosition);
                adapter.notifyItemRemoved(deletePosition);
                return true;
        }
        return false;
    }

    @Override
    public void onRegister(View view) {
        registerForContextMenu(view); // Регестрируем Context menu
    }

}
