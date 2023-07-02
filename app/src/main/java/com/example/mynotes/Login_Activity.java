package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.function.LongFunction;

public class Login_Activity extends AppCompatActivity {
    EditText emailEditText, passEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText=findViewById(R.id.email_edit_text);
        passEditText=findViewById(R.id.pass_edit_text);
        loginBtn=findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.progress_bar);
        createAccountBtnTextView=findViewById(R.id.sign_text_view_btn);

        loginBtn.setOnClickListener(v-> loginUser());
        createAccountBtnTextView.setOnClickListener((v)->startActivities(new Intent[]{new Intent(Login_Activity.this, Create_Account_Activity.class)}));

    }
    void loginUser(){
        String email=emailEditText.getText().toString();
        String password=passEditText.getText().toString();

        boolean isvalidated=validateData(email, password);
        if(!isvalidated){
            return;
        }
        loginInFireBase(email, password);
    }

    void loginInFireBase(String email, String password){
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //login is Successful
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //goto main activity
                        startActivities(new Intent[]{new Intent(Login_Activity.this, MainActivity.class)});
                        finish();
                    }

                    else{
                        Toast.makeText(Login_Activity.this, "email not verifed", Toast.LENGTH_SHORT).show();

                    }

                }
                else{
                    //login failed
                    Toast.makeText(Login_Activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password){
        //validate the data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passEditText.setError("Short Password not allow");
            return false;
        }

        return true;
    }
}