

package com.example.virtualdarts2;

        import android.os.Build;
        import android.os.Bundle;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.util.UUID;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.util.Log;
        import android.content.Intent;
        import android.os.Handler;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.bluetooth.BluetoothManager;
        import android.content.Context;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Set;
        import android.app.Activity;

        import androidx.annotation.RequiresApi;

        import java.io.InputStream;

public class TestActivity extends Activity {

    //sudo python3 FILENAME

    BluetoothSocket socket;
    BluetoothDevice bt_device = null;

    final byte delimiter = 35;
    int readBufferPosition = 0;

    private final static int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter bluetoothAdapter;

    private Button turnOnLED;
    private Button turnOffLED;
    private Button getTemperature;
    private TextView status;
    //List<BiometricManager.Strings> PiMessageBuffer;

    // additions
    ImageButton backBtn;
    String user_ID;

    private Set<BluetoothDevice> pairedDevices;
    List<BluetoothDevice> PairedDevices;

    public void sendMessage(String send_message) {

        //UUID uuid = UUID.fromString("ae14f5e2-9eb6-4015-8457-824d76384ba0");
        UUID uuid = UUID.fromString("256bfb09-7e59-4453-99cf-f281e0fd0f5c");

        try {

            socket = bt_device.createRfcommSocketToServiceRecord(uuid);
            if (!socket.isConnected()) {
                socket.connect();
            }

            String message = send_message;

            OutputStream btOutputStream = socket.getOutputStream();
            btOutputStream.write(message.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Android Bluetooth Raspberry Pi Demo");

        setContentView(R.layout.activity_test);

        turnOnLED = (Button) findViewById(R.id.turn_on);
        turnOffLED = (Button) findViewById(R.id.turn_off);
        getTemperature = (Button) findViewById(R.id.get_temperature);
        status = (TextView) findViewById(R.id.status);
        backBtn = (ImageButton) findViewById(R.id.btnHomeBluetooth2);

        final Handler handler = new Handler();

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();

        pairedDevices = bluetoothAdapter.getBondedDevices();
        PairedDevices = new ArrayList<>(pairedDevices);

        for (int i = 0; i < PairedDevices.size(); i++) {

            String name = PairedDevices.get(i).getName();

            //The name in quotes must match your device
            String deviceName = "raspberrypi"; // change this depending on the name of the pi youre using
            if(name.equals(deviceName))
            {
                bt_device = PairedDevices.get(i);
                break;
            }
        }

        //Check if Bluetooth is enabled
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        final class workerThread implements Runnable {

            private String message;

            public workerThread(String msg) {
                message = msg;
            }

            public void run() {

                sendMessage(message);

                while(!Thread.currentThread().isInterrupted()) {

                    int bytesAvailable;
                    boolean workDone = false;

                    try {

                        final InputStream inputStream;
                        inputStream = socket.getInputStream();
                        bytesAvailable = inputStream.available();
                        if(bytesAvailable > 0)
                        {

                            byte[] packetBytes = new byte[bytesAvailable];
                            Log.e("Bytes received from", "Raspberry Pi");
                            byte[] readBuffer = new byte[1024];
                            inputStream.read(packetBytes);

                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            status.setText(data);
                                        }
                                    });

                                    workDone = true;
                                    break;
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }

                            if (workDone == true){
                                socket.close();
                                break;
                            }

                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }

        //Turn On LED Listener
        turnOnLED.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                (new Thread(new workerThread("turn_on_LED"))).start();

            }
        });

        //Turn Off LED Listener
        turnOffLED.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                (new Thread(new workerThread("turn_off_LED"))).start();

            }
        });

        //Get Temperature Listener
        getTemperature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                (new Thread(new workerThread("getTemperature"))).start();

            }
        });

        // user_ID from other activity
        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";


        // takes you to home page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });

    }

}