package ie.ul.cs4084project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PostRunActivity extends AppCompatActivity {

    public static final int SECOND = 1000;
    public static final int MINUTE = 60000;
    public static final int HOUR = 3600000;
    private LinearLayout linearLayout;
    private ArrayList<RunMilestone> milestones;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private String name, email, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_run);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        email = intent.getStringExtra(MainActivity.EMAIL);
        id = intent.getStringExtra(MainActivity.ID);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewPostRun);
        byte[] bytes = getIntent().getByteArrayExtra(Run.RUN_PICTURE);
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bmp);
        linearLayout = findViewById(R.id.linearLayoutPostRun);
        milestones = new ArrayList<RunMilestone>();
        String[] strings = getIntent().getStringArrayExtra(Run.MILESTONES);
        for(int i=0; i<(strings.length); i++){
            String s = strings[i];
            milestones.add(new RunMilestone(s));
        }
        setFastestAndSlowest();

        setTotalTimeDistance();

        for (RunMilestone r: milestones) {
            CardView cardView = new CardView(this);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            cardView.addView(layout);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            lp.bottomMargin = 20;
            cardView.setLayoutParams(lp);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layout.setLayoutParams(layoutParams);
            TextView textView = new TextView(this);
            textView.setTextSize(50);
            textView.setText(r.getKm() + "Km");
            textView.setTextColor(Color.BLACK);
            layout.addView(textView);
            textView = new TextView(this);
            textView.setTextSize(40);
            textView.setTextColor(Color.BLACK);
            textView.setText("Lap Time: " + r.getLapTime());
            layout.addView(textView);
            cardView.setCardBackgroundColor(Color.YELLOW);
            cardView.setCardElevation(10);
            cardView.setElevation(10);
            cardView.setRadius(100);

            if(r.getFastest()){
                cardView.setCardBackgroundColor(Color.GREEN);
            }else if(r.getSlowest()){
                cardView.setCardBackgroundColor(Color.RED);
            }
            linearLayout.addView(cardView);
        }

        setAverageTime();
        setAverageSpeed();

    }

    private void setTotalTimeDistance(){
        CardView cardView = new CardView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(layout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 20;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        TextView textView = new TextView(this);
        textView.setTextSize(50);
        textView.setText("Total Distance: " + milestones.get(milestones.size()-1).getKm() +
                "km\nTotal Time: "  +getIntent().getStringExtra(Run.TIME));
        textView.setTextColor(Color.WHITE);
        layout.addView(textView);
        cardView.setCardBackgroundColor(Color.BLACK);
        cardView.setCardElevation(10);
        cardView.setRadius(100);
        linearLayout.addView(cardView);
    }

    private void setAverageTime(){

        CardView cardView = new CardView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(layout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 20;
        cardView.setLayoutParams(lp);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        TextView textView = new TextView(this);
        textView.setTextSize(50);
        textView.setText("Average Km time: " + millsToTime(getAverageTime()));
        textView.setTextColor(Color.BLACK);
        layout.addView(textView);
        cardView.setCardBackgroundColor(Color.BLUE);
        cardView.setCardElevation(10);
        cardView.setRadius(100);
        linearLayout.addView(cardView);
    }

    private void setAverageSpeed(){

        CardView cardView = new CardView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(layout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 20;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        TextView textView = new TextView(this);
        textView.setTextSize(50);
        double speed = (double) 1/getAverageTime();
        speed*= HOUR;
        textView.setText("Average speed: " + df2.format(speed)  +"Km/h");
        textView.setTextColor(Color.BLACK);
        layout.addView(textView);
        cardView.setCardBackgroundColor(Color.MAGENTA);
        cardView.setCardElevation(10);
        cardView.setRadius(100);
        linearLayout.addView(cardView);
    }

    private void setFastestAndSlowest(){
        int f = 0;
        int s = 0;
        long fastest = milestones.get(0).getLapTimeLong();
        long slowest = milestones.get(0).getLapTimeLong();

        for (int i = 0; i<milestones.size(); i++) {
            RunMilestone runMilestone = milestones.get(i);
            if(runMilestone.getLapTimeLong() < fastest){
                fastest = runMilestone.getLapTimeLong();
                f = i;
            }else if(runMilestone.getLapTimeLong() > slowest){
                slowest = runMilestone.getLapTimeLong();
                s=i;
            }
        }
        milestones.get(f).setFastest(true);
        milestones.get(s).setSlowest(true);
    }

    private long getAverageTime(){
        long totalTime = 0;
        double totalDistance  = milestones.get(milestones.size()-1).getKm();
        String[] times = getIntent().getStringExtra(Run.TIME).split(":");
        totalTime+= ((Integer.parseInt(times[0].substring(0,2))) * HOUR);
        totalTime+= ((Integer.parseInt(times[1].substring(1,3))) * MINUTE);
        totalTime+= ((Integer.parseInt(times[2].substring(1,3))) * SECOND);

        long avg = (long) (totalTime/totalDistance);
        return avg;
    }

    private String millsToTime(long time){

        long tot = time;
        int hours = 0;
        int mins = 0;
        int secs = 0;
        String t = "";
        if (tot > HOUR){
            hours = (int) tot / HOUR;
            tot %= HOUR;
        }
        mins =  (int) tot / MINUTE;
        tot %= MINUTE;
        secs =  (int) tot/SECOND ;
        if (hours>0){
            t+=getLapTimeUnit(hours);
            t+=":";
        }
        t+=getLapTimeUnit(mins)+":"+getLapTimeUnit(secs);
        return t;
    }


    private String getLapTimeUnit(int unit){
        String lapTime = "";


        if(unit<10){
            lapTime+= "0";
        }
        lapTime+=unit;
        return lapTime;
    }

    private ArrayList<RunMilestone> test(){
        milestones = new ArrayList<RunMilestone>();
        for (int i = 0 ; i<5; i++){
            int r = ((int) (Math.random() * 8)) +1;
            int s = ((int) (Math.random() * 8)) +1;
            if(i>0) {
                milestones.add(new RunMilestone(i + 1, (((r) * 10000) + (s * 1000)), (milestones.get(i-1).getTotalTime()+(((r) * 10000) + (s * 1000)))));
            }else {
                milestones.add(new RunMilestone(i + 1, ((r) * 10000) + (s * 1000), 0));
            }
        }int r = ((int) (Math.random() * 8)) +1;
        int s = ((int) (Math.random() * 8)) +1;
        milestones.add(new RunMilestone(5.5, (((r) * 10000) + (s * 1000)), (milestones.get(milestones.size()-1).getTotalTime()+(((r) * 10000) + (s * 1000)))));
        return milestones;
    }


    public void finish(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(MainActivity.ID, id);
        intent.putExtra(MainActivity.EMAIL, email);
        intent.putExtra(MainActivity.NAME, name);

        startActivity(intent);
    }
}