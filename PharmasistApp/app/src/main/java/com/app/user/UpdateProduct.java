package com.app.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.android.volley.AuthFailureError;
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
import com.app.tools.ToastTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class UpdateProduct extends AppCompatActivity {

    EditText txtProductName, txtProductDose, txtProductQuantity, txtProductPrice, txtProductExpiration;
    Spinner spinnerForm;
    Button btnUpdate;
    int productId;
    ToastTask toastTask = new ToastTask(UpdateProduct.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        btnUpdate = findViewById(R.id.btnUpdate1);

        defineFields();

        Product.setErrFields(UpdateProduct.this, btnUpdate, txtProductName, txtProductDose,
                txtProductQuantity, txtProductPrice, txtProductExpiration);

        Intent intent = getIntent();

        productId = intent.getIntExtra("productId", 0);
        txtProductName.setText(intent.getStringExtra("productName"));
        spinnerForm.setSelection(getFormTypePosition(intent.getStringExtra("productForm")));
        txtProductDose.setText(intent.getStringExtra("productDose"));
        txtProductQuantity.setText(intent.getIntExtra("productQuantity", 0)+"");
        txtProductPrice.setText(intent.getDoubleExtra("productPrice", 0)+"");
        txtProductExpiration.setText(intent.getStringExtra("productExpiration"));

        MyTools.viewDatePicker(UpdateProduct.this, txtProductExpiration);

    }

    public void defineFields(){
        txtProductName = findViewById(R.id.txtProductName1);
        spinnerForm = findViewById(R.id.spinnerForm1);
        txtProductDose = findViewById(R.id.txtProductDose1);
        txtProductQuantity = findViewById(R.id.txtProductQantity1);
        txtProductPrice = findViewById(R.id.txtProductPrice1);
        txtProductExpiration = findViewById(R.id.txtProductExpiration2);
    }

    public int getFormTypePosition(String item){
        String items[] = getResources().getStringArray(R.array.product_form2);
        int position = -1;
        for(int i=0; i<items.length; i++){
            if(items[i].equals(item)){
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }

    public void updateProduct(View view) {

        if (
            /* checking the empty fields */
                Product.checkBlankFields(getString(R.string.blank_field), txtProductName, txtProductDose,
                        txtProductQuantity, txtProductPrice, txtProductExpiration)
             && Product.checkFieldsSyntaxe(txtProductName, txtProductDose, txtProductQuantity,
                        txtProductPrice, txtProductExpiration)
        ) {
            String productName = txtProductName.getText().toString();
            String productForm = spinnerForm.getSelectedItem().toString();
            String productDose = txtProductDose.getText().toString();
            String productQuantity = txtProductQuantity.getText().toString();
            String productPrice = txtProductPrice.getText().toString();
            String productExpiration = txtProductExpiration.getText().toString();

            int qnte = Integer.parseInt(productQuantity);

            if(qnte <= 0){
                toastTask.showMsg(getString(R.string.null_number));
            }else{
                    updateProductTask(productId, productName, productForm, productDose,
                            productQuantity, productPrice, productExpiration);
            }
        }

    }

    private void updateProductTask(int productId, String productName, String productForm, String productDose,
                                   String productQuantity, String productPrice, String productExpiration) {

        ProgressTask progress = new ProgressTask(UpdateProduct.this);
        progress.showProgress();

        AlertDialogTask dialog = new AlertDialogTask(UpdateProduct.this);

        StringRequest jor = new StringRequest(Request.Method.PUT, new AsyncTasks().UPDATE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    try {
                        if(new JSONObject(response).getInt("response") == 0)
                            dialog.showAlertDialog(getString(R.string.server_response),
                                    getString(R.string.product_updated),
                                    getString(R.string.i_inderstand));
                        else
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
                toastTask.showMsg(UpdateProduct.this.getResources().getString(R.string.err_getting_info));
                toastTask.showMsg(UpdateProduct.this.getResources().getString(R.string.check_your_internet));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("productId", productId+"");
                params.put("productName", productName);
                params.put("productForm", productForm);
                params.put("productCntr", productDose);
                params.put("productQuantity", productQuantity);
                params.put("productPrice", productPrice);
                params.put("productExpiration", productExpiration);
                params.put("user", User.getId()+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UpdateProduct.this);
        requestQueue.add(jor);

    }
}
