package com.example.covid19apps;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ThirdActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PReqCode = 1;
    private static final int REQUESCODE = 1;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser currentUser;
    Dialog popAddPost;
    ImageView popupUserPhoto, popupImage, popupAdd;
    TextView popupTitle, popupDescription;
    ProgressBar popupProgressBar;
    private DrawerLayout drawer;
    private Uri pickedImgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        iniPopup();
        setupPopupImageClick();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();
           }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_staff);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff, new HomeFragment()).commit();
    }

    private void setupPopupImageClick() {
        popupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ThirdActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(ThirdActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(ThirdActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else
            openGallery();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            popupImage.setImageURI(pickedImgUri);
        }
    }

    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.activity_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        popupUserPhoto = popAddPost.findViewById(R.id.popup_userPhoto);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupImage = popAddPost.findViewById(R.id.popup_image);
        popupAdd = popAddPost.findViewById(R.id.popup_add);
        popupProgressBar = popAddPost.findViewById(R.id.popup_progress_bar);

        Glide.with(ThirdActivity.this).load(currentUser.getPhotoUrl()).into(popupUserPhoto);

        popupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAdd.setVisibility(View.INVISIBLE);
                popupProgressBar.setVisibility(View.VISIBLE);

                if (!popupTitle.getText().toString().isEmpty() && !popupDescription.getText().toString().isEmpty() && pickedImgUri != null) {

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("post_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    Post post = new Post(popupTitle.getText().toString(),
                                                         popupDescription.getText().toString(),
                                                         imageDownloadLink,
                                                         currentUser.getUid(),
                                                         currentUser.getDisplayName(),
                                                         currentUser.getEmail(),
                                                         currentUser.getPhotoUrl().toString());
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ThirdActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    popupProgressBar.setVisibility(View.INVISIBLE);
                                    popupAdd.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(ThirdActivity.this, "Please verify all input fields and choose an image", Toast.LENGTH_LONG).show();
                    popupAdd.setVisibility(View.VISIBLE);
                    popupProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        String key = myRef.getKey();
        post.setPostKey(key);

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ThirdActivity.this, "Posts added successfully", Toast.LENGTH_LONG).show();
                popupTitle.setText("");
                popupDescription.setText("");
                popupImage.setImageDrawable(null);
                popupProgressBar.setVisibility(View.INVISIBLE);
                popupAdd.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FloatingActionButton fab = findViewById(R.id.fab);

        switch (item.getItemId()) {
            case R.id.nav_profile:
                getSupportActionBar().setTitle("Profile");
                fab.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_notification:
                getSupportActionBar().setTitle("Notifications");
                fab.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,
                        new NotificationFragment()).commit();
                break;
            case R.id.nav_message:
                getSupportActionBar().setTitle("Messages");
                fab.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,
                        new MessageFragment()).commit();
                break;
            case R.id.nav_setting:
                getSupportActionBar().setTitle("Settings");
                fab.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff,
                        new SettingFragment()).commit();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intToLogout = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intToLogout);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            getSupportActionBar().setTitle("COVID-19Apps");
            FloatingActionButton fab = findViewById(R.id.fab);

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_track:
                    selectedFragment = new TrackerFragment();
                    fab.setVisibility(View.INVISIBLE);
                    break;
                case R.id.nav_location:
                    selectedFragment = new LocationFragment();
                    fab.setVisibility(View.INVISIBLE);
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_staff, selectedFragment).commit();
            return true;
        }
    };

    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView userPhoto = headerView.findViewById(R.id.navUserPhoto);
        TextView userName = headerView.findViewById(R.id.navUserName);
        TextView userEmail = headerView.findViewById(R.id.navUserEmail);

        userName.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(userPhoto);
    }
}
