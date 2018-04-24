package com.clarkson.connect.clarksonconnect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.DialogFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
//audio chats imports

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;


import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button ptt, callFood, callCampo;
    private RadioButton channelOne, channelTwo, channelThree, globalChannel;
    private TextView channelSelect;

    //audiochatz objects

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;

    //chatz objects

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    static final int MESSAGE_READ = 1;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }




    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers))
            {
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray=new String[peerList.getDeviceList().size()];
                deviceArray=new WifiP2pDevice[peerList.getDeviceList().size()];

                int index=0;

                for(WifiP2pDevice device : peerList.getDeviceList())
                {
                    deviceNameArray[index]=device.deviceName;
                    deviceArray[index]=device;
                    index++;

                }

                //ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                //listView.setAdapter(adapter);

            }

            if(peers.size()==0)
                Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_SHORT).show();
            return;
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;
            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner)
            {
                //ConnectionStatus.setText("HOST");
                serverClass=new ServerClass();
                serverClass.start();

            }
            else if(wifiP2pInfo.groupFormed)
            {
                //ConnectionStatus.setText("Client");
                clientClass=new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }



    public class ServerClass extends Thread
    {
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try{
                serverSocket=new ServerSocket(8888);
                socket=serverSocket.accept();
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();

            }
        }
    }

    private class SendReceive extends Thread
    {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket skt)
        {
            socket=skt;
            try{
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_READ:
                        byte[] readBuff = (byte[]) msg.obj;
                        String tempMsg = new String(readBuff, 0, msg.arg1);
                        //read_msg_box.setText(tempMsg);
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(readBuff.toString());
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mediaPlayer.start();
                        break;

                }
                return true;
            }
        });

        @Override
        public void run() {
            byte[] buffer=new byte[1024];
            int bytes;

            while(socket!=null)
            {
                try {
                    bytes=inputStream.read(buffer);
                    if(bytes>0)
                    {
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public class ClientClass extends Thread
    {
        Socket socket;
        String hostAdd;

        public ClientClass(InetAddress hostAddress)
        {
            hostAdd=hostAddress.getHostAddress();
            socket=new Socket();

        }

        @Override
        public void run() {
            try
            {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
                sendReceive=new SendReceive(socket);
                sendReceive.start();

            }
            catch (IOException e)
            {
                e.printStackTrace();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CALL_PHONE}, 3);
        //audiochatz init
        random = new Random();

        //chatz init
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        try
        {
            //chatz wifi on
            if (wifiManager.isWifiEnabled()) {
//                            wifiManager.setWifiEnabled(false);
            }
            else if(wifiManager.isWifiEnabled() == false)
            {
                wifiManager.setWifiEnabled(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            //chatz wifi p2p discover peers
            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    //   ConnectionStatus.setText("Discovery Started");
                }

                @Override
                public void onFailure(int i) {
                    //     ConnectionStatus.setText("Discover Starting Failed");
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //chatz connect to first available person
        try {

            final WifiP2pDevice device = deviceArray[0];
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;

            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i) {
                    Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }







        //---------- ALL FOR THE ACTION BAR ----------//
        // to center the title bar (...they call it the action bar)
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //---------- END ACTION BAR ----------//


        // to center the title bar (...they call it the action bar)
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //OBJECT DECLARATIONS
        ptt = (Button) findViewById(R.id.PTT);
        callFood= (Button) findViewById(R.id.callFood);
        callCampo= (Button) findViewById(R.id.callCampo);
        channelSelect= (TextView) findViewById(R.id.channelSelect);
        channelOne = (RadioButton) findViewById(R.id.channelOne);
        channelTwo = (RadioButton) findViewById(R.id.channelTwo);
        channelThree = (RadioButton) findViewById(R.id.channelThree);
        globalChannel = (RadioButton) findViewById(R.id.globalChannel);

        ptt.setText("Push-to-Talk");
        channelSelect.setText(" Channel Select: ");
        //Set all the text here rather than hardcode it, figure out how to use the string variables in strings.xml

        //This block should be in a function that runs to reset all of the buttons, this is just an initializer
        ptt.setEnabled(true);
        channelOne.setEnabled(false);
        channelTwo.setEnabled(false);
        channelThree.setEnabled(false);
        globalChannel.setEnabled(false);
        globalChannel.setBackgroundColor(0xFF116318); //When they push the button,
        globalChannel.setTextColor(0xFFefd43b);
        globalChannel.setChecked(true);
        callFood.setEnabled(true);
        callCampo.setEnabled(true);


        View.OnTouchListener pushToTalk = new View.OnTouchListener() { //below will be what happens when play again button is pressed
            @Override
            public boolean onTouch(View v, MotionEvent event) { //Might need to be something like on click and hold, idk yet
                Button b = (Button) v; //This represents the button they pressed, use this to use the right one (ex- b.doAThing)
                //When they push the ptt button, this is where the stuff needs to be executed
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        try
                        {
                            if(mediaPlayer != null){
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                MediaRecorderReady();
                            }

                            if(checkPermission()) {

                                AudioSavePathInDevice =
                                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                                CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                                MediaRecorderReady();

                                try {
                                    mediaRecorder.prepare();
                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                mediaRecorder.start();
                                //buttonStart.setEnabled(false);
                                //buttonStop.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Recording started",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                requestPermission();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();

                        }

                        try{
                            sendReceive.write(AudioSavePathInDevice.getBytes());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        //play audio
                        //  mediaPlayer.start();


                        v.setPressed(true);
                        channelOne.setEnabled(false); //Disable other buttons
                        channelTwo.setEnabled(false);
                        channelThree.setEnabled(false);
                        globalChannel.setEnabled(false);
                        callFood.setEnabled(false);
                        callCampo.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        //audiochatz stop audio
                        mediaRecorder.stop();
                        Toast.makeText(MainActivity.this, "Transmission Completed",
                                Toast.LENGTH_LONG).show();

                        v.setPressed(false);
                        //channelOne.setEnabled(true); //Disable other buttons
                        //channelTwo.setEnabled(true);
                        //channelThree.setEnabled(true);
                        //globalChannel.setEnabled(true);
                        callFood.setEnabled(true);
                        callCampo.setEnabled(true);
                        break;
                }

                return true;
                //HAVE TO SET THEM BACK TO TRUE ONCE PTT BUTTON IS RELEASED
            }
        };
        View.OnClickListener channelSelect = new View.OnClickListener() { //below will be what happens when play again button is pressed
            @Override
            public void onClick(View v) {
                Button b = (Button) v; //This represents the button they pressed, use it to use the right one
                //When they push a channel button, this is where the stuff needs to be executed

                /*channelOne.setBackgroundColor(0xFFefd43b);
                channelTwo.setBackgroundColor(0xFFefd43b);
                channelThree.setBackgroundColor(0xFFefd43b);
                globalChannel.setBackgroundColor(0xFFefd43b);
                channelOne.setTextColor(0xFF116318);
                channelTwo.setTextColor(0xFF116318);
                channelThree.setTextColor(0xFF116318);
                globalChannel.setTextColor(0xFF116318);

                b.setBackgroundColor(0xFF116318); //When they push the button,
                b.setTextColor(0xFFefd43b);
                channelOne.setEnabled(true);
                channelTwo.setEnabled(true);
                channelThree.setEnabled(true);
                globalChannel.setEnabled(true);
                b.setEnabled(false);*/
            }
        };

        View.OnClickListener phoneCallFood = new View.OnClickListener()
        { //below will be what happens when play again button is pressed
            @Override
            public void onClick(View v) {
                Button b = (Button) v; //This represents which button they pressed, use it to use the right one
                //When they push a call button, this is where the stuff needs to be executed******
                //Make a function to bring up the phone call-ey thing and use it her

                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                DialogFragment newFragment = new FoodCallDialogFragment();
                newFragment.show(getFragmentManager(), "Call");
            }
        };

        View.OnClickListener phoneCallSecurity = new View.OnClickListener()
        { //below will be what happens when play again button is pressed
            @Override
            public void onClick(View v) {
                Button b = (Button) v; //This represents which button they pressed, use it to use the right one
                //When they push a call button, this is where the stuff needs to be executed******
                //Make a function to bring up the phone call-ey thing and use it here
                DialogFragment newFragment = new CallDialogFragment();
                newFragment.show(getFragmentManager(), "Call");
            }
        };


        //ON-CLICK LISTENERS:
        ptt.setOnTouchListener(pushToTalk);

        channelOne.setOnClickListener(channelSelect);
        channelTwo.setOnClickListener(channelSelect);
        channelThree.setOnClickListener(channelSelect);
        globalChannel.setOnClickListener(channelSelect);

        callFood.setOnClickListener(phoneCallFood);
        callCampo.setOnClickListener(phoneCallSecurity);

    }
}
