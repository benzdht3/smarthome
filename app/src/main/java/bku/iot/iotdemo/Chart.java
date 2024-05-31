package bku.iot.iotdemo;

import static bku.iot.iotdemo.MainActivity.getTempArr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class Chart extends Fragment {
    MQTTHelper mqttHelper;
    static LineChart tempchart,humidchart,lightchart;
    LineData t,h,l;
    LineDataSet tset;
    static MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
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
        tempyAxis.setAxisLineWidth(2f);
        tempyAxis.setLabelCount(10);
        tempyAxis.setGranularity(1f);

        Description humdes= new Description();
        humdes.setText("Humidity History");
        humidchart.setDescription(humdes);
        XAxis humxAxis = humidchart.getXAxis();
        humxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Description ligdes= new Description();
        ligdes.setText("Light History");
        lightchart.setDescription(ligdes);
        XAxis ligxAxis = lightchart.getXAxis();
        ligxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        tempchart.invalidate();
        return view;
    }
    public static void getData(){
        List<Float> temp = getTempArr();
        LineData data= new LineData();
        tempchart.setData(data);
        /*if(temp.size()>0) {
                data = new LineData();
                for (int i = 0; i < temp.size(); i++) {
                    data.addEntry(new Entry(i,temp.get(i)),0);
                }
                tempchart.setData(data);
        }else{
            Log.d("TEST","null");
        }*/

        //.d("TEST","getdata");
    }

    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null,"Dynamic data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(2f);
        return set;
    }
}