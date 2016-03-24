package com.watsonlogic.artifacts2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AlertUtil {
    String msg, title, posMsg, negMsg;
    boolean isCancelable;
    DialogInterface.OnClickListener posD, negD;
    AlertDialog.Builder dialog;
    Context context;

    public AlertUtil(String msg, String title, String posMsg, String negMsg, boolean isCancelable, DialogInterface.OnClickListener posD, DialogInterface.OnClickListener negD, Context context) {
        this.msg = msg;
        this.title = title;
        this.posMsg = posMsg;
        this.negMsg = negMsg;
        this.isCancelable = isCancelable;
        this.posD = posD;
        this.negD = negD;
        this.context = context;
    }

    protected void buildAlert() {
        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(msg).setCancelable(isCancelable);
        dialog.setTitle(title);
        dialog.setPositiveButton(posMsg, posD);
        dialog.setNegativeButton(negMsg, negD);
    }

    protected void showAlert() {
        dialog.show();
    }


}
