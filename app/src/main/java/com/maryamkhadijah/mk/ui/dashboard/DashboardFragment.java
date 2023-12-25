package com.maryamkhadijah.mk.ui.dashboard;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.database.DatabaseReference;
import com.maryamkhadijah.mk.Employee;
import com.maryamkhadijah.mk.R;
import com.maryamkhadijah.mk.applyleave;
import com.maryamkhadijah.mk.databinding.FragmentDashboardBinding;
import com.maryamkhadijah.mk.edit_profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DatabaseReference mDatabase;
    // Request code for image picker
    private static final int PICK_IMAGE_REQUEST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        final View root = binding.getRoot();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("employees").child(currentUser.getUid());

        // Call the loadProfileData method
        loadProfileData(root, currentUser);

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize the button correctly
        Button btnedit;
        ImageButton btnClaim;


                btnedit = root.findViewById(R.id.editprofileEmp);
                btnClaim = root.findViewById(R.id.btnClaim);


        // Handle click on the userImage
        ImageView userImage = root.findViewById(R.id.userImage);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle image click, for example, open an image picker
                selectImage();
            }
        });

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Edit Profile Nohh", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), edit_profile.class);
                startActivity(i);
            }
        });

        btnClaim.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
             Toast.makeText(getActivity(), "Apply Cuti ke tu", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), applyleave.class);

             startActivity(i);
        }
    });

        return root;
    }

    private void loadProfileData(View root, FirebaseUser currentUser) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    if (employee != null) {
                        // Update UI with user profile data
                        TextView idEmpTextView = root.findViewById(R.id.idEmp);
                        TextView emailEmpTextView = root.findViewById(R.id.emailEmp);
                        TextView userEmpTextView = root.findViewById(R.id.userEmp);

                        idEmpTextView.setText("ID Employee: " + currentUser.getUid());
                        emailEmpTextView.setText("Email: " + currentUser.getEmail());
                        userEmpTextView.setText("Name: " + employee.getName());

                        ImageView userImageView = root.findViewById(R.id.userImage);

                        // Check if imageUrl is not null or empty
                        if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {

                            Log.d("ImageURL", "URL: " + employee.getImageUrl());
                            // Load the image using Glide
                            Glide.with(requireContext())
                                    .load(employee.getImageUrl())
                                    .placeholder(R.drawable.placeholder_image)
                                    .into(userImageView);
                        } else {
                            // Set a default image or handle the case where imageUrl is empty
                            userImageView.setImageResource(R.drawable.placeholder_image);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    private void selectImage() {
        // Create an intent to open the image picker
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // Check if there's an app available to handle this intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            // Handle the case where there is no app available to handle the intent
            Toast.makeText(getActivity(), "No app available to handle image selection", Toast.LENGTH_SHORT).show();
        }
    }

    // Override onActivityResult to handle the result of the image picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // The user has successfully picked an image
            Uri selectedImageUri = data.getData();

            // You can use the selected image URI as needed (e.g., load it into an ImageView)
            // For example, if you have an ImageView with the id R.id.userImage:
            ImageView userImageView = getView().findViewById(R.id.userImage);
            Glide.with(requireContext())
                    .load(selectedImageUri)
                    .placeholder(R.drawable.placeholder_image)
                    .into(userImageView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
