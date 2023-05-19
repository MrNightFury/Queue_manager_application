package ru.mrnightfury.queuemanager.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkService;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkWorker;
import ru.mrnightfury.queuemanager.repository.networkAPI.QueueManagerAPI;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;

public class AccountRepository {
    private static AccountRepository instance;
    private static String TAG = "AR";
    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    private AccountModel model;
    private QueueManagerAPI API;
    private SharedPrefsWorker sharedPrefs;
    private MutableLiveData<LoginStates> loginState = new MutableLiveData<>(LoginStates.NONE);
    private NetworkWorker worker;

    private AccountRepository() {
        sharedPrefs = SharedPrefsWorker.getInstance();
        model = new AccountModel();
//        API = NetworkService.getInstance().getJSONApi();
        worker = NetworkWorker.getInstance();

        String login = sharedPrefs.getLogin();
        String password = sharedPrefs.getPassword();
        String token = sharedPrefs.getToken();

        if (login != null && password != null) {
            model.setAccount(login, password);
//            loginState.setValue(LoginStates.LOGGING);
        }
        if (token != null) {
            model.setToken(token);
        }
    }

    public LiveData<LoginStates> getLoginState() {
        return loginState;
    }

    public void connect() {
        loginState.setValue(LoginStates.NONE);
        worker.checkConnection(
                () -> {
                    loginState.setValue(LoginStates.CONNECTED);
                    checkExist();
                },
                () -> {
                    loginState.setValue(LoginStates.CONNECTION_FAILED);
                }
        );
    }

    public void checkExist() {
        if (model.hasAccount()) {
            loginState.setValue(LoginStates.LOGGING);
            login();
        } else {
            loginState.setValue(LoginStates.NOT_FOUND);
        }
    }

    public void login() {
        worker.logIn(model,
                result -> {
                    if (result.isSuccess()) {
                        model.setToken(result.getMessage());
                        loginState.setValue(LoginStates.LOGGED);
                    } else {
                        loginState.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
                    }
                },
                (call, t) -> {
                    loginState.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
                });
    }
}
