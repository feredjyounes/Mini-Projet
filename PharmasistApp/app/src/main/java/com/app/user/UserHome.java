package com.app.user;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.pharmasist.R;
import com.app.tools.AlertDialogTask;
import com.app.tools.AsyncTasks;
import com.app.tools.MyTools;
import com.app.tools.ProgressTask;
import com.app.tools.SharedPreferencesImpl;
import com.app.tools.ToastTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserHome extends AppCompatActivity {

    TextView txtFullName, txtPhone, txtEmail, txtDate, txtTime, txtStatus;
    ListView productsListView;
    ArrayList<Product> productsList;
    ProductAdapter myAdapter;
    EditText searchText;
    RadioGroup radioGroup;
    LinearLayout userLayout;
    SharedPreferencesImpl spImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // This part for auto complet in login field
        SharedPreferences sp = getSharedPreferences("emails", Context.MODE_PRIVATE);
        spImpl = new SharedPreferencesImpl(sp);
        spImpl.save(User.getUsername());

        productsListView = findViewById(R.id.productsListView);
        radioGroup = findViewById(R.id.radioGroup2);

        txtFullName = findViewById(R.id.txtFullName);
        txtPhone = findViewById(R.id.txtUserPhone);
        txtEmail = findViewById(R.id.txtUserEmail);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtStatus = findViewById(R.id.txtStatus);
        txtFullName.setText(User.getFname() + " " + User.getLname());
        txtPhone.setText(User.getPhone());
        txtEmail.setText(User.getUsername());
        searchText = findViewById(R.id.searchText);
        userLayout = findViewById(R.id.userLayout);

        setOnSearchListener();

        setDate();

        String url = new AsyncTasks().GET_USER_PRODUCT + User.getId() + "/1";
        getProductsList(url);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String url = new AsyncTasks().GET_USER_PRODUCT + User.getId();
                switch (checkedId){
                    case (R.id.alphabetRadio2):
                        url += "/1";
                        break;
                    case (R.id.priceRadio2):
                        url += "/2";
                        break;
                    case (R.id.expirationRadio):
                        url += "/3";
                        break;
                }
                getProductsList(url);
            }
        });

    }

    public void setDate(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    String time = new SimpleDateFormat("hh:mm:ss a").format(new Date());

                    boolean result = MyTools.isPharmasistOpen(User.getMoorningStart(),
                                                                User.getMoorningEnd(),
                                                                User.getEveningStart(),
                                                                User.getEveningEnd());

                    String status = result == true ? getString(R.string.open) : getString(R.string.close);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtDate.setText(date);
                            txtTime.setText(time);
                            txtStatus.setText(status);
                        }
                    });
                }
            }
        });
        thread.start();
    }

    public void profile(View view) {
        Intent intent = new Intent(this, UserProfil.class);
        startActivity(intent);
    }

    public void goToAddProduct(View view) {
        Intent intent = new Intent(this, AddProduct.class);
        startActivity(intent);
    }

    public void setting(View view) {
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    public class ProductAdapter extends BaseAdapter {

        ArrayList<Product> list = new ArrayList<Product>();

        public ProductAdapter(ArrayList<Product> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.product_item, null);

            ImageView img = view.findViewById(R.id.productImg);
            TextView productName = view.findViewById(R.id.productName2);
            TextView productForm = view.findViewById(R.id.productForm2);
            TextView productDose = view.findViewById(R.id.productDose2);
            TextView productPrice = view.findViewById(R.id.productPrice2);
            TextView productLife = view.findViewById(R.id.productLife);
            Button btnUpdate = view.findViewById(R.id.productUpdate2);
            Button btnDelete = view.findViewById(R.id.productDelete2);

            img.setImageResource(R.drawable.medical_bag);

            productName.setText(list.get(position).productName);
            productForm.setText(list.get(position).productForm);
            productDose.setText(list.get(position).productDose);
            productPrice.setText(list.get(position).productPrice+"");

            Date now = new Date();
            Date product = list.get(position).productExpiration;

            if((now.compareTo(product) == -1) ||(now.compareTo(product) == 0)){
                productLife.setText(getString(R.string.product_life));
                productLife.setTextColor(Color.GREEN);
            }else{
                productLife.setText(getString(R.string.product_in_life));
                productLife.setTextColor(Color.RED);
            }

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(UserHome.this, UpdateProduct.class);
                    intent.putExtra("productId", list.get(position).productId);
                    intent.putExtra("productName", list.get(position).productName);
                    intent.putExtra("productForm", list.get(position).productForm);
                    intent.putExtra("productDose", list.get(position).productDose);
                    intent.putExtra("productQuantity", list.get(position).productQuantity);
                    intent.putExtra("productPrice", list.get(position).productPrice);
                    intent.putExtra("productExpiration", list.get(position).productExpiration.toString());
                    startActivity(intent);

                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(UserHome.this);
                    builder1.setTitle(getResources().getString(R.string.loading));
                    builder1.setMessage(getResources().getString(R.string.sure_to_delete_product));
                    builder1.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteProduct(list.get(position).productId);
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
            });

            return view;
        }
    }

    private void deleteProduct(int productId) {

        ProgressTask progress = new ProgressTask(UserHome.this);
        progress.showProgress();

        AlertDialogTask dialog = new AlertDialogTask(UserHome.this);

        StringRequest jor = new StringRequest(Request.Method.DELETE, new AsyncTasks().DELETE_PRODUCT + productId
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals(null)) {

                    try {

                        int result = new JSONObject(response).getInt("response");
                        if(result == 0) {
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.product_deleted),
                                    getString(R.string.i_inderstand));
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {

                            }
                            Intent intent = new Intent(UserHome.this, UserHome.class);
                            startActivity(intent);

                        }else
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.err_getting_info),
                                    getString(R.string.i_inderstand));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{

                    dialog.showAlertDialog(getString(R.string.server_response),
                            getString(R.string.err_getting_info),
                            getString(R.string.i_inderstand));

                }

                progress.hideProgress();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hideProgress();
                ToastTask toastTask = new ToastTask(UserHome.this);
                toastTask.showMsg(UserHome.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(UserHome.this.getResources().getString(R.string.check_your_internet));
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(UserHome.this);
        requestQueue.add(jor);

    }

    public void getProductsList(String url){

        productsList = new ArrayList<>();
        ProgressTask progress = new ProgressTask(UserHome.this);
        progress.showProgress();

        AlertDialogTask dialog = new AlertDialogTask(UserHome.this);

        StringRequest jor = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals(null)) {

                    try {

                        JSONArray jArray = new JSONArray(response);

                        if(jArray.length() > 0) {

                            JSONArray jsonArray = new JSONArray(response);

                            userLayout.setBackground(null);

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                java.sql.Date productExp = java.sql.Date.valueOf(jsonObject.getString("productExpiration"));
                                productsList.add(new Product(jsonObject.getInt("productId"), jsonObject.getString("productName"),
                                        jsonObject.getString("productForm"), jsonObject.getString("productCntr"),
                                        jsonObject.getInt("productQuantity"), jsonObject.getDouble("productPrice"),
                                        productExp));
                            }

                            myAdapter = new ProductAdapter(productsList);
                            productsListView.setAdapter(myAdapter);

                        } else
                            productsListView.setAdapter(null);
                             /* dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.no_products),
                                    getString(R.string.i_inderstand)); */

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{

                    dialog.showAlertDialog(getString(R.string.server_response),
                            getString(R.string.err_getting_info),
                            getString(R.string.i_inderstand));

                }

                progress.hideProgress();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hideProgress();
                ToastTask toastTask = new ToastTask(UserHome.this);
                toastTask.showMsg(UserHome.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(UserHome.this.getResources().getString(R.string.check_your_internet));
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(UserHome.this);
        requestQueue.add(jor);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }

    public void setOnSearchListener(){

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String product = s.toString().toLowerCase();
                ArrayList<Product> serchableProductList = new ArrayList<>();

                if(s.toString().equals(null) || s.toString().length() == 0){
                    userLayout.setBackground(getResources().getDrawable(R.drawable.logo3));
                    myAdapter = new ProductAdapter(productsList);

                }else{
                    userLayout.setBackground(null);
                    for(int i=0; i<productsList.size(); i++){
                        if(productsList.get(i).productName.contains(product))
                            serchableProductList.add(productsList.get(i));
                    }
                    myAdapter = new ProductAdapter(serchableProductList);
                }
                productsListView.setAdapter(myAdapter);

            }
        });

    }


}
