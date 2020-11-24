package ie.ul.cs4084project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLngBounds;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Run extends AppCompatActivity {
    public static final String MILESTONES = "MILESTONES";
    public static final String RUN_PICTURE = "RUN_PICTURE";
    public static final String TIME = "TIME";
    private RunFragment runFragment;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private Chronometer chronometer;
    boolean start;
    private String name, id, email;
    private ArrayList<RunMilestone> milestones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        email = intent.getStringExtra(MainActivity.EMAIL);
        id = intent.getStringExtra(MainActivity.ID);
        runFragment = (RunFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentRun);
        runFragment.setUp(name, id, email);
        chronometer = findViewById(R.id.timer);
        start = true;
        milestones = new ArrayList<RunMilestone>();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

    }

    public void start(View view){
        if(start) {
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    int h = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;
                    int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                    String t = (h < 10 ? "0" + h : h) + " : " + (m < 10 ? "0" + m : m) + " : " + (s < 10 ? "0" + s : s);
                    chronometer.setText(t);
                }
            });
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setText("00 : 00 : 00");
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            runFragment.start();
            start = false;
        }
    }



    public void finish(View view){

            String time = chronometer.getText().toString();
            chronometer.stop();
        runFragment.finish(time);
        }

    public void addMileStone(double km){
        long totalTime = SystemClock.elapsedRealtime() - chronometer.getBase();
        if(km>0){
            if(km % 1 == 0) {
                long lapTime = totalTime - milestones.get((int) km -1).getTotalTime();
                milestones.add(new RunMilestone(km, lapTime, totalTime));
            }else {
                long lapTime = totalTime - milestones.get(milestones.size()-1).getTotalTime();
                BigDecimal bd = new BigDecimal(km).setScale(2, RoundingMode.HALF_UP);
                milestones.add(new RunMilestone(bd.doubleValue(), lapTime, totalTime));
            }
        }else{
            milestones.add(new RunMilestone(km, 0, 0));
        }
    }

    public ArrayList<RunMilestone> getMilestones(){
        return milestones;
    }
}