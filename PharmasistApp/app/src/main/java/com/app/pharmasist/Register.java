package com.app.pharmasist;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.tools.AlertDialogTask;
import com.app.tools.AsyncTasks;
import com.app.tools.MyTools;
import com.app.tools.ProgressTask;
import com.app.tools.ToastTask;
import com.app.user.User;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText txtName, txtLName, txtPhone, txtUser, txtPass;
    EditText txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening;
    Button btnRegister;
    ToastTask toastTask = new ToastTask(Register.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /* define fields and variables */

        btnRegister = findViewById(R.id.btnRegister2);

        txtName = findViewById(R.id.txtName);
        txtLName = findViewById(R.id.txtLName);
        txtPhone = findViewById(R.id.txtPhone);
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        txtStartMoorning = findViewById(R.id.txtStartMoorning);
        txtEndMoorning = findViewById(R.id.txtEndMoorning);
        txtStartEvening = findViewById(R.id.txtStartEvening);
        txtEndEvening = findViewById(R.id.txtEndEvening);

        /* call the time picker for this fields */

        MyTools.viewTimePicker(Register.this, txtStartMoorning);
        MyTools.viewTimePicker(Register.this, txtEndMoorning);
        MyTools.viewTimePicker(Register.this, txtStartEvening);
        MyTools.viewTimePicker(Register.this, txtEndEvening);

        /* set syntaxe error to all fields */
        User.setErrorForAllEditText(Register.this, btnRegister, txtName, txtLName, txtPhone, txtUser,
                txtPass, txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening);

    }

    /* method of registring a new pharmasist */

    public void register(View view) {
            if (
                /* checking the empty fields and syntaxe and order of working times */
                    User.checkBlankFields(getString(R.string.blank_field), txtName, txtLName, txtPhone, txtUser,
                            txtPass, txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening)
                 && User.checkSyntaxeFields(txtName, txtLName, txtPhone, txtUser,
                            txtPass, txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening)
                 && User.checkTimes(Register.this, txtStartMoorning, txtEndMoorning,
                            txtStartEvening, txtEndEvening)
            ) {
                String name = txtName.getText().toString();
                String lName = txtLName.getText().toString();
                String phone = txtPhone.getText().toString();
                String user = txtUser.getText().toString();
                String pass = txtPass.getText().toString();
                String startMoorning = txtStartMoorning.getText().toString();
                String endMoorning = txtEndMoorning.getText().toString();
                String startEvening = txtStartEvening.getText().toString();
                String endEvening = txtEndEvening.getText().toString();

                        // check if the gps enabled or not

                        if (MyTools.isLocationEnabled(Register.this)) {

                            /* This part work to get the current position(longitude and latitude) */

                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                            /* This part to check permession */

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    Activity#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for Activity#requestPermissions for more details.
                                    return;
                                }
                            }

                            /* This part complete the first part of getting the current position */

                            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location == null) {
                                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            }

                            if (location != null) {

                                double longitude = location.getLongitude();
                                double latitude = location.getLatitude();

                               registerTask(name, lName, phone, user, pass,
                                            startMoorning, endMoorning, startEvening, endEvening,
                                            longitude, latitude);

                            } else {
                                toastTask.showMsg(getResources().getString(R.string.location_err));
                            }

                        } else {
                            toastTask.showMsg(getResources().getString(R.string.gps_disable));
                        }
            }
    }

    /*
        Method of registring a new pharmasist
     */

    AlertDialog alertDialog1 = null;

    public void registerTask(String name, String lName, String phone, String user, String pass,
                             String startMoorning, String endMoorning, String startEvening,
                             String endEvening, double longitude, double latitude){

        ProgressTask progress = new ProgressTask(Register.this);
        progress.showProgress();

        AlertDialogTask dialog = new AlertDialogTask(Register.this);

        StringRequest jor = new StringRequest(Request.Method.POST, new AsyncTasks().REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    try {

                        switch (new JSONObject(response).getInt("response")){
                            case 0:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Register.this);
                                builder1.setTitle(getResources().getString(R.string.server_response));
                                builder1.setMessage(getString(R.string.user_saved));
                                builder1.setPositiveButton(getResources().getString(R.string.i_inderstand), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(alertDialog1.isShowing()){
                                            Intent intent = new Intent(Register.this, Login.class);
                                            startActivity(intent);
                                            alertDialog1.dismiss();
                                        }
                                    }
                                });
                                builder1.setCancelable(false);
                                alertDialog1 = builder1.create();
                                alertDialog1.show();
                                break;
                            case 1:
                                dialog.showAlertDialog(getString(R.string.server_response),
                                        getString(R.string.user_exist),
                                        getString(R.string.i_inderstand));
                                break;
                            case 2:
                                dialog.showAlertDialog(getString(R.string.server_response),
                                        getString(R.string.phone_exist),
                                        getString(R.string.i_inderstand));
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progress.hideProgress();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hideProgress();
                toastTask.showMsg(Register.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(Register.this.getResources().getString(R.string.check_your_internet));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fname", name);
                params.put("lname", lName);
                params.put("phone", phone);
                params.put("username", user);
                params.put("pass", pass);
                params.put("locationx", String.valueOf(longitude));
                params.put("locationy", String.valueOf(latitude));
                params.put("moorningStart", startMoorning);
                params.put("moorningEnd", endMoorning);
                params.put("eveningStart", startEvening);
                params.put("eveningEnd", endEvening);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Register.this);
        requestQueue.add(jor);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}