package com.example.timetable.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import com.example.timetable.R;
import com.example.timetable.modules.DialogListener;
import java.util.Objects;

public class DeleteDialog extends DialogFragment implements View.OnClickListener {

    private int position;
    private DialogListener dialogListener;

    public DeleteDialog(DialogListener context, int position) {
        dialogListener = context;
        this.position = position;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.delete_dialog, null);
        v.findViewById(R.id.btn_del_dialog_remove).setOnClickListener(this);
        v.findViewById(R.id.btn_del_dialog_cancel).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_del_dialog_remove) {
            dialogListener.onClickRemoveDialog();
        } else {
            dialogListener.onClickCancelDialog(position);
        }
        dismiss();
    }
}
