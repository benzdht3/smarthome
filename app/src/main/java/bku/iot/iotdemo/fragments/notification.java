package bku.iot.iotdemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import bku.iot.iotdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notification extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
}