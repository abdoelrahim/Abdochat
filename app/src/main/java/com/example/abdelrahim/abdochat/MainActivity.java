package com.example.abdelrahim.abdochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mtoolbar;
    private ProgressDialog Login_progressDialog;
    Button btnLogin, btnsignUp;
    EditText etEmail, etPassword;
    TextInputLayout inputEmail;
    TextInputLayout inputPassword;
    TextView tvForgetPassword;
    TextView tvSignUp;
    TextView tvOr;
    RelativeLayout main_Activity;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        tvForgetPassword = (TextView) findViewById(R.id.login_tv_forget_password);
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnsignUp = (Button) findViewById(R.id.login_btn_signUp);
        etEmail = (EditText) findViewById(R.id.login_et_email);
        etPassword = (EditText) findViewById(R.id.login_et_password);
        inputEmail = (TextInputLayout) findViewById(R.id.ligin_input_email);
        inputPassword = (TextInputLayout) findViewById(R.id.login_input_password);


        Login_progressDialog = new ProgressDialog(this);
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users");

        mtoolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Abdo chat");


        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        btnsignUp.setOnClickListener(this);


        if (mAuth.getCurrentUser() != null)
            startActivity(new Intent(MainActivity.this, DashBoard.class));


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
        if (view.getId() == R.id.login_tv_forget_password) {
            startActivity(new Intent(MainActivity.this, ForgetPassword.class));
            finish();

        } else if (view.getId() == R.id.login_btn_signUp) {
            startActivity(new Intent(MainActivity.this, SignUp.class));
            finish();
        } else if (view.getId() == R.id.login_btn_login) {

            final String email = etEmail.getText().toString();
            final String password = etPassword.getText().toString();

            if (!(email.isEmpty() && password.isEmpty())) {
                Login_progressDialog.setTitle("Logging...");
                Login_progressDialog.setMessage("Please wait...");
                Login_progressDialog.setCanceledOnTouchOutside(false);
                Login_progressDialog.show();
                LoginUser();
            }


        }


    }

    private void LoginUser() {

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Enter Email");
            etEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email Not Formatted Well");
            etEmail.requestFocus();
        } else if (password.isEmpty()) {
            etPassword.setError("Enter Password");
            etPassword.requestFocus();
        } else if (password.length() < 6) {
            etPassword.setError("Password Length must be greater than 6");
            etPassword.requestFocus();
        } else {


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Login_progressDialog.dismiss();


                        String user_id = mAuth.getCurrentUser().getUid();

                        String deviceToken = FirebaseInstanceId.getInstance().getToken();

                        mUserDatabase.child(user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(MainActivity.this, "You can login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, DashBoard.class));
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                            }
                        });


                                            } else if (!task.isSuccessful()) {

                        Login_progressDialog.hide();

                        Toast.makeText(MainActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                    }

                    // ...
                }
            });
        }


    }


    private void sendtologinActivity() {

    }
}
