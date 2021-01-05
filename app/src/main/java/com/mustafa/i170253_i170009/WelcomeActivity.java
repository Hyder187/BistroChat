package com.mustafa.i170253_i170009;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;

public class WelcomeActivity extends AppCompatActivity {
    Button buttonRegister;
    Button buttonSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        StatusBarUtil.setTranslucent(WelcomeActivity.this, 75);

        buttonRegister = findViewById(R.id.button_register);
        buttonSignin=findViewById(R.id.button_signin);

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}