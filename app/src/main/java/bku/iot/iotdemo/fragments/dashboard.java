package bku.iot.iotdemo.fragments;

import static bku.iot.iotdemo.fragments.realtime.getHumArr;
import static bku.iot.iotdemo.fragments.realtime.getHumTime;
import static bku.iot.iotdemo.fragments.realtime.getLigArr;
import static bku.iot.iotdemo.fragments.realtime.getLigTime;
import static bku.iot.iotdemo.fragments.realtime.getTempArr;
import static bku.iot.iotdemo.fragments.realtime.getTempTime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import bku.iot.iotdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboard extends Fragment {
    LineChart tempchart,humidchart,lightchart;
    List<Float> temp;
    List<String> temptime;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        tempchart=view.findViewById(R.id.tempchart);
        humidchart=view.findViewById(R.id.humidchart);
        lightchart=view.findViewById(R.id.lightchart);

        Description tempdes= new Description();
        tempdes.setText("Temperature History");
        tempchart.setDescription(tempdes);
        tempchart.getDescription().setEnabled(true);
        XAxis tempxAxis = tempchart.getXAxis();
        tempxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis tempyAxis = tempchart.getAxisLeft();
        tempyAxis.setAxisMinimum(15f);
        tempyAxis.setAxisMaximum(60f);
        List<Float> temp = getTempArr();
        List<String> temptime = getTempTime();
        tempxAxis.setValueFormatter(new IndexAxisValueFormatter(temptime));
        tempxAxis.setLabelCount(temp.size());
        tempyAxis.setLabelCount(temptime.size());
        List<Entry> entriest= new ArrayList<>();
        for(int i=0;i<temp.size();i++){
            entriest.add(new Entry(i,temp.get(i)));
        }
        LineDataSet tdataSet=new LineDataSet(entriest,"Temperature");
        LineData tlineData = new LineData(tdataSet);
        tempchart.setData(tlineData);

        Description humdes= new Description();
        humdes.setText("Humidity History");
        humidchart.setDescription(humdes);
        humidchart.getDescription().setEnabled(true);
        XAxis humxAxis = humidchart.getXAxis();
        humxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis humyAxis = humidchart.getAxisLeft();
        humyAxis.setAxisMinimum(0f);
        humyAxis.setAxisMaximum(100f);
        List<Float> hum = getHumArr();
        List<String> humtime = getHumTime();
        humxAxis.setValueFormatter(new IndexAxisValueFormatter(humtime));
        humxAxis.setLabelCount(hum.size());
        humyAxis.setLabelCount(humtime.size());
        List<Entry> entriesh= new ArrayList<>();
        for(int i=0;i<hum.size();i++){
            entriesh.add(new Entry(i,hum.get(i)));
        }
        LineDataSet hdataSet=new LineDataSet(entriesh,"Humidity");
        LineData hlineData = new LineData(hdataSet);
        humidchart.setData(hlineData);

        Description ligdes= new Description();
        ligdes.setText("Light History");
        lightchart.setDescription(ligdes);
        lightchart.getDescription().setEnabled(true);
        XAxis ligxAxis = lightchart.getXAxis();
        ligxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis ligyAxis = lightchart.getAxisLeft();
        ligyAxis.setAxisMinimum(0f);
        ligyAxis.setAxisMaximum(500f);
        List<Float> lig = getLigArr();
        List<String> ligtime = getLigTime();
        ligxAxis.setValueFormatter(new IndexAxisValueFormatter(ligtime));
        ligxAxis.setLabelCount(lig.size());
        ligyAxis.setLabelCount(ligtime.size());
        List<Entry> entriesl= new ArrayList<>();
        for(int i=0;i<lig.size();i++){
            entriesl.add(new Entry(i,lig.get(i)));
        }
        LineDataSet ldataSet=new LineDataSet(entriesl,"Light");
        LineData llineData = new LineData(ldataSet);
        lightchart.setData(llineData);

        tempchart.invalidate();
        humidchart.invalidate();
        lightchart.invalidate();
        return view;
    }
}