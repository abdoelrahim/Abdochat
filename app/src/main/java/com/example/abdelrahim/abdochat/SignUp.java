package com.example.abdelrahim.abdochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    EditText SignUp_etEmail, SignUp_etPassword;
    TextInputLayout SignUp_inputEmail;
    TextInputLayout SignUp_inputPassword;
    TextView SignUp_tvForgetPassword;
    TextView SignUp_tv_loginMe;
    TextView SignUp_tvOr;
    RelativeLayout SignUp_Activity;
    private FirebaseAuth mAuth;
    private Toolbar stoolbar;
    private FirebaseDatabase database;
    private DatabaseReference mUserDatabase;

   private ProgressDialog signup_progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        stoolbar=(Toolbar) findViewById(R.id.Signup_tollbar);
        setSupportActionBar(stoolbar);
        getSupportActionBar().setTitle("Create new Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mAuth = FirebaseAuth.getInstance();


        SignUp_tvForgetPassword = (TextView) findViewById(R.id.Signup_tv_forget_password);
        SignUp_tv_loginMe = (TextView) findViewById(R.id.Signup_tv_loginMe);
        btnSignUp = (Button) findViewById(R.id.Signup_btn_register);
        SignUp_etEmail = (EditText) findViewById(R.id.Signup_et_email);
        SignUp_etPassword = (EditText) findViewById(R.id.Signup_et_password);
        SignUp_inputEmail = (TextInputLayout) findViewById(R.id.Signup_input_email);
        SignUp_inputPassword = (TextInputLayout) findViewById(R.id.Signup_input_password);

        signup_progressDialog=new ProgressDialog(this);

        btnSignUp.setOnClickListener(this);
        SignUp_tv_loginMe.setOnClickListener(this);
        SignUp_tvForgetPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


    }

    public SignUp() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.Signup_tv_loginMe) {
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();

        } else if (view.getId() == R.id.Signup_tv_forget_password) {
            startActivity(new Intent(SignUp.this, ForgetPassword.class));
            finish();
        } else if (view.getId() == R.id.Signup_btn_register) {

            final String email = SignUp_etEmail.getText().toString();
            final String password = SignUp_etPassword.getText().toString();

            if (!(email.isEmpty()&&password.isEmpty())) {
                signup_progressDialog.setTitle("Registering...");
                signup_progressDialog.setMessage("Please wait...");
                signup_progressDialog.setCanceledOnTouchOutside(false);
                signup_progressDialog.show();
                SignUpUser();
            }
        }


    }

    private void SignUpUser() {
        final String email = SignUp_etEmail.getText().toString();
        final String password = SignUp_etPassword.getText().toString();

        if (email.isEmpty()) {
            SignUp_etEmail.setError("Enter Email");
            SignUp_etEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SignUp_etEmail.setError("Email Not Formatted Well");
            SignUp_etEmail.requestFocus();
        } else if (password.isEmpty()) {
            SignUp_etPassword.setError("Enter Password");
            SignUp_etPassword.requestFocus();
        } else if (password.length() < 6) {
            SignUp_etPassword.setError("Password Length must be greater than 6");
            SignUp_etPassword.requestFocus();
        } else {


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {

                        /*
                        String user_id = mAuth.getCurrentUser().getUid();

                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        mUserDatabase.child(user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUp.this, "device token Registered", Toast.LENGTH_SHORT).show();

                            }
                        });


                        */

                        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid=currentuser.getUid();

                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference myref = database.getReference().child("Users").child(uid);




                        HashMap<String,String> userMap = new HashMap<>();


                        userMap.put("Image","default");
                        userMap.put("Status","Hi there I'm using abdoChat");
                        userMap.put("email",email);
                        userMap.put("name","Abdo");
                        userMap.put("password",password);
                        userMap.put("thumb_image","default");  // check




                        myref.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                signup_progressDialog.dismiss();
                                startActivity(new Intent(SignUp.this, DashBoard.class));


                            }
                        });




                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                    } else if (!task.isSuccessful()) {

                        signup_progressDialog.hide();
                        Toast.makeText(SignUp.this, "Registeration Failed", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

    }


    /*private void saveUserInfo(String uid) {

        String birthDay = dateOfBirth.getDayOfMonth() + "/" + (dateOfBirth.getMonth() + 1) + "/" + dateOfBirth.getYear();
        String email = etEmail.getText().toString();
        String address = etAddress.getText().toString();
        String name = etName.getText().toString();
        int gender = rgGender.getCheckedRadioButtonId() == R.id.rb_male ? 1 : 2;

        User newUser = new User(name, email, address, birthDay, gender);

        FirebaseDatabase.getInstance().getReference("User").child(uid).setValue(newUser);


    }
    */

}
