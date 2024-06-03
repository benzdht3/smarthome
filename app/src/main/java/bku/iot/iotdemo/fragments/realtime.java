package bku.iot.iotdemo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import bku.iot.iotdemo.MQTTHelper;
import bku.iot.iotdemo.R;

public class realtime extends Fragment {
    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid, txtLight, txtLog, txtMotion;
    LinearLayout lay;
    LabeledSwitch btnLed, btnFan, btnDoor;
    String log = "";
    Boolean ack = false;
    String btn;
    static List<Float> temp = new ArrayList<>();
    boolean payload;
    boolean alert = true;
    public String gettimestamp(long timestamp){
        return String.valueOf(timestamp);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_realtime, container, false);
        lay = view.findViewById(R.id.lay);
        txtTemp = view.findViewById(R.id.txtTemp);
        txtHumid = view.findViewById(R.id.txtHumid);
        txtLight = view.findViewById(R.id.txtLight);
        txtMotion = view.findViewById(R.id.txtMotion);
        txtLog = view.findViewById(R.id.txtLog);
        btnLed = view.findViewById(R.id.btnBulb);
        btnFan = view.findViewById(R.id.btnFan);
        btnDoor = view.findViewById(R.id.btnDoor);
        startMQTT();

            btnLed.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    btn = "led";
                    if (btnLed.isOn()) {
                        sendDataMQTT("benzdht/feeds/button1", "0");
                        payload = false;
                        checkAck(btnLed, false);
                    } else {
                        sendDataMQTT("benzdht/feeds/button1", "1");
                        payload = true;
                        checkAck(btnLed, true);
                    }
                }
            });

            btnFan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn = "pump";
                    if (btnFan.isOn()) {
                        sendDataMQTT("benzdht/feeds/button2", "0");
                        payload = false;
                        checkAck(btnFan, false);
                    } else {
                        sendDataMQTT("benzdht/feeds/button2", "1");
                        payload = true;
                        checkAck(btnFan, true);
                    }

                }
            });

        btnDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn = "pump";
                if (btnDoor.isOn()) {
                    sendDataMQTT("benzdht/feeds/button2", "0");
                    payload = false;
                    checkAck(btnDoor, false);
                } else {
                    sendDataMQTT("benzdht/feeds/button2", "1");
                    payload = true;
                    checkAck(btnDoor, true);
                }

            }
        });

        return view;
        }

        public static List<Float> getTempArr () {
            return temp;
        }

        public void checkAck (LabeledSwitch btn,boolean purpose){
            if (!ack) {
                btn.setOn(purpose);
                if (alert) {
                    AlertDialog dialog = createFailDialog(requireContext());
                    dialog.show();
                }
            }
        }

        public void sendDataMQTT (String topic, String value){
            MqttMessage msg = new MqttMessage();
            msg.setId(1234);
            msg.setQos(0);
            msg.setRetained(false);

            byte[] b = value.getBytes(StandardCharsets.UTF_8);
            msg.setPayload(b);

            try {
                mqttHelper.mqttAndroidClient.publish(topic, msg);
            } catch (MqttException e) {
            }
        }

        public void startMQTT () {
            mqttHelper = new MQTTHelper(requireContext());
            mqttHelper.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    sendDataMQTT("benzdht/feeds/Ack", "0.5");
                }

                @Override
                public void connectionLost(Throwable cause) {
                    alert = true;
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d("TEST", topic + " " + message.toString());
                    if (topic.contains("sensor1")) {
                        txtTemp.setText(message.toString() + "°C");
                        temp.add(Float.parseFloat(message.toString()));
                        log += gettimestamp(System.currentTimeMillis()) + ": Temperature is changed to " + message.toString() + "°C" + "\n";
                        txtLog.setText(log);
                    } else if (topic.contains("sensor3")) {
                        txtHumid.setText(message.toString() + "%");
                        log += gettimestamp(System.currentTimeMillis()) + ": Humidity is changed to " + message.toString() + "%" + "\n";
                        txtLog.setText(log);
                    } else if (topic.contains("sensor2")) {
                        txtLight.setText(message.toString() + "lux");
                        log += gettimestamp(System.currentTimeMillis()) + ": Light is changed to " + message.toString() + "lux" + "\n";
                        txtLog.setText(log);
                    } else if (topic.contains("motion")) {
                        txtLight.setText(message.toString() );
                        log += gettimestamp(System.currentTimeMillis()) + ": Motion is changed to " + message.toString() + "\n";
                        txtLog.setText(log);
                    }  else if (topic.contains("Ack")) {
                        if (message.toString().equals("1")) {
                            alert = false;
                            if (btn == "led") {
                                btnLed.setOn(payload);
                                if (payload) {
                                    log += gettimestamp(System.currentTimeMillis()) + ": Light bulb is turned ON\n";
                                } else {
                                    log += gettimestamp(System.currentTimeMillis()) + ": Light bulb is turned OFF\n";
                                }
                                txtLog.setText(log);
                            } else if (btn == "fan") {
                                btnFan.setOn(payload);
                                if (payload) {
                                    log += gettimestamp(System.currentTimeMillis()) + ": Fan is turned ON\n";
                                } else {
                                    log += gettimestamp(System.currentTimeMillis()) + ": Fan is turned OFF\n";
                                }
                                txtLog.setText(log);
                            }else if (btn == "door") {
                                btnFan.setOn(payload);
                                if (payload) {
                                    log += gettimestamp(System.currentTimeMillis()) + ": Door is locked\n";
                                } else {
                                    log += gettimestamp(System.currentTimeMillis()) + ": Fan is unlocked\n";
                                }
                                txtLog.setText(log);
                            }
                        } else if (message.toString().equals("2")) {
                            alert = true;
                        } else if (message.toString().equals("0")) {
                            alert = false;
                        }
                    } else if (topic.contains("button1")) {
                        btn = "led";
                        if (message.toString().equals("1")) {
                            payload = true;
                        } else {
                            payload = false;
                        }
                        alert = true;
                    } else if (topic.contains("button2")) {
                        btn = "fan";
                        if (message.toString().equals("1")) {
                            payload = true;
                        } else {
                            payload = false;
                        }
                        alert = true;
                    }else if (topic.contains("door")) {
                        btn = "door";
                        if (message.toString().equals("1")) {
                            payload = true;
                        } else {
                            payload = false;
                        }
                        alert = true;
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }

        AlertDialog createFailDialog (Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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