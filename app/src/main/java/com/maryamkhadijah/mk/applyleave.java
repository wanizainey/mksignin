// applyleave.java
package com.maryamkhadijah.mk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class applyleave extends AppCompatActivity {

    private static final String TAG = "ApplyLeaveActivity";
    private ListView listLeaveStatus;
    private DatabaseReference leaveDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applyleave);

        Button btnapplyleave;

        btnapplyleave = findViewById(R.id.btnapplyleave);
        listLeaveStatus = findViewById(R.id.listLeaveStatus);
        leaveDatabase = FirebaseDatabase.getInstance().getReference("leaveApplications");

        // Set up a click listener for the Apply Leave button
        btnapplyleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applyleave.this, "Legal Info", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), Applyleaveform.class);
                startActivity(i);
            }
        });

        // Set up a click listener for the Leave Status list items (optional)
        listLeaveStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click if needed
            }
        });

        // Fetch and display leave applications for the current user
        displayLeaveApplications();
    }

    private void displayLeaveApplications() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            leaveDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> leaveStatusList = new ArrayList<>();

                    for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                        // Update this part to handle both String and HashMap data
                        Object leaveDataObject = leaveSnapshot.getValue();

                        if (leaveDataObject instanceof HashMap) {
                            HashMap<String, Object> leaveData = (HashMap<String, Object>) leaveDataObject;

                            // Customize this part to extract data from the HashMap
                            String leaveStatus = "Type: " + leaveData.get("leaveType") +
                                    "\nStart Date: " + leaveData.get("startDate") +
                                    "\nEnd Date: " + leaveData.get("endDate");

                            // Check if the 'isApproved' field is not null before comparing
                            Object approvedValue = leaveData.get("isApproved");
                            if (approvedValue != null && approvedValue instanceof Boolean) {
                                leaveStatus += "\nStatus: " + ((Boolean) approvedValue ? "Approved" : "Pending");
                            } else {
                                // Handle the case where 'isApproved' is null or not a Boolean (if needed)
                                leaveStatus += "\nStatus: Pending"; // Assuming Pending by default
                            }

                            leaveStatusList.add(leaveStatus);

                        } else if (leaveDataObject instanceof String) {
                            // Handle the case where the data is a String (if needed)
                        }
                    }

                    // Display the leave applications in a ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(applyleave.this, android.R.layout.simple_list_item_1, leaveStatusList);
                    listLeaveStatus.setAdapter(adapter);

                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }
}
