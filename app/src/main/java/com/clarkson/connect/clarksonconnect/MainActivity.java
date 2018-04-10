package com.clarkson.connect.clarksonconnect;

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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button ptt, callFood, callCampo;
    private RadioButton channelOne, channelTwo, channelThree, globalChannel;
    private TextView channelSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        channelOne.setEnabled(true);
        channelTwo.setEnabled(true);
        channelThree.setEnabled(true);
        globalChannel.setEnabled(true);
        callFood.setEnabled(true);
        callCampo.setEnabled(true);


        View.OnTouchListener pushToTalk = new View.OnTouchListener()
        { //below will be what happens when play again button is pressed
            @Override
            public boolean onTouch(View v, MotionEvent event) { //Might need to be something like on click and hold, idk yet
                Button b = (Button) v; //This represents the button they pressed, use this to use the right one (ex- b.doAThing)
                //When they push the ptt button, this is where the stuff needs to be executed
                int action = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        v.setPressed(true);
                        channelOne.setEnabled(false); //Disable other buttons
                        channelTwo.setEnabled(false);
                        channelThree.setEnabled(false);
                        globalChannel.setEnabled(false);
                        callFood.setEnabled(false);
                        callCampo.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setPressed(false);
                        channelOne.setEnabled(true); //Disable other buttons
                        channelTwo.setEnabled(true);
                        channelThree.setEnabled(true);
                        globalChannel.setEnabled(true);
                        callFood.setEnabled(true);
                        callCampo.setEnabled(true);
                        break;
                }

                return true;
                //HAVE TO SET THEM BACK TO TRUE ONCE PTT BUTTON IS RELEASED
            }
        };
        View.OnClickListener channelSelect = new View.OnClickListener()
        { //below will be what happens when play again button is pressed
            @Override
            public void onClick(View v) {
                Button b = (Button) v; //This represents the button they pressed, use it to use the right one
                //When they push a channel button, this is where the stuff needs to be executed

                channelOne.setBackgroundColor(0xFFefd43b);
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
                b.setEnabled(false);
            }
        };

        View.OnClickListener phoneCall = new View.OnClickListener()
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

        callFood.setOnClickListener(phoneCall);
        callCampo.setOnClickListener(phoneCall);

    }
}
