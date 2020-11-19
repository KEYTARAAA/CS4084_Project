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

import java.text.DecimalFormat;

public class Run extends AppCompatActivity {
    private RunFragment runFragment;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private Chronometer chronometer;
    boolean start;
    private String name, id, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        email = intent.getStringExtra(MainActivity.EMAIL);
        id = intent.getStringExtra(MainActivity.ID);
        runFragment = (RunFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentRun);
        chronometer = findViewById(R.id.timer);
        start = true;
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
            chronometer.start();
            runFragment.start();
            start = false;
        }
    }



    public void finish(View view){


            chronometer.stop();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(MainActivity.ID, id);
        intent.putExtra(MainActivity.EMAIL, email);
        intent.putExtra(MainActivity.NAME, name);

        startActivity(intent);
        }

}