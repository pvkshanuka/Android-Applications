package com.app.wooker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Dialog_Lodaing extends AppCompatDialogFragment {

    String message;
    String title;

    public Dialog_Lodaing() {
    }

    @SuppressLint("ValidFragment")
    public Dialog_Lodaing(String message) {
        this.message = message;
    }

    @SuppressLint("ValidFragment")
    public Dialog_Lodaing(String message, String title) {
        this.message = message;
        this.title = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.loading_dialog, null);

        if (!(message == null || message.equals(""))){
            ((TextView) view.findViewById(R.id.tv_message)).setText(message);
        }

        if (!(title == null || title.equals(""))){
            ((TextView) view.findViewById(R.id.tv_titile)).setText(title);
        }

        builder.setView(view);

        return builder.create();

    }
}
