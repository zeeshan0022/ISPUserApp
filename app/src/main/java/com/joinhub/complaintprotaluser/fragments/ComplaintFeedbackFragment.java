package com.joinhub.complaintprotaluser.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joinhub.complaintprotaluser.R;
import com.joinhub.complaintprotaluser.databinding.FragmentComplaintFeedbackBinding;
import com.joinhub.complaintprotaluser.models.FeedbackModel;

import java.util.Objects;


public class ComplaintFeedbackFragment extends Fragment {
    FragmentComplaintFeedbackBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     binding= FragmentComplaintFeedbackBinding.inflate(getLayoutInflater());

     binding.btnSendComplaint.setOnClickListener(v->{

         DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Feedbacks");
         reference.push().setValue(new FeedbackModel(Objects.requireNonNull(binding.nameEditText.getText()).toString(),
                 Objects.requireNonNull(binding.phoneEditText.getText()).toString(), Objects.requireNonNull(binding.subjEditText.getText()).toString().toString(),
                 Objects.requireNonNull(binding.descEditText.getText()).toString())).addOnSuccessListener(unused -> Toast.makeText(requireContext(), "Feedback Sent Successfully",Toast.LENGTH_LONG).show()).addOnFailureListener(e -> Toast.makeText(requireContext(), "Exception: "+e.toString(),Toast.LENGTH_LONG).show());
     });

     return binding.getRoot();
    }
}