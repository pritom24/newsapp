package com.example.dipto.newsgoround;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private ImageView backtoSignInPageButton;
    private Button submitButton;
    private EditText usernameEditText,passwordEditText,emailEditText;

    private FirebaseAuth mAuth;


    //can be removed anytime
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        submitButton = (Button) findViewById(R.id.submitButton);
        backtoSignInPageButton = (ImageView) findViewById(R.id.backButton);


        backtoSignInPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySignIn();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password  = passwordEditText.getText().toString();

                if(!TextUtils.isEmpty(username) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(email)){
                    progressDialog.setTitle("Registering User");
                    progressDialog.setMessage("Please wait while we create your account !");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    registerUser(username,email,password);
                }


            }
        });



    }

    private void registerUser(final String username, final String email,String password) {


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){

                    progressDialog.hide();
                    Toast.makeText(SignUpActivity.this,"Can not create account.Please check form and try again!",Toast.LENGTH_LONG).show();


                }else{


                    progressDialog.dismiss();
                    String u_id = mAuth.getCurrentUser().getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users").child(u_id);
                    Map values = new HashMap();
                    values.put("email" , email);
                    values.put("username" , username);

                    myRef.setValue(values);
                    openActivitySignIn();

                }
            }
        });

    }

    public void openActivitySignIn(){
        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
