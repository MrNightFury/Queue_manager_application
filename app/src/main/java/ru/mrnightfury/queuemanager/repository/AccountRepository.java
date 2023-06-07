package ru.mrnightfury.queuemanager.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ru.mrnightfury.queuemanager.repository.model.AccountModel;
import ru.mrnightfury.queuemanager.repository.networkAPI.NetworkWorker;
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

    private final LiveData<AccountModel> model;
    private final SharedPrefsWorker sharedPrefs;
    private final MutableLiveData<LoginStates> loginState = new MutableLiveData<>(LoginStates.NONE);
    private final NetworkWorker worker;
    private String status;

    public String getStatus() {
        return status;
    }

    public boolean isLogged() {
        return loginState.getValue() == LoginStates.LOGGED;
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
            worker.setToken(token);
        }
    }

    public MutableLiveData<LoginStates> getLoginState() {
        return loginState;
    }

    public void connect() {
        loginState.setValue(LoginStates.CONNECTION_CHECKING);
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

    public void incorrectAccount() {
        this.loginState.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
    }

    public void logOut() {
        sharedPrefs.deleteAccount();
        model.getValue().deleteAccount();
        loginState.setValue(LoginStates.NONE);
    }
}
