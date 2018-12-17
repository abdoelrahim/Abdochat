package com.example.abdelrahim.abdochat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileimageView;
    private TextView profileName, profileStatuse, profileFriendsCount;
    private Button mFriendRequestbtn, mDeclinebtn;
    private DatabaseReference mUsersDatabase;
    ProgressDialog mprogressDialog;
    private String mCurrent_state;
    private DatabaseReference mFriendrequestDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("user_id"); // user_id which clicked from AllUsersActivity
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendrequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");

        profileimageView = (ImageView) findViewById(R.id.profile_Imageview);
        profileName = (TextView) findViewById(R.id.profile_tv_displayname);
        profileStatuse = (TextView) findViewById(R.id.profile_displaystatuse);
        profileFriendsCount = (TextView) findViewById(R.id.profile_displayFriends);
        mFriendRequestbtn = (Button) findViewById(R.id.prfile_sendFriend_request);
        mDeclinebtn = (Button) findViewById(R.id.prfile_DeclineFriend_request);

        mCurrent_state = "not_friends";

        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setTitle("Loading user data");
        mprogressDialog.setMessage("Please wait");
        mprogressDialog.setCanceledOnTouchOutside(false);
        mprogressDialog.show();


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("Status").getValue().toString();
                String Image = dataSnapshot.child("Image").getValue().toString();

                profileName.setText(name);
                profileStatuse.setText(status);
                Picasso.with(ProfileActivity.this).load(Image).placeholder(R.drawable.blank).into(profileimageView);

                //-------------Friends list / Request feature ---------------------------//

                mFriendrequestDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)) {

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")) {


                                mCurrent_state = "req_received";
                                mFriendRequestbtn.setText("Accept friend Request");
                                mDeclinebtn.setVisibility(View.VISIBLE);
                                mDeclinebtn.setEnabled(true);


                            } else if (req_type.equals("sent")) {
                                mCurrent_state = "req_sent";
                                mFriendRequestbtn.setText("Cancel Friend Request");

                                mDeclinebtn.setVisibility(View.INVISIBLE);
                                mDeclinebtn.setEnabled(false);


                            }

                            mprogressDialog.dismiss();

                        }

                        // start from here

                        else {
                            mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(user_id)) {

                                        mCurrent_state = "friends";
                                        mFriendRequestbtn.setText("UnFriend this Person");
                                        mDeclinebtn.setVisibility(View.INVISIBLE);
                                        mDeclinebtn.setEnabled(false);
                                    }
                                    mprogressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mprogressDialog.dismiss();

                                }
                            });
                        }


                        mprogressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFriendRequestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFriendRequestbtn.setEnabled(false);

                //-----------Not Friends state --------------------------//

               // if not friends
                if (mCurrent_state.equals("not_friends")) {

                    mFriendrequestDatabase.child(mCurrentUser.getUid()).child(user_id).
                            child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                // create another field with the user_id (which we visited ) and request_type will be received
                                mFriendrequestDatabase.child(user_id).child(mCurrentUser.getUid()).
                                        child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        HashMap<String, String> notification = new HashMap<>();
                                        notification.put("from", mCurrentUser.getUid());
                                        notification.put("type", "request");
                                        mNotificationDatabase.child(user_id).push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


                                                mCurrent_state = "req_sent";
                                                mFriendRequestbtn.setText("Cancel friend Request");

                                                mDeclinebtn.setVisibility(View.INVISIBLE);
                                                mDeclinebtn.setEnabled(false);


                                            }
                                        });


                                        //Toast.makeText(ProfileActivity.this, "Friend request sent", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed sending request", Toast.LENGTH_SHORT).show();
                            }
                            mFriendRequestbtn.setEnabled(true);


                        }  // on complete
                    });  // addoncomplete
                }
                // -----------Cancel Request State ----------------------------------

                if (mCurrent_state.equals("req_sent")) {

                    mFriendrequestDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendrequestDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mFriendRequestbtn.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mFriendRequestbtn.setText("Send friend Request");

                                    mDeclinebtn.setVisibility(View.INVISIBLE);
                                    mDeclinebtn.setEnabled(false);

                                }
                            });


                        }

                    });
                }


                //------------REQ receiced state ------------------------------------//


                if (mCurrent_state.equals("req_received")) {

                    final String CurrentDate = DateFormat.getDateTimeInstance().format(new Date());
                    mFriendDatabase.child(mCurrentUser.getUid()).child(user_id).setValue(CurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {


                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendDatabase.child(user_id).child(mCurrentUser.getUid()).setValue(CurrentDate).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mFriendrequestDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    mFriendrequestDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            mFriendRequestbtn.setEnabled(true);
                                                            mCurrent_state = "friends";
                                                            mFriendRequestbtn.setText("Unfriend this person");

                                                            mDeclinebtn.setVisibility(View.INVISIBLE);
                                                            mDeclinebtn.setEnabled(false);

                                                        }
                                                    });


                                                }

                                            });

                                        }
                                    });
                        }
                    });
                }


            }
        });
    }
}


// Note we need to handle Unfriend button -> vedio N 20
// Note we need to handle friends (not to be afriend of my self)
