package com.example.virtualdarts2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    Button btnEnable, btnReceive, btnHome;
    TextView fileTV;
    EditText fileToReadET;
    String user_ID;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    BluetoothDevice bluetoothDevice;

    RecyclerView recyclerView;
    BluetoothViewAdapter adapter;
    ArrayList<String> deviceNames;

    private LinearLayoutManager linearLayoutManager;

    DBGame dbGame;
    DBStats dbStats;

    private int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // user_ID from other activity
        user_ID = getIntent().getStringExtra("current_user");
        if (user_ID == null)
            user_ID = "user_ID";

        // initialize variables
        btnHome = (Button) findViewById(R.id.btnHomeBluetooth);
        btnEnable = (Button) findViewById(R.id.btnEnableBluetooth);
        btnReceive = (Button) findViewById(R.id.btnReceiveData);
        fileTV = (TextView) findViewById(R.id.enterFileNameTV);
        fileToReadET = (EditText) findViewById(R.id.fileNameET);
        dbGame = new DBGame(this);
        dbStats = new DBStats(this);

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
        for(BluetoothDevice device : pairedDevices) deviceNames.add(device.getName());

        // recycler view setup
        recyclerView = (RecyclerView) findViewById(R.id.rvBluetooth);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new BluetoothViewAdapter(this, deviceNames);
        adapter.setClickListener(new BluetoothViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*Toast.makeText(getApplicationContext(), "You clicked " + adapter.getItem(position) + " on row number "
                        + position, Toast.LENGTH_SHORT).show();*/
                String chosenDevice = adapter.getItem(position);
                String connectedToString = "Connected to: " + chosenDevice;
                //connectedDevice.setText(connectedToString);
                for(BluetoothDevice device : pairedDevices) {
                    if(device.getName().equals(chosenDevice)) {
                        bluetoothDevice = device;
                    }
                }

            }
        });

        // spaces?
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager); // ?????
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        // whatever this is
        ActivityResultLauncher<Intent> BluetoothActivityResultLauncher = registerForActivityResult(
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
                    BluetoothActivityResultLauncher.launch(enable);
                }
                Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                BluetoothActivityResultLauncher.launch(getVisible);
                Toast.makeText(getApplicationContext(), "Bluetooth enabled" ,
                        Toast.LENGTH_SHORT).show();

            }
        });

        // does file thing
        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verifyStoragePermissions(view);
                String data = accessPermission(view);
                /*if (data != null) {
                    Toast.makeText(getApplicationContext(), "Statistics updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to update statistics", Toast.LENGTH_SHORT).show();
                }*/

                /*Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                intent.putExtra("file_data", data);
                startActivity(intent);*/

            }

        });
    }

    public void verifyStoragePermissions(View view) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public String accessPermission(View view) {
        // Requesting Permission to access External Storage
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);
        //verifyStoragePermissions(view);

        // getExternalStoragePublicDirectory() represents root of external storage, we are using DOWNLOADS
        // We can use following directories: MUSIC, PODCASTS, ALARMS, RINGTONES, NOTIFICATIONS, PICTURES, MOVIES
        // Accessing the saved data from the downloads folder
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(folder, String.valueOf(fileToReadET.getText()));
        String data = readAndUpdate(file);
        return data;
    }

    // reads file
    private String readFile(File myFile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(myFile);
            int i = -1;
            StringBuffer buffer = new StringBuffer();
            while ((i = fileInputStream.read()) != -1) {
                buffer.append((char) i);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // reads file and updates game table
    public String readAndUpdate(File myFile) {
        String readData = "";
        try {
            Scanner myReader = new Scanner(myFile);
            int i = 0;
            int j = 0;
            while (myReader.hasNextLine()) {
                i++;
                String data = myReader.nextLine();
                /*Log.d("TAG_GAME_ENTRY", gameEntry.getUser_ID_1());*/

                // insert game entry into the database
                Boolean inserted = dbGame.insertGameEntry(makeGameEntry(data));

                if(inserted) {
                    j++;

                }

            }
            if(i == j) { // proper number of lines was inserted
                Toast.makeText(getApplicationContext(), "Game page updated!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Unable to update game page", Toast.LENGTH_SHORT).show();
            }
            myReader.close();
            readData = "lol";

        } catch (FileNotFoundException e) {
            Log.e("TAG_READ_FILE", "Error reading file", e);
        }
        if(readData.equals("")) {
            return null;
        }
        else return readData;
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