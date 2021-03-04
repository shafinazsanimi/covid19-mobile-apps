package com.example.covid19apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {
    ImageView image, userImage, currentUserImage;
    TextView title, date, userName, description;
    EditText editTextComment;
    Button addCommentButton;
    String PostKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String COMMENT_KEY = "Comments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        RvComment = findViewById(R.id.rv_comment);
        image = findViewById(R.id.post_detail_image);
        userImage = findViewById(R.id.post_detail_user_image);
        currentUserImage = findViewById(R.id.currentUser_image);
        title = findViewById(R.id.post_detail_title);
        date = findViewById(R.id.post_detail_date);
        userName = findViewById(R.id.post_detail_user_name);
        description = findViewById(R.id.post_detail_description);
        editTextComment = findViewById(R.id.post_comment);
        addCommentButton = findViewById(R.id.add_comment_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentButton.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                String commentContent = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uimage = firebaseUser.getPhotoUrl().toString();
                String uname = firebaseUser.getDisplayName();
                final Comment comment = new Comment(commentContent, uid, uimage, uname);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PostDetailActivity.this, "Comment added", Toast.LENGTH_LONG).show();
                        editTextComment.setText("");
                        addCommentButton.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetailActivity.this, "Fail to add comment: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        String postImage = getIntent().getExtras().getString("postImage") ;
        Glide.with(this).load(postImage).into(image);

        String postTitle = getIntent().getExtras().getString("title");
        title.setText(postTitle);

        String postUserName = getIntent().getExtras().getString("postUserName");
        userName.setText(postUserName);

        String userPostImage = getIntent().getExtras().getString("postUserPhoto");
        Glide.with(this).load(userPostImage).into(userImage);

        String postDescription = getIntent().getExtras().getString("description");
        description.setText(postDescription);

        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(currentUserImage);
        PostKey = getIntent().getExtras().getString("postKey");

        String postDate = timestampToString(getIntent().getExtras().getLong("postDate"));
        date.setText(postDate);

        iniRvComment();
    }

    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}
