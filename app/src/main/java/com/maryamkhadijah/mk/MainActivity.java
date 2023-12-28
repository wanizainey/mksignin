package com.maryamkhadijah.mk;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.maryamkhadijah.mk.databinding.ActivityNavGraphBinding;

public class MainActivity extends AppCompatActivity {

    private Button btnSignin, btnSignup;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;

    private ActivityNavGraphBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        btnSignin = findViewById(R.id.btnLogin);
        editTextEmail = findViewById(R.id.editEmailLogin);
        editTextPassword = findViewById(R.id.editPasswordLogin);
        btnSignup = findViewById(R.id.btnRegister);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, register.class));
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform login
                String email, password;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Success
                                String user_id = mAuth.getCurrentUser().getEmail();
                                Toast.makeText(MainActivity.this, user_id, Toast.LENGTH_SHORT).show();

                                // Check if the logged-in user is the admin
                                if (isUserAdmin(user_id)) {
                                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                } else {
                                    startActivity(new Intent(MainActivity.this, nav_graph.class));
                                }
                                finish();
                            } else {
                                // Fail
                                Toast.makeText(MainActivity.this, "Email or password must be correct", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // Function to check if the logged-in user is the admin
    private boolean isUserAdmin(String userEmail) {
        return userEmail.equals("adminhr@gmail.com");
    }
}
