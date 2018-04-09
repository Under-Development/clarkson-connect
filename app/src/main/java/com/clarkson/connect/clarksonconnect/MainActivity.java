package com.clarkson.connect.clarksonconnect;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Button ptt, callFood, callCampo;
    private RadioButton channelOne, channelTwo, channelThree, globalChannel;
    private TextView channelSelect;
    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //OBJECT DECLARATIONS
        ptt = (Button) findViewById(R.id.PTT);
        callFood = (Button) findViewById(R.id.callFood);
        callCampo = (Button) findViewById(R.id.callCampo);
        channelSelect = (TextView) findViewById(R.id.channelSelect);
        channelOne = (RadioButton) findViewById(R.id.channelOne);
        channelTwo = (RadioButton) findViewById(R.id.channelTwo);
        channelThree = (RadioButton) findViewById(R.id.channelThree);
        globalChannel = (RadioButton) findViewById(R.id.globalChannel);

        ptt.setText("Push-to-Talk");
        channelSelect.setText(" Channel Select: ");
        //Set all the text here rather than hardcode it, figure out how to use the string variables in strings.xml

        //This block should be in a function that runs to reset all of the buttons, this is just an initializer
        ptt.setEnabled(true);
        channelOne.setEnabled(true);
        channelTwo.setEnabled(true);
        channelThree.setEnabled(true);
        globalChannel.setEnabled(true);
        callFood.setEnabled(true);
        callCampo.setEnabled(true);


        View.OnClickListener pushToTalk = new View.OnClickListener() { //below will be what happens when play again button is pressed
            @Override
            public void onClick(View v) { //Might need to be something like on click and hold, idk yet
                Button b = (Button) v; //This represents the button they pressed, use this to use the right one (ex- b.doAThing)
                //When they push the ptt button, this is where the stuff needs to be executed
                //function.function();
                ptt.setBackgroundColor(Color.RED); //When they push the button
                ptt.setText("Pressed");
                channelOne.setEnabled(false); //Disable other buttons
                channelTwo.setEnabled(false);
                channelThree.setEnabled(false);
                globalChannel.setEnabled(false);
                callFood.setEnabled(false);
                callCampo.setEnabled(false);
                //HAVE TO SET THEM BACK TO TRUE ONCE PTT BUTTON IS RELEASED
            }
        };
        View.OnClickListener channelSelect = new View.OnClickListener() { //below will be what happens when play again button is pressed
            @Override
            public void onClick(View v) {
                Button b = (Button) v; //This represents the button they pressed, use it to use the right one
                //When they push a channel button, this is where the stuff needs to be executed

                b.setBackgroundColor(Color.GREEN); //When they push the button,
                channelOne.setEnabled(true);
                channelTwo.setEnabled(true);
                channelThree.setEnabled(true);
                globalChannel.setEnabled(true);
                b.setEnabled(false);
            }
        };

        View.OnClickListener phoneCall = new View.OnClickListener() { //below will be what happens when play again button is pressed
            @Override
            public void onClick(View v) {
                Button b = (Button) v; //This represents which button they pressed, use it to use the right one
                //When they push a call button, this is where the stuff needs to be executed******
                //Make a function to bring up the phone call-ey thing and use it here

                if (b.getId() == R.id.callFood)
                    dialogFood(dialogView);
                else
                    dialogCampo(dialogView);
            }
        };


        //ON-CLICK LISTENERS:
        ptt.setOnClickListener(pushToTalk);

        channelOne.setOnClickListener(channelSelect);
        channelTwo.setOnClickListener(channelSelect);
        channelThree.setOnClickListener(channelSelect);
        globalChannel.setOnClickListener(channelSelect);

        callFood.setOnClickListener(phoneCall);
        callCampo.setOnClickListener(phoneCall);
    }


    public void dialogCampo(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Call Campus Security?");
        alertDialogBuilder.setPositiveButton("Call",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //open phone app with campo number here
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dialogFood(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Call Food Service?");
        alertDialogBuilder.setPositiveButton("Call",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //open phone app with food number here
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
