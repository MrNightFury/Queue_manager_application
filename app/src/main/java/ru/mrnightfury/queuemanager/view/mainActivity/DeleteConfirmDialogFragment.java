package ru.mrnightfury.queuemanager.view.mainActivity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.viewmodel.QueueViewModel;

public class DeleteConfirmDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.deletion_confirmation_dialog_text)
                .setPositiveButton(R.string.deletion_configmation_dialog_yes, (dialog, which) -> {
                    new ViewModelProvider(this).get(QueueViewModel.class).deleteQueue();
                })
                .setNegativeButton(R.string.deletion_confirmation_dialog_cancel, (dialog, which) -> {

                })
                .setCancelable(true)
                .create();
    }
}