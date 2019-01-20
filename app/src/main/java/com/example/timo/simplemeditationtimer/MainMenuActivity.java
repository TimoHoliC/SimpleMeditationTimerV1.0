package com.example.timo.simplemeditationtimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainMenuActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener {

    private Button startMeditationButton;
    private Spinner phasesSpinner;
    private Spinner durationSpinner;
    private Spinner warmUpSpinner;
    private ArrayAdapter<String> phasesAdapter;
    private ArrayAdapter<String> durationsAdapter;
    private ArrayAdapter<String> warmUpAdapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        startMeditationButton = findViewById(R.id.startMeditationButton);
        startMeditationButton.setOnClickListener(this);
        phasesSpinner = findViewById(R.id.phasesSpinner);
        phasesSpinner.setOnItemSelectedListener(this);
        durationSpinner = findViewById(R.id.durationSpinner);
        durationSpinner.setOnItemSelectedListener(this);
        warmUpSpinner = findViewById(R.id.warmUpSpinner);
        warmUpSpinner.setOnItemSelectedListener(this);

        List<String> numbers = new ArrayList<String>();
        numbers.add("1");
        numbers.add("4");
        numbers.add("5");

        phasesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        phasesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        phasesSpinner.setAdapter(phasesAdapter);

        List<String> durations = new ArrayList<>();
        durations.add("20:00");
        durations.add("30:00");
        durations.add("40:00");

        durationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
        durationsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        durationSpinner.setAdapter(durationsAdapter);

        List<String> warumUpList = new ArrayList<>();
        warumUpList.add("-");
        warumUpList.add("00:30");
        warumUpList.add("1:00");
        warumUpList.add("2:00");
        warumUpList.add("3:00");
        warumUpList.add("5:00");
        warumUpList.add("10:00");

        warmUpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, warumUpList);
        warmUpAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        warmUpSpinner.setAdapter(warmUpAdapter);

    }

    @Override
    public void onClick(View v){
        int ce = v.getId();

        if(ce == R.id.startMeditationButton){


            Intent intent = new Intent(MainMenuActivity.this, MeditationActivity.class);
            intent.putExtra("lastOfMeditation", getDuration());
            intent.putExtra("numberOfPhases", getPhase());
            intent.putExtra("warmUpTime", getWarmUpTime());

            startActivity(intent);
        }
    }

    private long getWarmUpTime() {
        int position = warmUpSpinner.getSelectedItemPosition();
        if(position == 0){
            return 0;
        }else if(position == 1){
            return 1 * 1000 * 30;
        }else if (position == 2){
            return 1 * 1000 * 60;
        }else if(position == 3){
            return 2 * 1000 * 60;
        }else if (position == 4){
            return 3 * 1000 * 60;
        }else if (position == 5){
            return 5 * 1000 * 60;
        }else{
            return 10 * 1000 * 60;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(12);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getPhase(){
        int phase = phasesSpinner.getSelectedItemPosition();
        if(phase == 0){
            phase = 1;
        }else if(phase == 1){
            phase = 4;
        }else if (phase == 2){
            phase = 5;
        }

        return phase;
    }

    public long getDuration(){
        int durationPosition = durationSpinner.getSelectedItemPosition();
        long lDuration = 20 * 1000 * 60;
        if(durationPosition == 0){
            lDuration = 20 * 1000 * 60;
        }else if(durationPosition == 1){
            lDuration = 30 * 1000 * 60;
        }else if (durationPosition == 2){
            lDuration = 40 * 1000 * 60;
        }

        return lDuration;

    }
}
