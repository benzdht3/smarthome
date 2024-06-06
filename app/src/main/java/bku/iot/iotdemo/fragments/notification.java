package bku.iot.iotdemo.fragments;

import static bku.iot.iotdemo.fragments.realtime.getNotilist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bku.iot.iotdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notification#newInstance} factory method to
 * create an instance of this fragment.
 */

public class notification extends Fragment {

    public static class Notification {
        private String title;
        private String content;

        public Notification(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        List<Notification> notifications = getNotilist();
        if(notifications.size()==0){
            notifications.add(new Notification("No notification now",""));
        }
        if(notifications.size()>1 && notifications.get(0).title == "No notification now"){
            notifications.remove(0);
        }
        // Add more notifications here

        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
        return view;
    }
}