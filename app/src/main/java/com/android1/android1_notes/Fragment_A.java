package com.android1.android1_notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Fragment_A extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflater - доступ к layout; container - активити, куда конектится фрагмент

        // По умолчанию: return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment__a, container, false);
        //attachToRoot - всегда false (ВРУЧНУЮ! Default = true). Иное - редкий кейс для ручного управления для каких-то тестов.. Нам не нужно.
    } // Возвращаем View

    // Получаем View
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.one_button);
        button.setOnClickListener(v -> {
            getChildFragmentManager().beginTransaction() // внутри фрагмента (вместо getSupportFragmentManager() - для активити)
                    .replace(R.id.childFragmentContainer, new Fragment_C())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) // Стандартные анимации
                    .commit(); //АСИНХРОННО, не забыва
        });

    }
}



// Default auto-created code
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Fragment_A#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class Fragment_A extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public Fragment_A() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Fragment_A.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static Fragment_A newInstance(String param1, String param2) {
//        Fragment_A fragment = new Fragment_A();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment__a, container, false);
//    }
//}