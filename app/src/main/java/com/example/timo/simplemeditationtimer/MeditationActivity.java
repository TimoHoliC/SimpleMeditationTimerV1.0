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

import java.util.Locale;

public class MeditationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startPauseButton;
    private TextView timeTextView;
    private TextView phaseTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;
    private boolean timerPositive;
    private long timeSetInMilliseconds;
    private long timePerPhaseInMilliseconds;
    private long warmUpTimeLeftInMillis;
    private long warmUpTimeSetInMillis;
    private int phase;
    private int phasesOverall;
    private int phaseDisplayed;
    private Intent intent;
    private long bellDelay = 2000;
    int counter;
    private boolean medFinished;
    private boolean warmUp;


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
        timeLeftInMillis = intent.getLongExtra("lastOfMeditation", 0);
        phase = intent.getIntExtra("numberOfPhases",0);
        phasesOverall = phase;
        warmUpTimeLeftInMillis = intent.getLongExtra("warmUpTime", 0);
        warmUp = warmUpTimeLeftInMillis != 0;
        phaseTextView = findViewById(R.id.phaseTextView);
        timeSetInMilliseconds = timeLeftInMillis;
        warmUpTimeSetInMillis = warmUpTimeLeftInMillis;
        timeTextView = findViewById(R.id.timeTextView);
        timePerPhaseInMilliseconds = timeSetInMilliseconds / phase;
        startPauseButton = findViewById(R.id.startPauseButton);
        timerPositive = true;
        startPauseButton.setOnClickListener(this);
        timeTextView.setOnClickListener(this);
        checkForWarmUp();

    }

    private void checkForWarmUp() {
        if (warmUp == false)
            checkForPhases();
        else
            updateCountdownText(warmUpTimeSetInMillis, warmUpTimeLeftInMillis);
    }

    private void checkForPhases() {

        if (phasesOverall == 1) {
            phaseTextView.setVisibility(View.INVISIBLE);
            updateCountdownText(timeSetInMilliseconds, timeLeftInMillis);
        } else {
            updateCountdownText(timeSetInMilliseconds, timeLeftInMillis);
        }
    }

    @Override
    public void onClick(View v){
        int ce = v.getId();

        if(ce == R.id.startPauseButton && medFinished){
            finish();
        }else if(ce == R.id.startPauseButton && timerRunning){

            pauseTimer();
        } else if (ce == R.id.startPauseButton && !timerRunning && warmUp) {
            startWarmUpTimer();
        } else if (ce == R.id.startPauseButton && !timerRunning && !warmUp) {
            startMeditationTimer();
        }else if (ce == R.id.timeTextView && timerPositive){
            timerPositive = false;
            updateCountdownText(timeSetInMilliseconds, timeLeftInMillis);
        }else if (ce == R.id.timeTextView && timerPositive == false){
            timerPositive = true;
            updateCountdownText(timeSetInMilliseconds, timeLeftInMillis);
        }
    }

    private void startWarmUpTimer() {
        warmUp = true;

        countDownTimer = new CountDownTimer(warmUpTimeLeftInMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                warmUpTimeLeftInMillis = millisUntilFinished;
                updateCountdownText(warmUpTimeSetInMillis, warmUpTimeLeftInMillis);
            }

            @Override
            public void onFinish() {
                warmUp = false;
                startThreeGongsTimer();
                startMeditationTimer();
            }
        }.start();

        timerRunning = true;
        startPauseButton.setBackgroundResource(R.drawable.pause_button);

    }

    private void startMeditationTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText(timeSetInMilliseconds, timeLeftInMillis);
            }

            @Override
            public void onFinish() {

                startThreeGongsTimer();
                medFinished = true;
                startPauseButton.setBackgroundResource(R.drawable.stop_button);
                phaseTextView.setText("");
                countDownTimer.cancel();
            }
        }.start();

        timerRunning = true;
        startPauseButton.setBackgroundResource(R.drawable.pause_button);
    }

    private void startThreeGongsTimer() {

        new CountDownTimer(bellDelay * 3 + 500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                //kleiner hundert, da ungenau beim abtasten
                if (millisUntilFinished % bellDelay <= 100) {
                    ringBell();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

        timerRunning = true;
        startPauseButton.setBackgroundResource(R.drawable.pause_button);
    }

    private void ringBell() {

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

    private void updateCountdownText(long timeSetInMillis, long timeLeftInMillis) {

        int minutes;
        int seconds;

        long difference = timeSetInMillis - timeLeftInMillis + 900;

        if (warmUp == true) {
            phaseTextView.setText("Vorbereitung");
        } else if (phasesOverall != 1) {
            countPhases();
        } else if (phasesOverall == 1) {
            phaseTextView.setVisibility(View.INVISIBLE);
        }

        if(timerPositive) {
            minutes = (int) (timeLeftInMillis / 1000) / 60;
            seconds = (int) (timeLeftInMillis / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            timeTextView.setText("-"+timeLeftFormatted);
        }else{
            minutes = (int) (difference / 1000) / 60;
            seconds = (int) (difference / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            timeTextView.setText(timeLeftFormatted);
        }



    }

    private void countPhases() {
        if ((timeLeftInMillis / timePerPhaseInMilliseconds == phase)) {
            phase--;

            displayPhase();

        }


    }


}
