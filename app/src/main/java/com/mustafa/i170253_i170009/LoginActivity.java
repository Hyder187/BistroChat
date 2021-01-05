package com.mustafa.i170253_i170009;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.jaeger.library.StatusBarUtil;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button signin;
    FirebaseAuth firebaseAuth;
    TextView signUpButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTranslucent(LoginActivity.this, 75);
        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.email_input);
        password=findViewById(R.id.password_input);
        signin=findViewById(R.id.button_login);
        signUpButton=findViewById(R.id.signUpButton);
        authStateListener=new FirebaseAuth.AuthStateListener(){
            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();



            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseUser!=null){
                    //Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
//                    Intent i=new Intent(LoginActivity.this,ContactListActivity.class);
//                    startActivity(i);
                }
                else{
//                Toast.makeText(Login.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email1=email.getText().toString().trim();
                final String password1=password.getText().toString().trim();
                if(email1.isEmpty()){
                    email.setError("Provide email id");
                    email.requestFocus();
                }

                else if(password1.isEmpty()){
                    password.setError("Provide password");
                    password.requestFocus();
                }

                else if(!email1.isEmpty() && !password1.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                e.printStackTrace();
                                System.out.println(email1 + "---------" + password1);
                                Toast.makeText(LoginActivity.this,"Login error",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent toHome=new Intent(LoginActivity.this,ContactListActivity.class);
                                startActivity(toHome);
                                finish();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
                }

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinbut=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(signinbut);
                finish();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}