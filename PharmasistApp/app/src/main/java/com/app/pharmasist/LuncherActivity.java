package com.app.pharmasist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tools.AlertDialogTask;
import com.app.tools.AsyncTasks;
import com.app.tools.MyTools;
import com.app.tools.ToastTask;
import org.json.JSONException;
import org.json.JSONObject;

public class LuncherActivity extends AppCompatActivity {

    TextView textView;
    //Thread conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);

        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
           checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
           checkPermission(Manifest.permission.CALL_PHONE)){



        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CALL_PHONE}, 1000);
        }

        textView = findViewById(R.id.luncherTxtView);
        reconnect();

        /* conn = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while (true) {
                        reconnect();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        conn.start(); */

    }

    public void refresh(View view) {
        reconnect();
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void reconnect(){
        textView.setText(getString(R.string.checking_network));
        StringRequest jor = new StringRequest(Request.Method.POST, new AsyncTasks().CHECK_CONNECTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AlertDialogTask dialog = new AlertDialogTask(LuncherActivity.this);

                if (!response.equals(null)) {
                    try {
                        JSONObject jObject = new JSONObject(response);
                        if(jObject.getInt("response") == 0) {
                            textView.setText(getString(R.string.checking_gps));
                            if(MyTools.isLocationEnabled(LuncherActivity.this)) {
                                //conn.join();
                                Intent intent = new Intent(LuncherActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                textView.setText(getString(R.string.gps_disable));
                            }
                        }else{
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.check_your_internet),
                                    getString(R.string.i_inderstand));
                            textView.setText(getString(R.string.check_your_internet));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    dialog.showAlertDialog(getString(R.string.server_response),
                            getString(R.string.check_your_internet),
                            getString(R.string.i_inderstand));
                    textView.setText(getString(R.string.check_your_internet));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastTask toastTask = new ToastTask(LuncherActivity.this);
                toastTask.showMsg(LuncherActivity.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(LuncherActivity.this.getResources().getString(R.string.check_your_internet));
                textView.setText(getString(R.string.check_your_internet));
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(LuncherActivity.this);
        requestQueue.add(jor);
    }

}
