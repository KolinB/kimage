package com.kolin.kimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by kolin on 27/05/15.
 */

public class NetworkErrorDialog extends DialogFragment {
    int mNum;
    String text;
    DialogInterface.OnClickListener okClick;
    DialogInterface.OnClickListener cancelClick;

    static NetworkErrorDialog newInstance(String text, DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancelClick) {
        NetworkErrorDialog f = new NetworkErrorDialog();
        f.text = text;
        f.okClick = okClick;
        f.cancelClick = cancelClick;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())

                .setMessage(text)
                .setPositiveButton(cancelClick == null ? "Go" : "Retry",
                        okClick
                );
        if (cancelClick != null) {
            builder.setNegativeButton("Quit",
                    cancelClick).setTitle("Sorry");
        } else
            builder.setTitle("Kolin's test");

        return builder.create();
    }
}


