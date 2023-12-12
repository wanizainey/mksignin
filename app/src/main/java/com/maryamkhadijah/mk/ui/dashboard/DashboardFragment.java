package com.maryamkhadijah.mk.ui.dashboard;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.maryamkhadijah.mk.R;
import com.maryamkhadijah.mk.applyleave;
import com.maryamkhadijah.mk.databinding.FragmentDashboardBinding;
import com.maryamkhadijah.mk.edit_profile;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    // Request code for image picker
    private static final int PICK_IMAGE_REQUEST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                // i.putExtra("Value1", "DFP50283");
                // i.putExtra("Value2", "MAD");

                startActivity(i);
            }
        });

        btnClaim.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
             Toast.makeText(getActivity(), "Apply Cuti ke tu", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), applyleave.class);
            // i.putExtra("Value1", "DFP50283");
            // i.putExtra("Value2", "MAD");

             startActivity(i);
        }
    });


        return root;
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
            userImageView.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
