package com.maryamkhadijah.mk.ui.home;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maryamkhadijah.mk.ClockData;
import com.maryamkhadijah.mk.R;
import com.maryamkhadijah.mk.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private Button btnClock, btnClock2; // Updated button names
    private TextView txtLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private boolean isClockIn = false;
    private boolean isClockButtonEnabled = true; // Added variable to track button state


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.homeTitle;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        btnClock = root.findViewById(R.id.btnClock); // Updated button name
        btnClock2 = root.findViewById(R.id.btnClock2); // Updated button name
        txtLocation = root.findViewById(R.id.txtLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("attendance").child(currentUser.getUid());

        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClockButtonEnabled) {
                    isClockIn = true;
                    isClockButtonEnabled = false; // Disable the button
                    requestLocationUpdates();
                }else {
                    Toast.makeText(requireActivity(), "You have already clocked in. Clock out first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClockButtonEnabled) {
                    isClockIn = false;
                    isClockButtonEnabled = true; // Re-enable the button after clocking out
                    requestLocationUpdates();
                }else {
                    Toast.makeText(requireActivity(), "You need to clock in first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    saveClockData(location);
                    stopLocationUpdates();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            // Permissions are granted, start location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


    private void saveClockData(Location location) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Geocode the location to get the address
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressString = address.getAddressLine(0); // You can concatenate more address lines if needed

                // Create a ClockData object with the address
                ClockData clockData = new ClockData(currentTime, location.getLatitude(), location.getLongitude(), addressString);
                String clockType = isClockIn ? "ClockIn" : "ClockOut";
                String key = databaseReference.child(clockType).push().getKey();
                databaseReference.child(clockType).child(key).setValue(clockData);

                // Update the UI
                txtLocation.setText("Location: " + addressString);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
