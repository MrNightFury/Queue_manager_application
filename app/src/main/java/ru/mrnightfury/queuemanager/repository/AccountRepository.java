package ru.mrnightfury.queuemanager.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkService;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkWorker;
import ru.mrnightfury.queuemanager.repository.networkAPI.QueueManagerAPI;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;
import ru.mrnightfury.queuemanager.repository.networkAPI.body.UserCreateRequest;

public class AccountRepository {
    private static AccountRepository instance;
    private static final String TAG = "AR";
    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    private LiveData<AccountModel> model;
//    private QueueManagerAPI API;
    private final SharedPrefsWorker sharedPrefs;
    private MutableLiveData<LoginStates> loginState = new MutableLiveData<>(LoginStates.NONE);
    private NetworkWorker worker;
    private String status;

    public String getStatus() {
        return status;
    }

    private AccountRepository() {
        sharedPrefs = SharedPrefsWorker.getInstance();
        model = AccountModel.getInstance();
        worker = NetworkWorker.getInstance();

        String login = sharedPrefs.getLogin();
        String password = sharedPrefs.getPassword();
        String token = sharedPrefs.getToken();

        if (login != null && password != null) {
            model.getValue().setAccount(login, password);
        }
        if (token != null) {
            model.getValue().setToken(token);
        }
    }

    public MutableLiveData<LoginStates> getLoginState() {
        return loginState;
    }

    public void connect() {
        loginState.setValue(LoginStates.CONNECTION_CHECKING);
        worker.checkConnection(
                () -> {
//                    Log.i("ASD", "ASSDASDASD");
                    loginState.setValue(LoginStates.CONNECTED);
                    checkExist();
                },
                () -> {
                    loginState.setValue(LoginStates.CONNECTION_FAILED);
                }
        );
    }

    public void checkExist() {
        if (model.getValue().hasAccount()) {
            login();
        } else {
            loginState.setValue(LoginStates.NOT_FOUND);
        }
    }

    public void setAccount(String login, String password) {
        model.getValue().setAccount(login, password);
    }

    public void login() {
        loginState.setValue(LoginStates.LOGGING);
        worker.logIn(model.getValue(),
                result -> {
                    if (result.isSuccess()) {
                        model.getValue().setToken(result.getMessage());
                        sharedPrefs.saveAccount(model.getValue().getLogin(), model.getValue().getPassword());
                        loginState.setValue(LoginStates.LOGGED);
                    } else {
                        loginState.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
                    }
                },
                (call, t) -> {
                    loginState.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
                });
    }

    public void createAccount(String login, String username, String password) {
        worker.createAccount(
                new UserCreateRequest(login, password, username),
                result -> {
                    if (result.isSuccess()) {
                        Log.i(TAG, "Account create result success");
                        model.getValue().setAccount(login, password);
                        login();
                    } else {
                        status = result.getMessage();
                        loginState.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
                    }
                },
                ((call, t) -> {
                    Log.i(TAG, "Error during account create");
//                    loginState.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
                })
        );
    }

    public void loadData() {
        worker.getUser(model.getValue().getLogin(),
                (user) -> {
                    model.getValue().setUserResponse(user);
                },
                (call ,t) -> {});
    }

    public LiveData<AccountModel> getAccount() {
        return model;
    }

    public void exit() {
        loginState.setValue(LoginStates.NONE);
    }
}
