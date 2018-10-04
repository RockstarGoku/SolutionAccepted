package com.example.mishalthakkar.phonefinder;

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

public class RegistrationActivity extends AppCompatActivity {
    EditText Uname,Password,Email;
    Button Signup;
    TextView Login;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Uname = findViewById(R.id.editText);
        Password = findViewById(R.id.editText2);
        Email = findViewById(R.id.editText3);
        Signup = findViewById(R.id.button2);
        Login = findViewById(R.id.textView4);
        mFirebaseAuth = FirebaseAuth.getInstance();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                {
                    //Code for inserting User details into database
                    String user_email = Email.getText().toString().trim();
                    String password = Password.getText().toString().trim();

                    mFirebaseAuth.createUserWithEmailAndPassword(user_email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegistrationActivity.this,"Registration Sucessful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this,"oops Registration Failed.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean validate()
    {
        boolean flag = false;
        String uname,password,email;
        uname = Uname.getText().toString();
        password = Password.getText().toString();
        email = Email.getText().toString();

        if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }
        else
        {
            flag = true;
        }

        return flag;
    }
}
