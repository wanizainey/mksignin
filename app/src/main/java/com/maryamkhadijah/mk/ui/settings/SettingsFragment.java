package com.maryamkhadijah.mk.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.maryamkhadijah.mk.ChangePasswordActivity;
import com.maryamkhadijah.mk.CompanyDetail;
import com.maryamkhadijah.mk.databinding.FragmentSetting1Binding;
import com.maryamkhadijah.mk.splashscreen;

public class SettingsFragment extends Fragment {

    private FragmentSetting1Binding binding;
    private TextView changePasswordTextView, companyD;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetting1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Check if binding is not null before accessing its properties
        if (binding != null) {
            // Initialize and finalize the view(s)
            // ChangePassword TextView
            changePasswordTextView = binding.changePasswordTV;
            changePasswordTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                    startActivity(intent);
                }
            });

            // CompanyDetail TextView
            companyD = binding.CompanyD;
            companyD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), CompanyDetail.class);
                    startActivity(intent);
                }
            });

            logoutButton = binding.logoutBtn;
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), splashscreen.class);
                    startActivity(intent);
                    // Instead of onDestroy(), you can finish the activity
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
