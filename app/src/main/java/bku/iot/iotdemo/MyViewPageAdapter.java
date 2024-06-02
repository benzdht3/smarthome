package bku.iot.iotdemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import bku.iot.iotdemo.fragments.automation;
import bku.iot.iotdemo.fragments.dashboard;
import bku.iot.iotdemo.fragments.notification;
import bku.iot.iotdemo.fragments.realtime;

public class MyViewPageAdapter extends FragmentStateAdapter {
    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new realtime();
            case 1:
                return new dashboard();
            case 2:
                return new notification();
            case 3:
                return new automation();
            default:
                return new realtime();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
