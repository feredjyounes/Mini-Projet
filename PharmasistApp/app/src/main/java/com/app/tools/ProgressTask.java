package com.app.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import com.app.pharmasist.R;

public class ProgressTask {

    /*
        This class specially for showing the progress bar
     */

    AlertDialog dialog;
    Context context;

    public ProgressTask(Context context){
        this.context = context;
        this.dialog = null;
    }

    public void showProgress(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.layout_loading_dialog);
        }
        dialog = builder.create();
        dialog.show();
    }

    public void hideProgress(){
        dialog.dismiss();
    }

}
