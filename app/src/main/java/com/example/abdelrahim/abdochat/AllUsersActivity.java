package com.example.abdelrahim.abdochat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private RecyclerView usersrecyclerView;
    private DatabaseReference mUsersDatabase;
    private Query query;
    private FirebaseRecyclerAdapter<User, myViewHolader> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mtoolbar = (Toolbar) findViewById(R.id.users_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        usersrecyclerView = findViewById(R.id.users_list);
        usersrecyclerView.setHasFixedSize(true);
        usersrecyclerView.setLayoutManager(new LinearLayoutManager(this));

    } // oncreate

    @Override
    protected void onStart() {
        super.onStart();


        //FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        //String uid=currentuser.getUid();


        query = FirebaseDatabase.getInstance().getReference().child("Users");
        query.keepSynced(true);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<User, myViewHolader>(options) {
            @Override
            public myViewHolader onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

                return new myViewHolader(view);
            }

            @Override
            protected void onBindViewHolder(myViewHolader holder,int position,User model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setDisplayName(model.getName());
                holder.setDisplayStatus(model.getStatus());
                holder.setDisplayImage(model.getThumb_image(),getApplicationContext());
                final String user_id = getRef(position).getKey();

                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(AllUsersActivity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };

        usersrecyclerView.setAdapter(adapter);

        adapter.startListening();

    }

    public static class myViewHolader extends RecyclerView.ViewHolder {

        View mview;

        public myViewHolader(View itemView) {
            super(itemView);
            mview = itemView;


        }

        public void setDisplayName(String name) {
            TextView NameTextView = (TextView) mview.findViewById(R.id.item_user_name);
            NameTextView.setText(name);
        }

        public void setDisplayStatus(String name) {
            TextView statusTextView = (TextView) mview.findViewById(R.id.item_user_status);
            statusTextView.setText(name);


        }

        public void setDisplayImage(String thumb_image , Context ctx) {
            CircleImageView circleImageView = (CircleImageView) mview.findViewById(R.id.item_user_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.blank).into(circleImageView);



        }

    }

}
