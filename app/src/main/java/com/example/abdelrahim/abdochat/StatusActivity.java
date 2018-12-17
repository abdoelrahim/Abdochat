package com.example.abdelrahim.abdochat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatusActivity extends AppCompatActivity {

    private Button Statusbtn;
    private Toolbar statustoolbar;
    private TextInputLayout statustextInputLayout;
    private DatabaseReference mStatusdatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mStatusdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        statustoolbar = (Toolbar) findViewById(R.id.status_toolbar);
        statustextInputLayout = (TextInputLayout) findViewById(R.id.Status_input_Status);
        Statusbtn = findViewById(R.id.Status_btn_Change);

        setSupportActionBar(statustoolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String Status_value= getIntent().getStringExtra("Current_Status");




        statustextInputLayout.getEditText().setText(Status_value);



        Statusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(StatusActivity.this);
                progressDialog.setTitle("Saving Changes");
                progressDialog.setMessage("Please wait");
                progressDialog.show();

                String status = statustextInputLayout.getEditText().getText().toString();

                mStatusdatabase.child("Status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error in saving", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });

    }

}







