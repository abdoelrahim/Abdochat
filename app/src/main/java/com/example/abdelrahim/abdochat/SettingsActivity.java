package com.example.abdelrahim.abdochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView mcircleImageView;
    private TextView mName;
    private TextView mStatus;
    private Button Setting_Change_Image;
    private Button Setting_Change_Status;
    private static final int GALARY_PICK = 1;
    private StorageReference mImagestorage;
    private ProgressDialog mprogressDialog;


    private DatabaseReference mUserDatabase;
    private FirebaseUser mcurrentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mcircleImageView = (CircleImageView) findViewById(R.id.setting_profile_image);
        mName = (TextView) findViewById(R.id.Setting_Name);
        mStatus = (TextView) findViewById(R.id.setting_status);
        Setting_Change_Status = (Button) findViewById(R.id.Setting_btn_status);
        Setting_Change_Image = (Button) findViewById(R.id.Setting_btn_changeImage);
        mImagestorage = FirebaseStorage.getInstance().getReference();


        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mcurrentuser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("Status").getValue().toString();
                final String image = dataSnapshot.child("Image").getValue().toString();


                // Check this line and it's use
                // String thumb_image = dataSnapshot.child("thumb image").getValue().toString();


                mName.setText(name);
                mStatus.setText(status);

                if (!image.equals("default")) {
                    // Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.blank).into(mcircleImageView);

        Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).
                placeholder(R.drawable.blank).into(mcircleImageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.blank).into(mcircleImageView);

            }
        });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        Setting_Change_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Current_Status = mStatus.getText().toString();
                Intent Status_Intent = new Intent(SettingsActivity.this, StatusActivity.class);
                Status_Intent.putExtra("Current_Status", Current_Status);
                startActivity(Status_Intent);


            }
        });

        Setting_Change_Image.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                Intent galaryIntent = new Intent();
                galaryIntent.setType("image/*");
                galaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galaryIntent, "SELECT IMAGE"), GALARY_PICK);


               /* CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this); */


            }


        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == GALARY_PICK && resultCode == RESULT_OK) {

            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri).setAspectRatio(1, 1).start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mprogressDialog = new ProgressDialog(SettingsActivity.this);
                mprogressDialog.setTitle("Uploading Image");
                mprogressDialog.setMessage("Please wait");
                mprogressDialog.setCanceledOnTouchOutside(false);
                mprogressDialog.show();

                Uri resultUri = result.getUri();
                final File thumb_filepath = new File(resultUri.getPath());
                String Current_uid = mcurrentuser.getUid();


                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(75).
                            compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thumb_byte = baos.toByteArray();






                StorageReference filePath = mImagestorage.child("profile_Images").child(mcurrentuser + ".jpg");
                final StorageReference thump_filePath = mImagestorage.child("profile_Images").child("thumb").child(mcurrentuser + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thump_filePath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_download_url = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()){

                                    Map Update_HashMap = new HashMap();
                                        Update_HashMap.put("Image",download_url);
                                        Update_HashMap.put("thumb_image" , thumb_download_url);
                                        mUserDatabase.updateChildren(Update_HashMap);



                                    } // if thumb_task

                                }


                            }); // uploadTask


                            mUserDatabase.child("Image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mprogressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Success", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SettingsActivity.this, "Error in uploading", Toast.LENGTH_SHORT).show();
                            mprogressDialog.dismiss();

                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
