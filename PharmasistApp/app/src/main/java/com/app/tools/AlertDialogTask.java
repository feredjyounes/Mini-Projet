package com.app.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;

public class AlertDialogTask {

    /*
        This class specially for alert dialog
     */

    AlertDialog alertDialog1;
    Context context;

    public AlertDialogTask(Context context){
        this.context = context;
        this.alertDialog1 = null;
    }

    public void showAlertDialog(String title, String msg, String btnText){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(msg);
        builder1.setMessage(title);
        builder1.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(alertDialog1.isShowing()){
                    alertDialog1.dismiss();
                }
            }
        });
        builder1.setCancelable(false);
        alertDialog1 = builder1.create();
        alertDialog1.show();
    }

}
