package com.example.trainingcenter.View.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class SingleChoiceDialogFragment extends AppCompatDialogFragment {

    private int checkedItem = 0; // this will hold the currently selected days
    private int checkedItem2 = 0; // this will hold the currently selected times
    private String[] dayArray;
    private String[] timeArray;
    private TextView course_schedule;

    public SingleChoiceDialogFragment(String[] dayArray,String[] timeArray, TextView course_schedule) {
        this.dayArray = dayArray;
        this.timeArray = timeArray;
        this.course_schedule = course_schedule;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Days")
                .setCancelable(false)
                .setSingleChoiceItems(dayArray, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkedItem = i;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Select Times")
                                .setCancelable(false)
                                .setSingleChoiceItems(timeArray, checkedItem2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkedItem2 = i;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  course_schedule.setText(dayArray[checkedItem]+" "+timeArray[checkedItem2]);
                              }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


        return builder.create();
    }
}