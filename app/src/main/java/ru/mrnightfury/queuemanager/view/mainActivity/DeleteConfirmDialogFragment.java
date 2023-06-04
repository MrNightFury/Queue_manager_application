package ru.mrnightfury.queuemanager.view.mainActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.mrnightfury.queuemanager.R;
import ru.mrnightfury.queuemanager.viewmodel.QueueViewModel;

public class DeleteConfirmDialogFragment extends DialogFragment {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_delete_confirm_dialog, container, false);
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(R.string.deletion_confirmation_dialog_text);
//        builder.setPositiveButton(R.string.deletion_configmation_dialog_yes, (dialog, which) -> {
//            new ViewModelProvider(this).get(QueueViewModel.class).deleteQueue();
//        });
//        builder.setNegativeButton(R.string.deletion_confirmation_dialog_cancel, (dialog, which) -> {
//
//        });
//        builder.setCancelable(true);
//        return builder.create();

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