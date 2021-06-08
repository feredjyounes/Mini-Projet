package com.app.pharmasist;

import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.app.tools.MyTools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView txtProductName, txtProductDose, txtProductForm, txtProductPrice;
    TextView txtUserFullName, txtUserPhone, txtUserDistance;
    double x, y;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtProductName = findViewById(R.id.detailProductName);
        txtProductDose = findViewById(R.id.detailProductDose);
        txtProductForm = findViewById(R.id.detailProductForm);
        txtProductPrice = findViewById(R.id.detailProductPrice);

        txtUserFullName = findViewById(R.id.detailUserFullName);
        txtUserPhone = findViewById(R.id.detailUserPhone);
        txtUserDistance = findViewById(R.id.detailUserDistance);

        Intent intent = getIntent();

        intent.getIntExtra("productId", 0);

        txtProductName.setText(getString(R.string.product_name) + " : " + intent.getStringExtra("productName"));
        txtProductForm.setText(getString(R.string.product_form) + " : " + intent.getStringExtra("productForm"));
        txtProductDose.setText(getString(R.string.product_cntr) + " : " + intent.getStringExtra("productDose"));
        txtProductPrice.setText(getString(R.string.product_price) + " : " + intent.getDoubleExtra("productPrice", 0)+" "+getString(R.string.dinar));

        txtUserFullName.setText(getString(R.string.full_name) + " : " + intent.getStringExtra("pharmasistFullName"));
        txtUserPhone.setText(getString(R.string.phone_number) + " : " + intent.getStringExtra("pharmasistPhone"));
        phoneNumber = intent.getStringExtra("pharmasistPhone");

        double distance = intent.getDoubleExtra("pharmasistDistance", 0);
        if(distance < 1)
            txtUserDistance.setText(getString(R.string.distance) + " : " + MyTools.getFormat(distance*1000) +" "+getString(R.string.meter));
        else
            txtUserDistance.setText(getString(R.string.distance) + " : " + MyTools.getFormat(distance) +" "+getString(R.string.kilo_meter));

        x = intent.getDoubleExtra("pharmasistLocx", 0);
        y = intent.getDoubleExtra("pharmasistLocy", 0);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Marker of Pharmasist
        LatLng pharmasistPosition = new LatLng(y, x);
        MarkerOptions pharmasistMarker = new MarkerOptions();
        pharmasistMarker.position(pharmasistPosition);
        pharmasistMarker.title(getString(R.string.pharmasist_position));

        Marker pMarker = mMap.addMarker(pharmasistMarker);
        pMarker.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pharmasistPosition, 7));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void call_pharmasist(View view){
            String number = "tel:"+phoneNumber;
            Log.d("number => ", number);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(number));
            startActivity(intent);
    }

}
