package bku.iot.iotdemo;

import static bku.iot.iotdemo.Chart.getData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Chart lchart;
    ToggleButton real,chart;
    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid, txtLight,txtLog;
    LabeledSwitch btnLed,btnPump;
    String log="";
    Boolean ack=false;
    String btn;
    static List<Float> temp = new ArrayList<>();
    boolean payload;
    boolean alert = true;
    public String gettimestamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }
    public boolean popFragment() {
        boolean isPop = false;
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getSupportFragmentManager().popBackStackImmediate();
        }
        return isPop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        real=findViewById(R.id.real);
        chart=findViewById(R.id.chart);
        real.setChecked(true);
        chart.setChecked(false);
        real.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(real.isChecked()){
                    chart.setChecked(false);
                }else{
                    real.setChecked(true);
                }
                if (!popFragment()) {
                    finish();
                }
            }
        });
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chart.isChecked()){
                    real.setChecked(false);
                }else{
                    chart.setChecked(true);
                }
                Fragment fragment = new Chart();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.change, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
        });
        txtTemp=findViewById(R.id.txtTemp);
        txtHumid=findViewById(R.id.txtHumid);
        txtLight=findViewById(R.id.txtLight);
        txtLog=findViewById(R.id.txtLog);
        btnLed=findViewById(R.id.btnBulb);
        btnPump=findViewById(R.id.btnPump);
        startMQTT();
        btnLed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                btn="led";
                if(btnLed.isOn()){
                    sendDataMQTT("benzdht/feeds/button1","0");
                    payload=false;
                    checkAck(btnLed,false);
                }else{
                    sendDataMQTT("benzdht/feeds/button1", "1");
                    payload=true;
                    checkAck(btnLed,true);
                }
            }
        });
        btnPump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn="pump";
                if (btnPump.isOn()){
                    sendDataMQTT("benzdht/feeds/button2","0");
                    payload=false;
                    checkAck(btnPump,false);
                }else{
                    sendDataMQTT("benzdht/feeds/button2", "1");
                    payload=true;
                    checkAck(btnPump,true);
                }

            }
        });
    }

    public static List<Float> getTempArr(){
        return temp;
    }

    public void checkAck(LabeledSwitch btn,boolean purpose){
        if(!ack){
            btn.setOn(purpose);
            if(alert){
                AlertDialog dialog = createFailDialog();
                dialog.show();
            }
        }
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(StandardCharsets.UTF_8);
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                sendDataMQTT("benzdht/feeds/Ack","0.5");
            }

            @Override
            public void connectionLost(Throwable cause) {
                alert = true;
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST",topic + " " + message.toString());
                if(topic.contains("sensor1")){
                    txtTemp.setText(message.toString() + "°C");
                    temp.add(Float.parseFloat(message.toString()));
                    log+=gettimestamp()+": Temperature is changed to "+message.toString()+ "°C" +"\n";
                    txtLog.setText(log);
                    getData();
                }
                else if(topic.contains("sensor3")){
                    txtHumid.setText(message.toString() + "%");
                    log+=gettimestamp()+": Humidity is changed to "+message.toString()+ "%" +"\n";
                    txtLog.setText(log);
                }
                else if(topic.contains("sensor2")){
                    txtLight.setText(message.toString() + "lux");
                    log+=gettimestamp()+": Light is changed to "+message.toString()+ "lux" +"\n";
                    txtLog.setText(log);
                }else if(topic.contains("Ack")){
                    if (message.toString().equals("1")) {
                        alert = false;
                        if(btn=="led"){
                            btnLed.setOn(payload);
                            if(payload){
                                log+=gettimestamp()+": Light bulb is turned ON\n";
                            }
                            else{
                                log+=gettimestamp()+": Light bulb is turned OFF\n";
                            }
                            txtLog.setText(log);
                        }
                        else if(btn=="pump"){
                            btnPump.setOn(payload);
                            if(payload){
                                log += gettimestamp() + ": Pump is turned ON\n";
                            }
                            else{
                                log += gettimestamp() + ": Pump is turned OFF\n";
                            }
                            txtLog.setText(log);
                        }
                    }else if(message.toString().equals("2")){
                        alert=true;
                    }else if(message.toString().equals("0")){
                        alert=false;
                    }
                }else if(topic.contains("button1")) {
                    btn="led";
                    if(message.toString().equals("1")){
                        payload = true;
                    }else{
                        payload = false;
                    }
                    alert=true;
                }else if(topic.contains("button2")) {
                    btn="pump";
                    if(message.toString().equals("1")){
                        payload = true;
                    }else{
                        payload = false;
                    }
                    alert=true;
                }
                /*else if(topic.contains("button1")) {
                    if(message.toString().equals("1")){
                        btnLed.setOn(true);
                        log+=gettimestamp()+": Light bulb is turned ON\n";
                    }else{
                        btnLed.setOn(false);
                        log+=gettimestamp()+": Light bulb is turned OFF\n";
                    }
                    txtLog.setText(log);
                }
                else if(topic.contains("button2")) {
                    if (message.toString().equals("1")) {
                        btnPump.setOn(true);
                        log += gettimestamp() + ": Pump is turned ON\n";
                    } else {
                        btnPump.setOn(false);
                        log += gettimestamp() + ": Pump is turned OFF\n";
                    }
                    txtLog.setText(log);
                }*/
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    AlertDialog createFailDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connection Error");
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}