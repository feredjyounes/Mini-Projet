package com.app.tools;

import android.content.Context;
import android.widget.Toast;

public class ToastTask {

    /*
        This class specially for showing the toast
     */

    Context context;
    public ToastTask(Context context){
        this.context = context;
    }

    public void showMsg(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
