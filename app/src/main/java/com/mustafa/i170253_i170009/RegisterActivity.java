package com.mustafa.i170253_i170009;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaeger.library.StatusBarUtil;

public class RegisterActivity extends AppCompatActivity {
    EditText inputEmail,inputPassword,inputConfirmPassword;
    Button buttonRegister;
    TextView loginButton;
    private FirebaseAuth mAuth;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setTranslucent(RegisterActivity.this, 75);
        inputEmail = findViewById(R.id.value_email);
        inputPassword = findViewById(R.id.value_password);
        inputConfirmPassword = findViewById(R.id.value_confirm_password);
        buttonRegister = findViewById(R.id.button_register);
        loginButton = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();



        buttonRegister.setOnClickListener( v -> {
            mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString(),inputPassword.getText().toString())
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    createProfile(mAuth.getCurrentUser().getUid(),inputEmail.getText().toString());
                    Intent intent=new Intent(RegisterActivity.this,CreateProfileActivity.class);
                    intent.putExtra("id",mAuth.getCurrentUser().getUid());
                    intent.putExtra("email",inputEmail.getText().toString());
                    startActivity(intent);
                    finish();

                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(
                        RegisterActivity.this,
                        "Failed...!",
                        Toast.LENGTH_SHORT
                ).show();
            });
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinbut=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(signinbut);
                finish();
            }
        });

    }

    public void createProfile(String id, String email){
        Intent intent = new Intent(RegisterActivity.this,CreateProfileActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("email",email);
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
//            Toast.makeText(
//                    RegisterActivity.this,
//                    currentUser.getUid()+"",
//                    Toast.LENGTH_SHORT
//            ).show();
        }
    }
}
