package bku.iot.iotdemo.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bku.iot.iotdemo.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private List<notification.Notification> notifications;

    public NotificationAdapter(List<notification.Notification> notifications) {
        this.notifications = notifications;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            content = itemView.findViewById(R.id.notification_content);
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noti_item, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        notification.Notification notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.content.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
