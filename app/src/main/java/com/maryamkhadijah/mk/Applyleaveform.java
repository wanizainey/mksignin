package com.maryamkhadijah.mk;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Applyleaveform extends AppCompatActivity {

    private Spinner leavetype;
    private TextView textStartDate, textEndDate, textTotalDays, textUploadFile, txtLeaveReason;
    private CheckBox checkMorningLeave, checkEveningLeave;
    private DatabaseReference leaveDatabase;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyleaveform);

        leavetype = findViewById(R.id.leavetype);
        textStartDate = findViewById(R.id.textStartDate);
        textEndDate = findViewById(R.id.textEndDate);
        textTotalDays = findViewById(R.id.textTotalDays);
        checkMorningLeave = findViewById(R.id.checkMorningLeave);
        checkEveningLeave = findViewById(R.id.checkEveningLeave);
        textUploadFile = findViewById(R.id.textUploadFile);
        txtLeaveReason = findViewById(R.id.TxtLeavereason);

        calendar = Calendar.getInstance();
        leaveDatabase = FirebaseDatabase.getInstance().getReference("leaveApplications");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.leave_types_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leavetype.setAdapter(adapter);

        textStartDate.setOnClickListener(v -> showDatePickerDialog(textStartDate));
        textEndDate.setOnClickListener(v -> showDatePickerDialog(textEndDate));

        checkMorningLeave.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalDays());
        checkEveningLeave.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalDays());

        textUploadFile.setOnClickListener(v -> showUploadOptions());

        Button btnSubmitLeave = findViewById(R.id.btnSubmitLeave);
        btnSubmitLeave.setOnClickListener(v -> saveLeaveApplicationData());

        // Allow unticking of Morning Leave
        checkMorningLeave.setOnClickListener(v -> {
            if (checkMorningLeave.isChecked()) {
                checkEveningLeave.setChecked(false);
                updateTotalDays();
            } else {
                // Allow unticking
                updateTotalDays();
            }
        });

        checkEveningLeave.setOnClickListener(v -> {
            if (checkEveningLeave.isChecked()) {
                checkMorningLeave.setChecked(false);
                updateTotalDays();
            } else {
                // Allow unticking
                updateTotalDays();
            }
        });
    }

    private void showDatePickerDialog(final TextView textView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.getTime());
                    textView.setText(selectedDate);
                    updateTotalDays();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void updateTotalDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            Date startDate = sdf.parse(textStartDate.getText().toString());
            Date endDate = sdf.parse(textEndDate.getText().toString());

            long diffInMillis = endDate.getTime() - startDate.getTime();
            double diffInDays = diffInMillis / (24.0 * 60.0 * 60.0 * 1000.0) + 1; // Use double for decimal values

            // Check if both Morning Leave and Evening Leave are selected
         /*   if (checkMorningLeave.isChecked() && checkEveningLeave.isChecked()) {
                // Untick Morning Leave if both are selected
                checkMorningLeave.setChecked(false);
            } */

            if (!checkMorningLeave.isChecked() && !checkEveningLeave.isChecked()) {
                checkMorningLeave.setChecked(false);
                checkEveningLeave.setChecked(false);
            }
                // Check if either Morning Leave or Evening Leave is selected
            if (checkMorningLeave.isChecked() || checkEveningLeave.isChecked()) {
                diffInDays -= 0.5;
            }

            // Handle same day selection
            if (startDate.equals(endDate)) {
                diffInDays = 1;
            }

            textTotalDays.setText("Total Days: " + diffInDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showUploadOptions() {
        // Your logic to handle upload options
    }

    private void saveLeaveApplicationData() {
        String selectedLeaveType = leavetype.getSelectedItem().toString();
        String startDate = textStartDate.getText().toString();
        String endDate = textEndDate.getText().toString();
        String leaveReason = txtLeaveReason.getText().toString();
        boolean morningLeave = checkMorningLeave.isChecked();
        boolean eveningLeave = checkEveningLeave.isChecked();

        // Construct a LeaveApplication object with the obtained data
        LeaveApplication leaveApplication = new LeaveApplication(
                selectedLeaveType,
                startDate,
                endDate,
                leaveReason,
                checkMorningLeave.isChecked(),
                checkEveningLeave.isChecked()
        );

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Convert the LeaveApplication object to a Map
            Map<String, Object> leaveMap = leaveApplication.toMap();

            // Save the leave application data to the database under the user's UID
            leaveDatabase.child(currentUser.getUid()).push().updateChildren(leaveMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Data was successfully saved
                            Toast.makeText(Applyleaveform.this, "Leave application submitted", Toast.LENGTH_SHORT).show();
                            finish();
                            // Optionally close the activity after submission
                        } else {
                            // Handle the error
                            Toast.makeText(Applyleaveform.this, "Error submitting leave application", Toast.LENGTH_SHORT).show();
                            // Log the error for debugging
                            task.getException().printStackTrace();
                        }
                    });
        }
    }
}
