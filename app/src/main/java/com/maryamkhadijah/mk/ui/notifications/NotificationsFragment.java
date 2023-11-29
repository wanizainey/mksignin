package com.maryamkhadijah.mk.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maryamkhadijah.mk.R;
import com.maryamkhadijah.mk.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the TextView for announcements
        TextView announcementTextView = root.findViewById(R.id.announcementTextView);

        // Set some example announcement text
        announcementTextView.setText("Important Announcement:\nYour text goes here.");




        // Create an adapter for the RecyclerView (you need to create this adapter)
        // RecyclerView.Adapter adapter = new YourRecyclerViewAdapter();
        // recyclerView.setAdapter(adapter);

        return root;
    }
}
