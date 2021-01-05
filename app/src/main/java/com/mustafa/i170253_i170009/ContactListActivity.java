package com.mustafa.i170253_i170009;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView rv;
    List<Profile> contactList;
    List<Profile> mutualContacts;
    FirebaseDatabase database;
    DatabaseReference reference;
    MyRvAdapter adapter;
    ArrayList<String> arrayList;
    private FirebaseAuth mAuth;
    EditText searchView;
    CharSequence search ="";
    ImageView profileImage,headerProfileImage;
    TextView headerName,headerEmail;
    String senderId;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        getWindow().setStatusBarColor(ContextCompat.getColor(ContactListActivity.this,R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        View loadingBackground  = findViewById(R.id.loading_background);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        rv=findViewById(R.id.rv);
        arrayList=new ArrayList<>();
        contactList=new ArrayList<>();
        mutualContacts=new ArrayList<>();
        searchView = findViewById(R.id.search_bar);
        profileImage = findViewById(R.id.profile_image);
        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        headerProfileImage = header.findViewById(R.id.header_profile_image);
        headerName = header.findViewById(R.id.header_name);
        headerEmail = header.findViewById(R.id.header_email);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        senderId =  mAuth.getCurrentUser().getUid();



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // requesting to the user for permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);

        } else {
            //if app already has permission this block will execute.
            readContacts();
        }
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("ProfilesTable");

            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    progressBar.setVisibility(View.VISIBLE);
                    loadingBackground.setVisibility(View.VISIBLE);

                    Profile x=dataSnapshot.getValue(Profile.class);
                    if(x.getId().equals(senderId)){
                        Picasso.get().load(x.getDp()).fit().centerCrop().into(profileImage);
                        Picasso.get().load(x.getDp()).fit().centerCrop().into(headerProfileImage);
                        headerName.setText(x.getName());
                        headerEmail.setText(x.getEmail());
                    }
                    if(arrayList.contains(x.getPhoneNo()) && !x.getId().equals(mAuth.getCurrentUser().getUid())){
                        mutualContacts.add(x);
                    }
                    adapter.notifyDataSetChanged();

                    progressBar.setVisibility(View.GONE);
                    loadingBackground.setVisibility(View.GONE);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Profile x=dataSnapshot.getValue(Profile.class);
                    if(x.getId().equals(senderId)){
                        Picasso.get().load(x.getDp()).fit().centerCrop().into(profileImage);
                        Picasso.get().load(x.getDp()).fit().centerCrop().into(headerProfileImage);
                        headerName.setText(x.getName());
                        headerEmail.setText(x.getEmail());
                    }
                    if(arrayList.contains(x.getPhoneNo()) && !x.getId().equals(mAuth.getCurrentUser().getUid())){
                        mutualContacts.add(x);
                    }

                    adapter.notifyDataSetChanged();
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


        rv=findViewById(R.id.rv);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(ContactListActivity.this);
        rv.setLayoutManager(lm);
        adapter=new MyRvAdapter(mutualContacts,ContactListActivity.this);
        rv.setAdapter(adapter);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setSupportActionBar(toolbar);
        try
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        catch (NullPointerException ignored){}
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ContactListActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(ContactListActivity.this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_button) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        readContacts();
    }
    // function to read contacts using content resolver
    private void readContacts() {
        ContentResolver contentResolver=getContentResolver();
        Cursor phones=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        String number;
            while (phones.moveToNext())
            {
                number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("+92","0");
                arrayList.add(number);
//                Log.d("Hello",phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }

            phones.close();

    }

}