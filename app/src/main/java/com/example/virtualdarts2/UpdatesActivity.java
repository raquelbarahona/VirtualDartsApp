package com.example.virtualdarts2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class UpdatesActivity extends AppCompatActivity {
    Button btnEnable, btnRefresh, btnUpdateStats, btnUpdateLDB;
    ImageButton btnHome;
    TextView dataReceivedTV;

    String user_ID;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    BluetoothDevice bluetoothDevice;

    BluetoothSocket socket;
    final byte delimiter = 35; // this is a #
    int readBufferPosition = 0;

    RecyclerView recyclerView;
    BluetoothViewAdapter adapter;
    ArrayList<String> deviceNames;

    private LinearLayoutManager linearLayoutManager;

    DBGame dbGame;
    DBStats dbStats;

    String chosenDevice = "nothing"; // ?
    Boolean dontCrash;

    public void sendMessage(String send_message) {

        //UUID uuid = UUID.fromString("ae14f5e2-9eb6-4015-8457-824d76384ba0");
        UUID uuid = UUID.fromString("256bfb09-7e59-4453-99cf-f281e0fd0f5c");

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            if (!socket.isConnected()) {
                socket.connect();
            }

            // String message = send_message;

            OutputStream btOutputStream = socket.getOutputStream();
            btOutputStream.write(send_message.getBytes());

        } catch (IOException e) {
            dontCrash = false; // will be used to check whether data can be received
            e.printStackTrace();

        }
    }

    final class workerThread implements Runnable {
        final Handler handler = new Handler();

        private String message;

        public workerThread(String msg) {
            message = msg;
        }

        String insertionStatus = " "; // result of insertion

        public void run() {
                /*if(dontCrash) {

                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot connect. Please try again or select another device.",
                            Toast.LENGTH_SHORT).show();
                    dataReceivedTV.setText("N/A");
                }*/
            sendMessage(message); // send the message if pi is available
            while (!Thread.currentThread().isInterrupted()) {

                int bytesAvailable;
                boolean workDone = false;

                try {
                    final InputStream inputStream;
                    inputStream = socket.getInputStream();
                    bytesAvailable = inputStream.available();
                    if (bytesAvailable > 0) {

                        byte[] packetBytes = new byte[bytesAvailable];
                        Log.e("Bytes received from", "Raspberry Pi");
                        byte[] readBuffer = new byte[1024];
                        inputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            if (b == delimiter) {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, "US-ASCII");
                                readBufferPosition = 0;

                                handler.post(new Runnable() {
                                    public void run() {
                                        if (!data.equals("stop")) {
                                            dataReceivedTV.setText(data);
                                            insertionStatus = readAndUpdateDB(data);
                                        } else {
                                            //dataReceivedTV.setText("Data");
                                        }

                                    }
                                });

                                workDone = true;
                                break;
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }

                        if (workDone) {
                            socket.close();
                            break;
                        }

                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Cannot connect. Please try again or select another device.",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            // send insertion status to pi
            //(new Thread(new workerThread(insertionStatus))).start();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);

        dontCrash = true;

        // user_ID from other activity
        user_ID = getIntent().getStringExtra("current_user");
        if (user_ID == null)
            user_ID = "user_ID";

        // handler ?
        //final Handler handler = new Handler();

        // initialize variables
        btnHome = (ImageButton) findViewById(R.id.btnHomeUP);
        btnEnable = (Button) findViewById(R.id.btnEnableBluetoothUP);
        btnRefresh = (Button) findViewById(R.id.btnRefreshUP);
        btnUpdateStats = (Button) findViewById(R.id.btnUpdateStats);
        //btnUpdateLDB = (Button) findViewById(R.id.btnUpdateLDB);
        //btnUpdateLDB.setVisibility(View.INVISIBLE);

        dataReceivedTV = (TextView) findViewById(R.id.dataReceivedTV);

        dbGame = new DBGame(this);
        dbStats = new DBStats(this);

        // bluetooth
        // names of bluetooth devices
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        pairedDevices = bluetoothAdapter.getBondedDevices();
        deviceNames = new ArrayList();
        for (BluetoothDevice device : pairedDevices) deviceNames.add(device.getName());
        Log.e("NUMBER OF DEVICES", Integer.toString(deviceNames.size()));

        // recycler view setup
        // TODO recyclerview test
        String list = "";
        for (BluetoothDevice device : pairedDevices) {
            list = list + device.getName() + " ";
        }
        Log.e("Devices", list);


        recyclerView = (RecyclerView) findViewById(R.id.rvPairedDevices);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new BluetoothViewAdapter(this, deviceNames);
        adapter.setClickListener(new BluetoothViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                chosenDevice = adapter.getItem(position);
                String connectedToString = "Device selected: " + chosenDevice;
                Toast.makeText(getApplicationContext(), connectedToString,
                        Toast.LENGTH_SHORT).show();
                //connectedDevice.setText(connectedToString);
                // chosen device should be "raspberrypi"

                // FOR DEBUGGING
                //chosenDevice = "raspberrypi";

                for (BluetoothDevice device : pairedDevices) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (device.getName().equals(chosenDevice)) {
                        bluetoothDevice = device;
                    }
                }
            }
        });

        // finalize recycler view
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        // result launcher, am not sure what should go inside if anything
        // whatever this is
        ActivityResultLauncher<Intent> UpdatesActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                        }
                    }
                });





        // receive stats update
        btnUpdateStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenDevice.equals("nothing")) {
                    Toast.makeText(getApplicationContext(), "Select a device before connecting",
                            Toast.LENGTH_SHORT).show();
                }
                else if(chosenDevice.equals("raspberrypi")) { // only connect with the raspberrypi
                    (new Thread(new workerThread("get stats updates"))).start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot communicate with selected device. Select another device",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // receive stats update
        /*btnUpdateLDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenDevice.equals("raspberrypi")) { // only connect with the raspberrypi
                    (new Thread(new workerThread("get LDB updates"))).start();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot communicate with selected device. Select another device",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });**/

        // BUTTONS
        // takes you to home page
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });

        // enables bluetooth and makes it visible
        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bluetoothAdapter.isEnabled()) {
                    Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    UpdatesActivityResultLauncher.launch(enable);
                }
                Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                UpdatesActivityResultLauncher.launch(getVisible);

            }
        });

        // refreshes after enabling bluetooth
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO RECYCLERVIEW
                recyclerView.getRecycledViewPool().clear();
                Intent intent = new Intent(getApplicationContext(), UpdatesActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });

    }

    // reads file and updates game table
    public String readAndUpdateDB(String dataReceived) {
        String readData = "";
        Scanner myReader = new Scanner(dataReceived);
        int i = 0;
        int j = 0;

        String statsOrLDBmsg = "";

        if(dataReceived.equals("UPDATE ALREADY RECEIVED")) {
            Toast.makeText(getApplicationContext(), "There are no new updates available", Toast.LENGTH_SHORT).show();
            statsOrLDBmsg = "no action needed";
            (new Thread(new workerThread(statsOrLDBmsg))).start();
            String str = "Statistics already updated"; // TODO edit
            dataReceivedTV.setText(str);
            return statsOrLDBmsg;
        }
        else {
            while (myReader.hasNextLine()) {
                i++;
                String data = myReader.nextLine();
                String statsOrLDB = String.valueOf(data.charAt(0)) + String.valueOf(data.charAt(1));

                // insert game entry into the database
                Boolean inserted;
                if (statsOrLDB.equals("1,") || statsOrLDB.equals("0,")) {
                    inserted = dbGame.insertGameEntry(makeGameEntry(data));
                    statsOrLDBmsg = "stats";
                } else {
                    // insert into leaderboard table
                    inserted = true; // TODO edit when leaderboard table is complete
                    statsOrLDBmsg = "LDB";
                }

                if (inserted) {
                    j++;
                }

            }

            if (i == j) { // proper number of lines was inserted
                if(statsOrLDBmsg.equals("stats")) {
                    Toast.makeText(getApplicationContext(), "Statistics updated", Toast.LENGTH_SHORT).show();
                    readData = "stats inserted";
                }
                else if(statsOrLDBmsg.equals("LDB")) {
                    Toast.makeText(getApplicationContext(), "Leaderboard updated", Toast.LENGTH_SHORT).show();
                    readData = "LDB inserted";
                }
                else {
                    Toast.makeText(getApplicationContext(), "There are no new updates", Toast.LENGTH_SHORT).show();
                    readData = "no action needed";
                }

            } else { // insertion failure
                if(statsOrLDBmsg.equals("stats")) {
                    Toast.makeText(getApplicationContext(), "Statistics failed to update", Toast.LENGTH_SHORT).show();
                    readData = "stats NOT inserted";
                }
                else if(statsOrLDBmsg.equals("LDB")) {
                    Toast.makeText(getApplicationContext(), "Leaderboard failed to update", Toast.LENGTH_SHORT).show();
                    readData = "LDB NOT inserted";
                }
                else {
                    Toast.makeText(getApplicationContext(), "There are no new updates", Toast.LENGTH_SHORT).show();
                    readData = "no action needed";
                }
            }

            myReader.close();

            (new Thread(new workerThread(readData))).start();
            return readData;
        }
    }

    public GameEntry makeGameEntry(String line) {
        String [] gameData = line.split(",");

        // don't assume the line is the right size--it might not be and that will break the code!
        if(gameData.length < 8) {
            String [] newArray = {"0", "0", "0", "0", "0", "0", "0", "0"};
            for(int i = 0; i < gameData.length; i++) {
                newArray[i] = gameData[i];
            }
            gameData = newArray;
        }

        GameEntry gameEntry;
        if(gameData[0].equals("0")) { // not a match!
            gameEntry = new GameEntry(0, gameData[1], "", gameData[3],
                    Float.valueOf(gameData[4]), (float) -1.0, gameData[6], gameData[7]);
        }
        else {
            gameEntry = new GameEntry(1, gameData[1], gameData[2], gameData[3],
                    Float.valueOf(gameData[4]), Float.valueOf(gameData[5]), gameData[6], gameData[7]);
        }
        return gameEntry;

    }

}