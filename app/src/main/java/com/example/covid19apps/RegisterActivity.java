package com.example.covid19apps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    ImageView userPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    private EditText name, email, password, confPassword;
    private ProgressBar register_progress;
    private Button register;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userPhoto = findViewById(R.id.regUserPhoto);
        name = findViewById(R.id.regName);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        confPassword = findViewById(R.id.regConfPassword);
        register_progress = findViewById(R.id.register_progressBar);
        register = findViewById(R.id.login_button);
        register_progress.setVisibility(View.INVISIBLE);

        mFirebaseAuth = FirebaseAuth.getInstance();

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setVisibility(View.INVISIBLE);
                register_progress.setVisibility(View.VISIBLE);
                final String Name = name.getText().toString();
                final String Email = email.getText().toString();
                final String Password = password.getText().toString();
                final String ConfPassword = confPassword.getText().toString();

                if (pickedImgUri == null) {
                    register.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this,"Please choose your profile picture", Toast.LENGTH_SHORT).show();
                }
                else if (Name.isEmpty()) {
                    register.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.INVISIBLE);
                    name.setError("Please enter your name");
                    name.requestFocus();
                }
                else if (Email.isEmpty()) {
                    register.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.INVISIBLE);
                    email.setError("Please enter your email");
                    email.requestFocus();
                }
                else if (Password.isEmpty()) {
                    register.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.INVISIBLE);
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if (ConfPassword.isEmpty()) {
                    register.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.INVISIBLE);
                    confPassword.setError("Please enter your confirm password");
                    confPassword.requestFocus();
                }
                else if (!Password.equals(ConfPassword)) {
                    register.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.INVISIBLE);
                    confPassword.setError("Your password doesn't match!");
                    confPassword.requestFocus();
                }
                else {
                    CreateUserAccount(Name, Email, Password);
                }
            }
        });
    }

    private void CreateUserAccount(final String name, String email, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                    updateUserData(name, pickedImgUri, mFirebaseAuth.getCurrentUser());
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Sign up failed!", Toast.LENGTH_LONG).show();
                    register.setVisibility(View.VISIBLE);
                    register_progress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUserData(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener((new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name).setPhotoUri(uri).build();
                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this,"Sign up completed!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(RegisterActivity.this, ThirdActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }
                }));
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            userPhoto.setImageURI(pickedImgUri);
        }
    }
}
