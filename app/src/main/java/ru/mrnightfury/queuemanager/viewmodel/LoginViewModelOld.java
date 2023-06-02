package ru.mrnightfury.queuemanager.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.repository.model.LoginStates;

@Deprecated
public class LoginViewModelOld extends ViewModel {
    private static String TAG = "LVM";

    private MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<LoginStates> state = new MutableLiveData<>();
    private String status;
//    private final AccountModel accountModel = AccountModel.getInstance();

    public LiveData<String> getToken() {
        return token;
    }
    public LiveData<LoginStates> getState() {
        return state;
    }
    public String getStatus() {
        return status;
    }

    public void login() {
//        accountModel.login(
//                (result) -> {
//                    if (result.isSuccess()) {
//                        state.setValue(LoginStates.LOGGED);
//                    } else {
//                        state.setValue(LoginStates.INCORRECT_LOGIN_OR_PASSWORD);
//                        status = result.getMessage();
//                    }
//                },
//                (call, t) -> {
//                    state.setValue(LoginStates.CONNECTION_FAILED);
//                }
//        );
    }

    public void login(String login, String password) {
//        accountModel.setAccount(login, password);
//        login();
    }

    public void checkExist() {
//        Boolean hasAccount = accountModel.hasAccount();
//        Log.i(TAG, hasAccount.toString());
//        if (hasAccount) {
//            state.setValue(LoginStates.LOGGING);
//            login();
//        } else {
//            Log.i(TAG, "Странная херня");
//            state.setValue(LoginStates.NOT_FOUND);
//        }
    }

    public void initialize(Context context) {
//        accountModel.load(context);
//        accountModel.checkConnection(
//                () -> {
//                    state.setValue(LoginStates.CONNECTED);
//                    checkExist();
//                },
//                () -> state.setValue(LoginStates.CONNECTION_FAILED)
//        );
    }
}
