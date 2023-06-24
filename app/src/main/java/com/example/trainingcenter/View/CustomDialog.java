package com.example.trainingcenter.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import com.example.trainingcenter.R;

public class CustomDialog extends AppCompatDialogFragment {
    private CustomDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(view);
//        builder.setView(view)
//                .setTitle("Login")
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String username = editTextUsername.getText().toString();
//                        String password = editTextPassword.getText().toString();
//                        listener.applyTexts(username, password);
//                    }
//                });
//        editTextUsername = view.findViewById(R.id.edit_username);
//        editTextPassword = view.findViewById(R.id.edit_password);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        try {
//            listener = (CustomDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement ExampleDialogListener");
//        }
    }

    public interface CustomDialogListener {
        void enroll(String username, String password);
    }
}
