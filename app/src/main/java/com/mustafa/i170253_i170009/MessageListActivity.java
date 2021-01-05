package com.mustafa.i170253_i170009;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MessageListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference reference2;
    MessageListJavaAdapter adapter;
    ImageButton send,selectImage,clearImage;
    View imagePreviewBackground;
    EditText messageContent;
    Uri selectedImage=null;
    ImageButton backButton;
    TextView appbar_heading;
    ImageView receiverImage,imagePreview;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        getWindow().setStatusBarColor(ContextCompat.getColor(MessageListActivity.this,R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        Intent data=getIntent();
        String id=data.getStringExtra("id");
        String receiverName=data.getStringExtra("profileName");
        String receiverImageUri = data.getStringExtra("image");
        // Populate dummy messages in List, you can implement your code here
        ArrayList<Message> messagesList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        String senderId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        database=FirebaseDatabase.getInstance();
        send=findViewById(R.id.send_button);
        selectImage=findViewById(R.id.select_image);
        messageContent=findViewById(R.id.message_content);
        backButton = findViewById(R.id.backButton);
        imagePreview = findViewById(R.id.image_preview);
        clearImage = findViewById(R.id.clear_image);
        imagePreviewBackground = findViewById(R.id.image_preview_bg);
        appbar_heading = findViewById(R.id.appbar_heading);
        receiverImage = findViewById(R.id.receiver_profile_image);

        reference=database.getReference("Chats");

        reference2=database.getReference("Chats");

        appbar_heading.setText(receiverName);
        Picasso.get().load(receiverImageUri).into(receiverImage);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message x=dataSnapshot.getValue(Message.class);

                if((x.getReceiverId().equals(id) && x.getSenderId().equals(senderId)) || (x.getReceiverId().equals(senderId) && x.getSenderId().equals(id))){
                    messagesList.add(x);

                }
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message x=dataSnapshot.getValue(Message.class);

                if((x.getReceiverId().equals(id) && x.getSenderId().equals(senderId)) || (x.getReceiverId().equals(senderId) && x.getSenderId().equals(id))){
                    messagesList.add(x);
                }
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapter = new MessageListJavaAdapter(this, messagesList,id,senderId);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,200);
            }
        });

        backButton.setOnClickListener(v -> {
            finish();
        });

        send.setOnClickListener(view -> {
            if(selectedImage!=null){
                StorageReference st= FirebaseStorage.getInstance().getReference();
                st=st.child("messages/" + selectedImage.getLastPathSegment() + ".jpg");
                st.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task= taskSnapshot.getStorage().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String dp=uri.toString();
                                        reference2.push().setValue(new Message(senderId,id,messageContent.getText().toString(),dp,true));
                                        messageContent.setText(null);
                                        recyclerView.smoothScrollToPosition(adapter.getItemCount());
                                        clearImage();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MessageListActivity.this,"Image uploading failed",Toast.LENGTH_LONG).show();
                                        clearImage();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MessageListActivity.this,"Image uploading failed",Toast.LENGTH_LONG).show();
                        clearImage();
                    }
                });
            }
            else{
                reference2.push().setValue(new Message(senderId,id,messageContent.getText().toString(),"",false));
                messageContent.setText(null);
                clearImage();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }

        });

        clearImage.setOnClickListener(v -> {
            clearImage();
        });

    }

    public void clearImage(){
        selectedImage= null;
        imagePreview.setImageURI(null);
        imagePreview.setVisibility(View.GONE);
        clearImage.setVisibility(View.GONE);
        imagePreviewBackground.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200 && resultCode==RESULT_OK){
            selectedImage=data.getData();
            imagePreview.setImageURI(selectedImage);
            imagePreview.setVisibility(View.VISIBLE);
            clearImage.setVisibility(View.VISIBLE);
            imagePreviewBackground.setVisibility(View.VISIBLE);

        }
    }
}