package com.example.setup;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Demo";

    private ShakeDetector mShakeDetector;
    private Random mRandomGenerator;
    private Integer mShakeValue;

    private ImageView mDiceImage;
    private TextView mDiceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);                                                                              // will hide the title
        getSupportActionBar().hide();                                                                                               // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);          // enable full screen

        setContentView(R.layout.activity_main);

        mRandomGenerator = new Random();
        mDiceImage = (ImageView)findViewById(R.id.diceImageView);
        mDiceText = (TextView)findViewById(R.id.diceTextView);
        final MediaPlayer mPlayer;

        mPlayer = MediaPlayer.create(this, R.raw.dice_roll);

        mShakeDetector = new ShakeDetector( getApplicationContext(), new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {

                Log.e(TAG, "Shaken, but not stirred");
                mShakeValue = mRandomGenerator.nextInt( 6 );

                Log.e(TAG, "RANDOM VALUE = " + mShakeValue);
                mPlayer.start();

                switch (mShakeValue) {
                    case 0: mDiceImage.setImageResource(R.drawable.dice1); break;
                    case 1: mDiceImage.setImageResource(R.drawable.dice2); break;
                    case 2: mDiceImage.setImageResource(R.drawable.dice3); break;
                    case 3: mDiceImage.setImageResource(R.drawable.dice4); break;
                    case 4: mDiceImage.setImageResource(R.drawable.dice5); break;
                    case 5: mDiceImage.setImageResource(R.drawable.dice6); break;

                    default : break;
                }

                String diceText = String.format("Dice : %d", mShakeValue + 1 );
                mDiceText.setText(diceText);
            }
        });

    }
}
