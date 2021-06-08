package com.app.user;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.app.pharmasist.Login;
import com.app.pharmasist.R;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void disconnect(View view) {
        User.destructUser();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void exit(View view) {

    }

    public void about_us(View view) {
        Intent intent = new Intent(this, Apropos.class);
        startActivity(intent);
    }
}
