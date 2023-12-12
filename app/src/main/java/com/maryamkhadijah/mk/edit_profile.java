package com.maryamkhadijah.mk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_profile extends AppCompatActivity {
    private EditText nameEditText, departmentEditText, emailEditText, imageUrlEditText;
    private Button updateButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in
            // Handle this case, maybe redirect to login activity
            Intent intent = new Intent(edit_profile.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("employees").child(currentUser.getUid());

        nameEditText = findViewById(R.id.txtName);
        departmentEditText = findViewById(R.id.txtDepartment);
        emailEditText = findViewById(R.id.txtEmail);
        imageUrlEditText = findViewById(R.id.txtImageUrl);
        updateButton = findViewById(R.id.buttonUpdate);

        // Display the authenticated user's email
        emailEditText.setText(currentUser.getEmail());

        // Make the email EditText non-editable
        emailEditText.setFocusable(false);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        // Load existing profile data
        loadProfileData();
    }

    private void loadProfileData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    if (employee != null) {
                        nameEditText.setText(employee.getName());
                        departmentEditText.setText(employee.getDepartment());
                        imageUrlEditText.setText(employee.getImageUrl());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void updateProfile() {
        String newName = nameEditText.getText().toString().trim();
        String newDepartment = departmentEditText.getText().toString().trim();
        String newImageUrl = imageUrlEditText.getText().toString().trim();

        // Update the employee object
        Employee updatedEmployee = new Employee(newName, mAuth.getCurrentUser().getEmail(), newDepartment, newImageUrl);

        // Update the data in Firebase
        mDatabase.setValue(updatedEmployee);

        // Display a success message
        Toast.makeText(edit_profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

    }
}
