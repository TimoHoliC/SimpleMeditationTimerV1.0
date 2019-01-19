package com.example.timo.simplemeditationtimer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MeditationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startPauseButton;
    private TextView timeTextView;
    private TextView phaseTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds;
    private boolean timerRunning;

    private boolean timerPositive;
    private long timeSetInMilliseconds;
    private long timePerPhaseInMilliseconds;
    private int phase;
    private int phaseDisplayed;
    private Intent intent;
    private long bellDelay;
    int counter;
    private boolean medFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meditation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        phaseDisplayed = 0;
        counter = 0;

        medFinished = false;
        intent = getIntent();
        timeLeftInMilliseconds = intent.getLongExtra("lastOfMeditation",0);
        phase = intent.getIntExtra("numberOfPhases",0);
        phaseTextView = findViewById(R.id.phaseTextView);
        timeSetInMilliseconds = timeLeftInMilliseconds;
        timeTextView = findViewById(R.id.timeTextView);
        timePerPhaseInMilliseconds = timeSetInMilliseconds / phase;
        startPauseButton = findViewById(R.id.startPauseButton);
        timerPositive = true;
        startPauseButton.setOnClickListener((View.OnClickListener) this);
        timeTextView.setOnClickListener((View.OnClickListener) this);
        checkForPhaseses();
    }

    private void checkForPhaseses(){
        if(phase == 1){
            phaseTextView.setVisibility(View.INVISIBLE);
            updateCountdownText();
        }else{
            updateCountdownText();
        }
    }

    @Override
    public void onClick(View v){
        int ce = v.getId();

        if(ce == R.id.startPauseButton && medFinished){
            finish();
        }else if(ce == R.id.startPauseButton && timerRunning){
            pauseTimer();
        }else if(ce == R.id.startPauseButton && !timerRunning){
            startMeditationTimer();
        }else if (ce == R.id.timeTextView && timerPositive){
            timerPositive = false;
            updateCountdownText();
        }else if (ce == R.id.timeTextView && timerPositive == false){
            timerPositive = true;
            updateCountdownText();
        }
    }

    private void startMeditationTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {

                ringEndbellTimer();
                medFinished = true;
                startPauseButton.setBackgroundResource(R.drawable.stop_button);
                phaseTextView.setText("");
            }
        }.start();

        timerRunning = true;
        startPauseButton.setBackgroundResource(R.drawable.pause_button);
    }

    public void ringEndbellTimer(){

        countDownTimer = new CountDownTimer(4000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
               bellDelay = millisUntilFinished;
               ringEndbell();


            }

            @Override
            public void onFinish() {


            }
        }.start();


    }

    private void ringEndbell(){

        MediaPlayer.create(this, R.raw.japanese_singing_bowl).start();

    }

    private void pauseTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        startPauseButton.setBackgroundResource(R.drawable.play_button);
    }

    private void displayPhase(){
        if(phaseDisplayed != 0){
            phaseTextView.setText("Phase " + phaseDisplayed);
            if(phaseDisplayed != 1){
                MediaPlayer.create(this, R.raw.japanese_singing_bowl).start();
            }
        }else {
            phaseTextView.setText("Phase " + 1);
        }
        phaseDisplayed++;

    }

    private void updateCountdownText(){

        int minutes;
        int seconds;

        long difference = timeSetInMilliseconds - timeLeftInMilliseconds + 900;

        countPhases(difference);



        if(timerPositive) {
            minutes = (int) (timeLeftInMilliseconds / 1000) / 60;
            seconds = (int) (timeLeftInMilliseconds / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            timeTextView.setText("-"+timeLeftFormatted);
        }else{
            minutes = (int) (difference / 1000) / 60;
            seconds = (int) (difference / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            timeTextView.setText(timeLeftFormatted);
        }



    }

    private void countPhases(long difference){
        if((timeLeftInMilliseconds / timePerPhaseInMilliseconds == phase)){
            phase--;

            displayPhase();

        }


    }


}
