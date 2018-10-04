package com.example.mishalthakkar.phonefinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText Uname ;
    EditText Password;
    Button Sign_in;
    TextView Register;
    FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Uname = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        Sign_in = findViewById(R.id.button);
        //mTextView = findViewById(R.id.textView);
        Register = findViewById(R.id.textView3);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if (user != null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Uname.getText().toString()) || TextUtils.isEmpty(Password.getText().toString()) )
                {
                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                }
                else
                    validate(Uname.getText().toString(),Password.getText().toString());
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    public void validate(String uname,String password)
    {
        mProgressDialog.setMessage("Please wait while we verify your details");
        mProgressDialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(uname,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Login Sucessful",Toast.LENGTH_SHORT);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    //checkEmailVerification();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean flag = firebaseUser.isEmailVerified();

        if (flag)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
        else
        {
            Toast.makeText(this,"Please Verify your Email address",Toast.LENGTH_SHORT).show();
            mFirebaseAuth.signOut();
        }
    }
}
