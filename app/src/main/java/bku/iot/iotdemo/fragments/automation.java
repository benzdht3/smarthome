package bku.iot.iotdemo.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bku.iot.iotdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link automation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class automation extends Fragment {
    private ListView taskListView;
    private Spinner deviceSpinner;
    private Spinner onOffSpinner;
    private TextView timeTextView, thisDay;
    private Button addTaskButton, changeDateButton;
    private ArrayAdapter<String> adapter;
    private Map<String, List<String>> tasksMap = new HashMap<>();
    private String selectedTime;
    private int taskdate, taskmonth, taskyear;
    static List<String> tasklist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_automation, container, false);
        taskListView = view.findViewById(R.id.taskListView);
        deviceSpinner = view.findViewById(R.id.deviceSpinner);
        onOffSpinner = view.findViewById(R.id.onOffSpinner);
        timeTextView = view.findViewById(R.id.timeTextView);
        thisDay = view.findViewById(R.id.thisDay);
        addTaskButton = view.findViewById(R.id.addTaskButton);
        changeDateButton = view.findViewById(R.id.changeDateButton);
        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        taskListView.setAdapter(adapter);
        String[] devices = {"Light Bulb", "Fan", "Door"};
        deviceSpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, devices));

        String[] onOffOptions = {"On", "Off"};
        onOffSpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, onOffOptions));

        timeTextView.setOnClickListener(viewvar -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), (view1, selectedHour, selectedMinute) -> {
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                timeTextView.setText(selectedTime);
            }, hour, minute, true);

            timePickerDialog.show();
        });

        changeDateButton.setOnClickListener(viewvar -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), (view1, year, month, dayOfMonth) -> {
                taskdate = dayOfMonth;
                taskyear = year;
                taskmonth = month + 1;
                String tday = taskyear + "-" + taskmonth + "-" + taskdate;
                thisDay.setText(tday);
                updateTaskList(taskyear, taskmonth-1, taskdate);

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
            timeTextView.setText(getString(R.string.select_time));
        });

        addTaskButton.setOnClickListener(viewvar -> {
            Log.d("day",thisDay.getText().toString());
            if (thisDay.getText().toString()=="No Date Selected") {
                Toast.makeText(this.getContext(), "Please select a date first", Toast.LENGTH_SHORT).show();
                return;
            }
            String device = deviceSpinner.getSelectedItem().toString();
            String onOff = onOffSpinner.getSelectedItem().toString();
            String time = selectedTime != null ? selectedTime : "00:00";
            String date = taskyear + "-" + taskmonth + "-" + taskdate;
            String fullTask = date+" : " + device + " - " + onOff + " - " + time;
            if (!tasksMap.containsKey(date)) {
                tasksMap.put(date, new ArrayList<>());
            }
            tasksMap.get(date).add(fullTask);
            updateTaskList(taskyear, taskmonth-1, taskdate);
            timeTextView.setText(getString(R.string.select_time));
            selectedTime = null;
        });

        return view;
    }

    private void updateTaskList(int year, int month, int day) {
        String date = year + "-" + (month + 1) + "-" + day;
        List<String> tasks;
        if (tasksMap.containsKey(date)) {
            tasks = tasksMap.get(date);
        } else {
            tasks = new ArrayList<>();
        }
        if(!tasks.isEmpty()){
            for(int i=0;i<tasks.size();i++){
                if(!tasklist.contains(tasks.get(i))){
                    tasklist.add(tasks.get(i));
                }
            }
        }
        adapter.clear();
        adapter.addAll(tasks);
        adapter.notifyDataSetChanged();
    }

    public static List<String> getTask(){return tasklist;}
}