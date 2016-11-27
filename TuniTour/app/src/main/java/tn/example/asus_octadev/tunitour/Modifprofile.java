package tn.example.asus_octadev.tunitour;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import tn.example.asus_octadev.tunitour.Model.User;

public class Modifprofile extends AppCompatActivity {
    FirebaseAuth mAuth;
    Uri selectedImage,url;
    DatabaseReference mDatabase ;
    EditText name,tel;
    User post1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Edit Profil");
        mAuth = FirebaseAuth.getInstance();
        inti();
        name= (EditText) findViewById(R.id.name);
        tel= (EditText) findViewById(R.id.tel);


        findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference firestorage;
                firestorage= FirebaseStorage.getInstance().getReference();
                StorageReference filepath=firestorage.child("users").child(selectedImage.getLastPathSegment());
                filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        url=taskSnapshot.getDownloadUrl();
                        post1.setFull_name(name.getText().toString());
                        post1.setMobile(tel.getText().toString());
                        post1.setProfile_image(url.toString());

                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).setValue(post1);


                    }
                });
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(menuItem);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK
                && null != data) {

            selectedImage = data.getData();
            Bitmap image = null;
            try {
                image = (Bitmap) MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((CircleImageView) findViewById(R.id.profile_image)).setImageBitmap(image);

        }
    }

    public void inti() {
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).
                addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                // Get user value
                                post1 = dataSnapshot1.getValue(User.class);
                                name.setText(post1.getFull_name());
                                tel.setText(post1.getMobile());


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
    }
}

