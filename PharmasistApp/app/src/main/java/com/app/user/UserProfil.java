package com.app.user;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.pharmasist.Login;
import com.app.pharmasist.R;
import com.app.tools.AlertDialogTask;
import com.app.tools.AsyncTasks;
import com.app.tools.MyTools;
import com.app.tools.ProgressTask;
import com.app.tools.ToastTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class UserProfil extends AppCompatActivity {

    EditText txtName, txtLName, txtPhone, txtUser, txtPass;
    EditText txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening;
    CheckBox checkBox;
    Button btnSave;
    AlertDialogTask dialog = new AlertDialogTask(UserProfil.this);
    ProgressTask progress = new ProgressTask(UserProfil.this);
    ToastTask toastTask = new ToastTask(UserProfil.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);

        btnSave = findViewById(R.id.btnUpdate);

        defineFields();

        /* set syntaxe error to all fields */
        User.setErrorForAllEditText(UserProfil.this, btnSave, txtName, txtLName, txtPhone, txtUser,
                txtPass, txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening);

        fillFields();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dialog.showAlertDialog(getString(R.string.attention),
                        getString(R.string.use_it_if_you_change_position),
                        getString(R.string.i_inderstand));
            }
        });

        /* call the time picker for this fields */

        MyTools.viewTimePicker(UserProfil.this, txtStartMoorning);
        MyTools.viewTimePicker(UserProfil.this, txtEndMoorning);
        MyTools.viewTimePicker(UserProfil.this, txtStartEvening);
        MyTools.viewTimePicker(UserProfil.this, txtEndEvening);
    }

    private void fillFields() {

        txtName.setText(User.getFname());
        txtLName.setText(User.getLname());
        txtPhone.setText(User.getPhone());
        txtPass.setText(User.getPass());
        txtUser.setText(User.getUsername());
        txtStartMoorning.setText(User.getMoorningStart()+"");
        txtEndMoorning.setText(User.getMoorningEnd()+"");
        txtStartEvening.setText(User.getEveningStart()+"");
        txtEndEvening.setText(User.getEveningEnd()+"");

    }

    public void defineFields(){

        txtName = findViewById(R.id.txtName2);
        txtLName = findViewById(R.id.txtLName2);
        txtPhone = findViewById(R.id.txtPhone2);
        txtUser = findViewById(R.id.txtUser2);
        txtPass = findViewById(R.id.txtPass2);
        txtStartMoorning = findViewById(R.id.txtStartMoorning2);
        txtEndMoorning = findViewById(R.id.txtEndMoorning2);
        txtStartEvening = findViewById(R.id.txtStartEvening2);
        txtEndEvening = findViewById(R.id.txtEndEvening2);
        checkBox = findViewById(R.id.updatePosition);

    }

    public void update(View view) {

        if (
            /* checking the empty fields */
                User.checkBlankFields(getString(R.string.blank_field), txtName, txtLName, txtPhone, txtUser,
                        txtPass, txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening)
             && User.checkSyntaxeFields(txtName, txtLName, txtPhone, txtUser,
                        txtPass, txtStartMoorning, txtEndMoorning, txtStartEvening, txtEndEvening)
             && User.checkTimes(UserProfil.this, txtStartMoorning, txtEndMoorning,
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
            double x = User.getLocationx();
            double y = User.getLocationy();

            if(checkBox.isChecked()){
                if(MyTools.isLocationEnabled(UserProfil.this)){
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
                        x = location.getLongitude();
                        y = location.getLatitude();
                    }
                }else{
                    toastTask.showMsg(getResources().getString(R.string.gps_disable));
                }
            }
            int idUser = User.getId();
            updateTask(idUser, name, lName, phone, user, pass,
                    startMoorning, endMoorning, startEvening, endEvening,
                    x, y);
        }

    }

    private void updateTask(int idUser, String name, String lName, String phone, String user, String pass,
                 String startMoorning, String endMoorning, String startEvening, String endEvening, double x, double y) {
        progress.showProgress();
        StringRequest jor = new StringRequest(Request.Method.PUT, new AsyncTasks().UPDATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        int result = new JSONObject(response).getInt("response");
                        if(result == 0) {
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.user_updated),
                                    getString(R.string.i_inderstand));
                            User.setFname(name);
                            User.setLname(lName);
                            User.setPhone(phone);
                            User.setUsername(user);
                            User.setPass(pass);
                            User.setMoorningStart(Time.valueOf(startMoorning));
                            User.setMoorningEnd(Time.valueOf(endMoorning));
                            User.setEveningStart(Time.valueOf(startEvening));
                            User.setEveningEnd(Time.valueOf(endEvening));
                            User.setLocationx(x);
                            User.setLocationy(y);
                        }else
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.err_getting_info),
                                    getString(R.string.i_inderstand));
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
                toastTask.showMsg(UserProfil.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(UserProfil.this.getResources().getString(R.string.check_your_internet));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idUser));
                params.put("fname", name);
                params.put("lname", lName);
                params.put("phone", phone);
                params.put("username", user);
                params.put("pass", pass);
                params.put("locationx", String.valueOf(x));
                params.put("locationy", String.valueOf(y));
                params.put("moorningStart", startMoorning);
                params.put("moorningEnd", endMoorning);
                params.put("eveningStart", startEvening);
                params.put("eveningEnd", endEvening);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserProfil.this);
        requestQueue.add(jor);

    }

    public void deleteProfil(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(getResources().getString(R.string.attention));
        builder1.setMessage(getResources().getString(R.string.sure_to_delete));
        builder1.setPositiveButton(getResources().getString(R.string.i_inderstand), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletProfilTask(User.getId());
            }
        });
        builder1.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();
    }

    private void deletProfilTask(int id) {

        progress.showProgress();
        String url = new AsyncTasks().DELETE_USER + User.getId();
        StringRequest jor = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        int result = new JSONObject(response).getInt("response");
                        if(result == 0) {
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.user_deleted),
                                    getString(R.string.i_inderstand));
                            User.destructUser();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(UserProfil.this, Login.class);
                            startActivity(intent);
                        }else
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.err_getting_info),
                                    getString(R.string.i_inderstand));
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
                toastTask.showMsg(UserProfil.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(UserProfil.this.getResources().getString(R.string.check_your_internet));
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(UserProfil.this);
        requestQueue.add(jor);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }
}
