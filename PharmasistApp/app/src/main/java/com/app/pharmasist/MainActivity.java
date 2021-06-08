package com.app.pharmasist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

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
import com.app.user.Apropos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /*
        The work of this class is :
            - searching for products from the server using the normal way or when the radio button
                changed listener.
            - Load and save or clear the history of research using the shared preferences
     */

    EditText productSearchable;
    ArrayList<SearchableProduct> productsList;
    ListView productsListView;
    ProductAdapter myAdapter;
    ToastTask toastTask = new ToastTask(MainActivity.this);
    RadioGroup radioGroup;
    LinearLayout mainLayout;
    SharedPreferencesImpl spImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productSearchable = findViewById(R.id.txtProductSearchable);
        productsListView = findViewById(R.id.searchableProductsListView);
        radioGroup = findViewById(R.id.radioGroup);
        mainLayout = findViewById(R.id.mainLayout);

        /*
            Get the file was named search
         */

        SharedPreferences sp = getSharedPreferences("search", Context.MODE_PRIVATE);
        spImpl = new SharedPreferencesImpl(sp);

        searchProducts();

        /*
            Get the context of the file was named search
         */

        loadHistory();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String product = productSearchable.getText().toString();
                if(product.length() != 0) {
                    String url = new AsyncTasks().SEARCH_PRODUCT + product;
                    switch (checkedId) {
                        case (R.id.alphabetRadio):
                            url += "/1";
                            break;
                        case (R.id.priceRadio):
                            url += "/2";
                            break;
                    }
                    getProductsList(url);
                }else {
                    loadHistory();
                }
            }
        });

    }

    /*
        Bind the text change listener to the field of research
     */

    public void searchProducts() {

        productSearchable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String product = s.toString().toLowerCase();
                if (product.length() == 0) {
                    productsListView.setAdapter(null);
                    loadHistory();
                    mainLayout.setBackground(getResources().getDrawable(R.drawable.logo3));
                } else {
                    mainLayout.setBackground(null);
                    String url = new AsyncTasks().SEARCH_PRODUCT + product;
                    switch (radioGroup.getCheckedRadioButtonId()){
                        case (R.id.alphabetRadio):
                            url += "/1";
                            break;
                        case (R.id.priceRadio):
                            url += "/2";
                            break;
                    }
                    getProductsList(url);
                }
            }
        });

    }

    /*
        Get and search for the product saved on the shared preferences
     */

    public void loadHistory(){
        if(spImpl.load() != null) {
            String list = spImpl.load().toString().trim();
            list = list.substring(1, list.length() - 1);
            list = list.replace(" ", "");
            String url = new AsyncTasks().SEARCH_PRODUCT_LIST + list;
            switch (radioGroup.getCheckedRadioButtonId()) {
                case (R.id.alphabetRadio):
                    url += "/1";
                    break;
                case (R.id.priceRadio):
                    url += "/2";
                    break;
            }

            getProductsList(url);
        }
    }

    /*
        Fill the list view of product
     */

    public void getProductsList(String url) {

        productsList = new ArrayList<>();
        ProgressTask progress = new ProgressTask(MainActivity.this);
        progress.showProgress();

        AlertDialogTask dialog = new AlertDialogTask(MainActivity.this);

        StringRequest jor = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals(null)) {

                    try {

                        JSONArray jArray = new JSONArray(response);

                        if (jArray.length() > 0) {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObjectProduct = jsonArray.getJSONObject(i);
                                JSONObject jsonObjectUser = jsonObjectProduct.getJSONObject("user");

                                SearchableProduct searchableProduct = new SearchableProduct();

                                searchableProduct.productId = jsonObjectProduct.getInt("productId");
                                searchableProduct.productName = jsonObjectProduct.getString("productName");
                                searchableProduct.productForm = jsonObjectProduct.getString("productForm");
                                searchableProduct.productDose = jsonObjectProduct.getString("productCntr");
                                searchableProduct.productQuantity = jsonObjectProduct.getInt("productQuantity");
                                searchableProduct.productPrice = jsonObjectProduct.getDouble("productPrice");
                                searchableProduct.productExpiration = java.sql.Date.valueOf(jsonObjectProduct.getString("productExpiration"));
                                searchableProduct.id = jsonObjectUser.getInt("id");
                                searchableProduct.fname = jsonObjectUser.getString("fname");
                                searchableProduct.lname = jsonObjectUser.getString("lname");
                                searchableProduct.phone = jsonObjectUser.getString("phone");
                                searchableProduct.locationx = jsonObjectUser.getDouble("locationx");
                                searchableProduct.locationy = jsonObjectUser.getDouble("locationy");
                                searchableProduct.moorningStart = Time.valueOf(jsonObjectUser.getString("moorningStart"));
                                searchableProduct.moorningEnd = Time.valueOf(jsonObjectUser.getString("moorningEnd"));
                                searchableProduct.eveningStart = Time.valueOf(jsonObjectUser.getString("eveningStart"));
                                searchableProduct.eveningEnd = Time.valueOf(jsonObjectUser.getString("eveningEnd"));

                                productsList.add(searchableProduct);
                            }

                            myAdapter = new ProductAdapter(productsList);
                            productsListView.setAdapter(myAdapter);

                        } else
                            productsListView.setAdapter(null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

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
                ToastTask toastTask = new ToastTask(MainActivity.this);
                toastTask.showMsg(MainActivity.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(MainActivity.this.getResources().getString(R.string.check_your_internet));
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jor);

    }

    /*
        Make the shared preferences empty
     */

    public void clearHistory(View view) {
        spImpl.clear();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void searchWithSpeach(View view) {
        speak();
    }

    private final int REQUEST_CODE = 8000;
    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.say_something));
        try{
            startActivityForResult(intent, REQUEST_CODE);
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    productSearchable.setText(result.get(0));
                }
                break;
        }
    }

    public void about_us(View view) {
        Intent intent = new Intent(this, Apropos.class);
        startActivity(intent);
    }

    /*
        This class references to the list of product (Adapter of the products)
     */

    public class ProductAdapter extends BaseAdapter {

        ArrayList<SearchableProduct> list = new ArrayList<SearchableProduct>();

        public ProductAdapter(ArrayList<SearchableProduct> list) {
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
            View view = inflater.inflate(R.layout.product_searchable_item, null);

            ImageView img = view.findViewById(R.id.productImg);
            TextView productName = view.findViewById(R.id.productName3);
            TextView productForm = view.findViewById(R.id.productForm3);
            TextView productDose = view.findViewById(R.id.productDose3);
            TextView productPrice = view.findViewById(R.id.productPrice3);
            TextView productDistance = view.findViewById(R.id.txtDistance);
            TextView pharmasistStatus = view.findViewById(R.id.txtStatus3);
            TextView pharmasistFullName = view.findViewById(R.id.txtFullName3);
            TextView pharmasistPhone = view.findViewById(R.id.txtPhone3);
            Button btnDetail = view.findViewById(R.id.btnDetail);

            img.setImageResource(R.drawable.medical_bag);

            SearchableProduct searchableProduct = list.get(position);

            productName.setText(getString(R.string.product_name) + " : " + searchableProduct.productName);
            productForm.setText(getString(R.string.product_form) + " : " + searchableProduct.productForm);
            productDose.setText(getString(R.string.product_cntr) + " : " + searchableProduct.productDose);
            productPrice.setText(getString(R.string.product_price) + " : " + searchableProduct.productPrice + " " + getString(R.string.dinar));
            double distance = getProductDistance(searchableProduct.locationx, searchableProduct.locationy);

            if(distance >= 1.00)
                productDistance.setText(getString(R.string.distance) + " : " + MyTools.getFormat(distance)
                        + " " + getString(R.string.kilo_meter));
            else
                productDistance.setText(getString(R.string.distance) + " : " + MyTools.getFormat(distance * 1000)
                        + " " + getString(R.string.meter));

            boolean result = MyTools.isPharmasistOpen(searchableProduct.moorningStart,
                    searchableProduct.moorningEnd,
                    searchableProduct.eveningStart,
                    searchableProduct.eveningEnd);

            String status = result == true ? getString(R.string.open) : getString(R.string.close);
            pharmasistStatus.setText(getString(R.string.pharmasist_status) + " : " + status);

            if (result) pharmasistStatus.setTextColor(Color.GREEN);
            else pharmasistStatus.setTextColor(Color.RED);

            pharmasistFullName.setText(getString(R.string.full_name) + " : " + searchableProduct.fname + " " + searchableProduct.lname);
            pharmasistPhone.setText(getString(R.string.phone_number) + " : " + searchableProduct.phone);

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spImpl.save(searchableProduct.productName);
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("productId", searchableProduct.productId);
                    intent.putExtra("productName", searchableProduct.productName);
                    intent.putExtra("productForm", searchableProduct.productForm);
                    intent.putExtra("productDose", searchableProduct.productDose);
                    intent.putExtra("productPrice", searchableProduct.productPrice);
                    String fullName = searchableProduct.lname + " " + searchableProduct.fname;
                    intent.putExtra("pharmasistFullName", fullName);
                    intent.putExtra("pharmasistPhone", searchableProduct.phone);
                    intent.putExtra("pharmasistDistance", distance);
                    intent.putExtra("pharmasistLocx", searchableProduct.locationx);
                    intent.putExtra("pharmasistLocy", searchableProduct.locationy);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    /*
        Return the distance between two point
     */

    public double getProductDistance(double lon1, double lat1) {
        double dist = 0.0;
        if (MyTools.isLocationEnabled(MainActivity.this)) {

            /* This part work to get the current position(longitude and latitude) */

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            /* This part complete the first part of getting the current position */

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return 0.0;
                }
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

            if (location != null) {

                double lon2 = location.getLongitude();
                double lat2 = location.getLatitude();

                dist = distance(lat1, lon1, lat2, lon2);

            } else {
                toastTask.showMsg(getResources().getString(R.string.location_err));
            }

        } else {
            toastTask.showMsg(getResources().getString(R.string.gps_disable));
        }
        return dist;
    }

    /*
        Calculate the distance between two point
    */
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344; // to kilométer
            return (dist);
        }
    }

    /*
        Open the activity for adding new pharmasist
     */

    public void register(View view) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(getResources().getString(R.string.loading));
        builder1.setMessage(getResources().getString(R.string.register_infos));
        builder1.setPositiveButton(getResources().getString(R.string.i_inderstand), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getBaseContext(), Register.class);
                startActivity(intent);
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

    public void login(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
