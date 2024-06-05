package bku.iot.iotdemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import bku.iot.iotdemo.fragments.automation;
import bku.iot.iotdemo.fragments.dashboard;
import bku.iot.iotdemo.fragments.notification;
import bku.iot.iotdemo.fragments.realtime;

public class MyViewPageAdapter extends FragmentStateAdapter {
    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentList.add(new realtime());
        fragmentList.add(new dashboard());
        fragmentList.add(new notification());
        fragmentList.add(new automation());
    }
    private final List<Fragment> fragmentList = new ArrayList<>();

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    public void refreshFragment(int position) {
        // Replace the fragment at the specified position
        fragmentList.set(position, createNewFragment(position));
        notifyItemChanged(position);
    }

    public Fragment createNewFragment(int position) {
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
        return fragmentList.size();
    }
}
