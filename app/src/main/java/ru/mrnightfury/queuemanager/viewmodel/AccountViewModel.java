package ru.mrnightfury.queuemanager.viewmodel;

import android.text.Editable;

import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.databinding.FragmentLoginBinding;
import ru.mrnightfury.queuemanager.model.AccountModel;
import ru.mrnightfury.queuemanager.model.User;
import ru.mrnightfury.queuemanager.repository.AccountRepository;
import ru.mrnightfury.queuemanager.repository.model.LoginStates;
import ru.mrnightfury.queuemanager.view.mainActivity.MainActivity;

public class AccountViewModel extends ViewModel {
    private LiveData<LoginStates> loginState;
    private AccountRepository repository = AccountRepository.getInstance();
    private String login;
    private String password;

    public AccountViewModel() {
        super();
        loginState = repository.getLoginState();
    }

    public LiveData<LoginStates> getLoginState() {
        return loginState;
    }

    public void connect() {
        repository.connect();
    }
}
