package com.android1.android1_notes.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android1.android1_notes.MainActivity;
import com.android1.android1_notes.Navigation;
import com.android1.android1_notes.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class StartFragment extends Fragment {

    // Используется, чтобы определить результат activity регистрации через  Google
    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";

    private Navigation navigation;

    // Клиент для регистрации пользователя через Google
    private GoogleSignInClient googleSignInClient;

    // Кнопка регистрации через Google
    private com.google.android.gms.common.SignInButton buttonSignIn;

    // Кнопка выхода из Google
    private MaterialButton buttonSingOut;
    private TextView emailView;
    private MaterialButton continue_;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Получим навигацию по приложению, чтобы перейти на фрагмент со списком карточек
        MainActivity activity = (MainActivity)context;
        navigation = activity.getNavigation();
    }

    @Override
    public void onDetach() {
        navigation = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSign(); // Для начала следует проинициализировать запрос на аутентификацию и получить клиента для аутентификации
        initView(view);
        enableSign();
        return view;
    }

    // Инициализация запроса на аутентификацию
    private void initGoogleSign() {
        // Конфигурация запроса на регистрацию пользователя, чтобы получить
        // идентификатор пользователя, его почту и основной профайл (регулируется параметром)
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        // Получаем клиента для регистрации и данные по клиенту
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    // Инициализация пользовательских элементов
    private void initView(View view) {
        // Кнопка регистрации пользователя
        buttonSignIn = view.findViewById(R.id.sign_in_button);
        buttonSignIn.setOnClickListener(v -> signIn());
        emailView = view.findViewById(R.id.email);
        // Кнопка «Продолжить», будем показывать главный фрагмент
        continue_ = view.findViewById(R.id.continue_);
        continue_.setOnClickListener(v ->
                navigation.addFragment(MainFragment.newInstance(),
                        false, false));

        // Кнопка выхода
        buttonSingOut = view.findViewById(R.id.sing_out_button);
        buttonSingOut.setOnClickListener(v -> signOut());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Проверим, входил ли пользователь в это приложение через Google
        GoogleSignInAccount account =
                GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            // Пользователь уже входил, сделаем кнопку недоступной
            disableSign();
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            updateUI(account.getEmail());
        }
    }

    // Выход из учётной записи в приложении
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    updateUI("");
                    enableSign();
                });
    }

    // Инициируем регистрацию пользователя
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        // При помощи клиента инициируем запуск системной активити для выбора пользователем аккаунта
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Здесь получим ответ от системы, что пользователь вошёл
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты аутентификации уже готовы
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //https://developers.google.com/identity/sign-in/android/backend-auth?authuser=1
    // Получаем данные пользователя
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account =
                    completedTask.getResult(ApiException.class);

            // Регистрация прошла успешно
            disableSign();
            updateUI(account.getEmail());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure
            // reason. Please refer to the GoogleSignInStatusCodes class
            // reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            // ВАЖНО! Добавить email от логина в Support email в Project Settings (в FireBase)
            // https://stackoverflow.com/questions/47632035/google-sign-in-error-12500
        }
    }

    // Обновляем данные о пользователе на экране
    private void updateUI(String email) {
        emailView.setText(email);
    }

    // Разрешить аутентификацию и запретить остальные действия
    private void enableSign(){
        buttonSignIn.setEnabled(true);
        continue_.setEnabled(false);
        buttonSingOut.setEnabled(false);
    }

    // Запретить аутентификацию (уже прошла) и разрешить остальные действия
    private void disableSign(){
        buttonSignIn.setEnabled(false);
        continue_.setEnabled(true);
        buttonSingOut.setEnabled(true);
    }
}