package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class Create_Account_Activity extends AppCompatActivity {
    EditText emailEditText, passEditText, confPassEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText=findViewById(R.id.email_edit_text);
        passEditText=findViewById(R.id.pass_edit_text);
        confPassEditText=findViewById(R.id.conf_pass_edit_text);
        createAccountBtn=findViewById(R.id.create_account_button);
        progressBar=findViewById(R.id.progress_bar);
        loginBtnTextView=findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v->startActivities(new Intent[]{new Intent(Create_Account_Activity.this, Login_Activity.class)}));


        }
    void createAccount(){
        String email=emailEditText.getText().toString();
        String password=passEditText.getText().toString();
        String conf_password=confPassEditText.getText().toString();

        boolean isvalidated=validateData(email, password, conf_password);
        if(!isvalidated){
            return;
        }
        createAccountInFirebase(email, password);
    }
    void createAccountInFirebase(String email, String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Create_Account_Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //Account Created Successfully
                    Toast.makeText(Create_Account_Activity.this, "Account Created Successfully, Check Your Email to verify", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }
                else{
                    //Account not created
                    Toast.makeText(Create_Account_Activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String conf_password){
        //validate the data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passEditText.setError("Short Password not allow");
            return false;
        }
        if(!password.equals(conf_password)){
            confPassEditText.setError("Password Mismatch");
            return false;
        }
        return true;
    }
}