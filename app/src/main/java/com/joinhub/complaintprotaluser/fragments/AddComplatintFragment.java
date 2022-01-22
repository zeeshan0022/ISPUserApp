package com.joinhub.complaintprotaluser.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joinhub.complaintprotaluser.R;

public class AddComplatintFragment extends Fragment {



    public AddComplatintFragment() {
        // Required empty public constructor
    }
 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_complatint, container, false);
    }
}