package ru.mrnightfury.queuemanager.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginStates> loginState;
    private MutableLiveData<Integer> hint = new MutableLiveData<>(0);
    private AccountRepository repository = AccountRepository.getInstance();

    public LoginViewModel() {
        super();
        loginState = repository.getLoginState();
    }

    public LiveData<LoginStates> getLoginState() {
        return loginState;
    }

    public LiveData<Integer> getError() {
        return hint;
    }

    public void connect() {
        repository.connect();
    }

    public void login(String login, String password) {
        Log.i("TEST", "2");
        repository.setAccount(login, password);
        repository.login();
    }

    public void createAccount(String login, String password, String username) {
        if (!login.matches("[a-zA-Z0-9_]*")) {
            hint.setValue(R.string.login_contains_bad_chars);
            return;
        }
        if (!password.matches("[0-9a-zA-Z!#$%&?]*")) {
            hint.setValue(R.string.password_contains_bad_chars);
            return;
        }
        if (!username.matches("[\\S ]*")) {
            hint.setValue(R.string.username_contains_bad_chars);
            return;
        }
        if (login.length() < 4) {
            hint.setValue(R.string.short_login_error);
            return;
        }
        if (password.length() < 4) {
            hint.setValue(R.string.short_password_error);
            return;
        }
        repository.createAccount(login, password, username);
    }

    public String getStatus() {
        return repository.getStatus();
    }

    public void setState(LoginStates state) {
        loginState.setValue(state);
    }
}
