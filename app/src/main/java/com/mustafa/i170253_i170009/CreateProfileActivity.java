package com.mustafa.i170253_i170009;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateProfileActivity extends AppCompatActivity {
    EditText firstNameValue,lastNameValue,dobValue,phnoValue,bioValue;
    Button saveButton;
    ImageView profile_picture;
    Uri selectedImage=null;
    RadioGroup radioGroup;
    RadioButton radioButton;

    FirebaseDatabase database;
    DatabaseReference reference;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getWindow().setStatusBarColor(ContextCompat.getColor(CreateProfileActivity.this,R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        Intent data=getIntent();
        String id=data.getStringExtra("id");
        String email=data.getStringExtra("email");
        profile_picture=findViewById(R.id.profile_image);
        firstNameValue = findViewById(R.id.editTextPersonFirstName);
        lastNameValue = findViewById(R.id.editTextPersonLastName);
        dobValue = findViewById(R.id.editTextDOB);
        phnoValue = findViewById(R.id.editTextPhoneNumber);
        bioValue = findViewById(R.id.editTextBio);
        saveButton = findViewById(R.id.save_button);
        radioGroup = findViewById(R.id.radioGroup);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("ProfilesTable");



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                if(selectedImage!=null){
                    StorageReference st= FirebaseStorage.getInstance().getReference();
                    st=st.child("images/"+id+".jpg");
                    st.putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> task= taskSnapshot.getStorage().getDownloadUrl();

                                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String dp=uri.toString();
                                            String name = firstNameValue.getText().toString() +" "+ lastNameValue.getText().toString();
                                            reference.push().setValue(
                                                    new Profile(id
                                                            ,email
                                                            ,name
                                                            ,dobValue.getText().toString()
                                                            ,radioButton.getText().toString()
                                                            ,phnoValue.getText().toString()
                                                            ,bioValue.getText().toString()
                                                            ,dp));

                                            Intent intent=new Intent(CreateProfileActivity.this,ContactListActivity.class);
                                            intent.putExtra("id",id);
                                            startActivity(intent);
                                            finish();


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CreateProfileActivity.this,"Image uploading failed",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateProfileActivity.this,"Image uploading failed",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(CreateProfileActivity.this,"Image not loaded",Toast.LENGTH_LONG).show();
                }
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,200);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200 && resultCode==RESULT_OK){
            selectedImage=data.getData();
            profile_picture.setImageURI(selectedImage);
        }
    }
}