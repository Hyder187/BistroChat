package com.mustafa.i170253_i170009;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sendMessageActivity extends AppCompatActivity {
    EditText text,sender,receiver;
    Button send;
    FirebaseDatabase database;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        text=findViewById(R.id.text);
        receiver=findViewById(R.id.rid);
        send=findViewById(R.id.sendButton);
        mAuth = FirebaseAuth.getInstance();
        Intent data=getIntent();
        String id=data.getStringExtra("id");
        String name=data.getStringExtra("profileName");
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Chats");

        send.setOnClickListener(view ->
                reference.push().setValue(new Message(  mAuth.getCurrentUser().getUid(),
                                                                                id,
                                                                                text.getText().toString(),"",false
                                                )));





    }
}