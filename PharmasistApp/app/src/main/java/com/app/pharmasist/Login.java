package com.app.pharmasist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.app.tools.SharedPreferencesImpl;
import com.app.tools.ToastTask;
import com.app.user.User;
import com.app.user.UserHome;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    The work of this class is:
        - get the user and password from the edit texts
        - send request to the server for log in
 */

public class Login extends AppCompatActivity {

    SharedPreferencesImpl spImpl;
    //EditText txtUser;
    EditText txtPass;
    Button btnLogin;
    AutoCompleteTextView txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin2);

        //txtUser = findViewById(R.id.txtUser1);
        txtUser = findViewById(R.id.txtUser1);
        txtPass = findViewById(R.id.txtPass1);

        setErrorForAllEditText();

        SharedPreferences sp = getSharedPreferences("emails", Context.MODE_PRIVATE);
        spImpl = new SharedPreferencesImpl(sp);

        ArrayList<String> emailsList = spImpl.load();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emailsList);
        txtUser.setAdapter(adapter);

    }

    public boolean checkBlankFields(){
        return MyTools.isBlank(txtUser, getString(R.string.blank_field))
                && MyTools.isBlank(txtPass, getString(R.string.blank_field));
    }

    public boolean checkSyntaxeFields(){
        return MyTools.isRespectSyntaxe(txtUser, MyTools.userNameSyntaxe)
                && MyTools.isRespectSyntaxe(txtPass, MyTools.passwordSyntaxe);
    }

    public void login(View view) {

        if(
            // checking the empty fields
                checkBlankFields() && checkSyntaxeFields()
        ){

            String user = txtUser.getText().toString();
            String pass = txtPass.getText().toString();

            getUser(user, pass);

        }

    }

    /*
        This method check the user if is exist in the server side
     */

    public void getUser(String user, String pass){

        ProgressTask progress = new ProgressTask(Login.this);
        progress.showProgress();

        AlertDialogTask dialog = new AlertDialogTask(Login.this);

        StringRequest jor = new StringRequest(Request.Method.POST, new AsyncTasks().FIND_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals(null)) {

                    try {

                        JSONArray jArray = new JSONArray(response);

                        if(jArray.length() > 0) {

                            JSONObject jObject = jArray.getJSONObject(0);

                            User.setId(jObject.getInt("id"));
                            User.setFname(jObject.getString("fname"));
                            User.setLname(jObject.getString("lname"));
                            User.setPhone(jObject.getString("phone"));
                            User.setUsername(jObject.getString("username"));
                            User.setPass(jObject.getString("pass"));
                            User.setLocationx(jObject.getDouble("locationx"));
                            User.setLocationy(jObject.getDouble("locationy"));
                            User.setMoorningStart(Time.valueOf(jObject.getString("moorningStart")));
                            User.setMoorningEnd(Time.valueOf(jObject.getString("moorningEnd")));
                            User.setEveningStart(Time.valueOf(jObject.getString("eveningStart")));
                            User.setEveningEnd(Time.valueOf(jObject.getString("eveningEnd")));

                            Intent intent = new Intent(Login.this, UserHome.class);
                            startActivity(intent);

                        }else
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.user_pass_wrong),
                                    getString(R.string.i_inderstand));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{

                    dialog.showAlertDialog(getString(R.string.server_response),
                            getString(R.string.user_pass_wrong),
                            getString(R.string.i_inderstand));

                }

                progress.hideProgress();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hideProgress();
                ToastTask toastTask = new ToastTask(Login.this);
                toastTask.showMsg(Login.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(Login.this.getResources().getString(R.string.check_your_internet));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", user);
                params.put("pass", pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(jor);

    }

    /*
        This method use to set the syntaxe error for the fields
     */

    public void setErrorForAllEditText(){

        MyTools.setError(txtUser, MyTools.userNameSyntaxe, getString(R.string.username_syntaxe), btnLogin);
        MyTools.setError(txtPass, MyTools.passwordSyntaxe, getString(R.string.password_syntaxe), btnLogin);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
