package com.example.timetable.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import com.example.timetable.R;
import com.example.timetable.modules.AddClassDialogListener;

import java.util.Objects;

public class AddClassDialog extends DialogFragment implements View.OnClickListener {

    private AddClassDialogListener dialogListener;
    private String text = "";

    public AddClassDialog(AddClassDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.add_class_dialog, null);
        v.findViewById(R.id.btn_add_class_dialog_add).setOnClickListener(this);
        v.findViewById(R.id.btn_add_class_dialog_cancel).setOnClickListener(this);
        EditText editText = v.findViewById(R.id.et_add_class_dialog);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                text = s.toString();
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_class_dialog_add) {
            dialogListener.onClickDialogAdd(text);
        } else {
            dialogListener.onClickDialogCancel();
        }
        dismiss();
    }
}
