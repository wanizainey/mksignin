package com.maryamkhadijah.mk;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;




import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Applyleaveform extends AppCompatActivity {

    private Spinner leavetype;
    private TextView textStartDate, textEndDate, textTotalDays;
    private CheckBox checkMorningLeave, checkEveningLeave;
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

        calendar = Calendar.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.leave_types_array,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        leavetype.setAdapter(adapter);

        // Set OnClickListeners for the date TextViews
        textStartDate.setOnClickListener(v -> showDatePickerDialog(textStartDate));
        textEndDate.setOnClickListener(v -> showDatePickerDialog(textEndDate));

        // Set OnCheckedChangeListener for the checkboxes
        setCheckboxListeners(checkMorningLeave, checkEveningLeave);
    }

    // Show a DatePickerDialog for the selected TextView
    private void showDatePickerDialog(TextView textView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    textView.setText(selectedDate);

                    // Check if both start and end dates are set before updating total days
                    if (!textStartDate.getText().toString().isEmpty() && !textEndDate.getText().toString().isEmpty()) {
                        updateTotalDays();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    // Set listeners for the checkboxes to enforce mutual exclusivity
    private void setCheckboxListeners(CheckBox checkBox1, CheckBox checkBox2) {
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBox2.setChecked(false);
                updateTotalDays();
            }
        });

        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBox1.setChecked(false);
                updateTotalDays();
            }
        });
    }

    // Update the total days TextView based on the selected start and end dates, and checkboxes
    private void updateTotalDays() { SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date startDate = sdf.parse(textStartDate.getText().toString());
            Date endDate = sdf.parse(textEndDate.getText().toString());

            long diffInMillis = endDate.getTime() - startDate.getTime();
            long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);

            // Check if Morning Leave or Evening Leave is selected
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

    public void showUploadOptions(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Options");

        String[] options = {"Take Photo", "Choose from Library", "Remove Current Picture", "Cancel"};

        builder.setItems(options, (dialog, which) -> {
            // Handle the selected option
            switch (which) {
                case 0:
                    // Take Photo
                    // Implement the logic to use the device camera
                    takePhoto();
                    break;
                case 1:
                    // Choose from Library
                    // Implement the logic to use the device gallery
                    chooseFromLibrary();
                    break;
                case 2:
                    // Remove Current Picture
                    // Implement the logic to remove the selected or taken picture
                    removeCurrentPicture();
                    break;
                case 3:
                    // Cancel
                    // Do nothing or handle cancellation logic
                    cancelCurrentDialog();
                    break;
            }
        });

        builder.show();
    }
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // Method to handle taking a photo using the device camera
    private void takePhoto() {
        // Check if the app has the CAMERA permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request CAMERA permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            return;
        }

        // Check if the device has any camera feature
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Create an intent to open the camera app
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Ensure there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Start the camera intent
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            // Handle the case where the device doesn't have a camera feature
            // You might want to display a message to the user or take alternative actions
            Toast.makeText(this, "No camera feature available on this device", Toast.LENGTH_SHORT).show();
        }
    }
// ...

    // Override onActivityResult to handle the result of the camera intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // The photo was successfully taken
            // You can access the photo from the 'data' Intent or use a file path if you specified it in the camera intent
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Now you can do something with the captured image, such as display it in an ImageView
            // imageView.setImageBitmap(imageBitmap);
        }
    }

   //Add this constant at the class level
    private static final int REQUEST_PICK_IMAGE = 2;

    // Method to handle choosing from the device gallery
    private void chooseFromLibrary() {
        // Check if the app has the READ_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request READ_EXTERNAL_STORAGE permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PICK_IMAGE);
            return;
        }

        // Create an intent to open the gallery
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Ensure there's a gallery activity to handle the intent
        if (pickPhotoIntent.resolveActivity(getPackageManager()) != null) {
            // Start the gallery intent
            startActivityForResult(pickPhotoIntent, REQUEST_PICK_IMAGE);
        }
    }

    // Assume this is your Bitmap variable
    private Bitmap selectedImageBitmap;

    // Method to handle removing the selected or taken picture
    private void removeCurrentPicture() {
        // Clear the Bitmap to remove the selected picture
        selectedImageBitmap = null;

        // Update your UI or do other necessary actions
        // For example, if you're using an ImageView to display the Bitmap:
        // yourImageView.setImageBitmap(null);
    }



    private void cancelCurrentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Upload");

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }











}
