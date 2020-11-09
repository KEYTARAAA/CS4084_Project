package ie.ul.cs4084project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.InputStream;

public class test extends AppCompatActivity {
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        count=0;
    }

    public void button(View view){
        ImageView imageView = new ImageView(this);
        TextView textView = new TextView(this);
        if(count==0) {


            new DownloadImageTask(imageView)
                    .execute("https://firebasestorage.googleapis.com/v0/b/cs4084-project-fae83.appspot.com/o/user_image%2Fp06TYGz2B3aR2q4pK0zvX4UyVsV2.jpeg?alt=media&token=c00160f5-dc40-4c67-b8ef-221a6bb26711");

            textView.setText("SETTINGS\n\n\n");
        }
        if(count==1) {
            new DownloadImageTask(imageView)
                    .execute("https://firebasestorage.googleapis.com/v0/b/cs4084-project-fae83.appspot.com/o/user_image%2Fp06TYGz2B3aR2q4pK0zvX4UyVsV2.jpeg?alt=media&token=66cbe4fc-5e84-4396-938a-6fd52da1e1a9");
            imageView.setImageResource(R.drawable.icon_profile);
            textView.setText("PROFILE\n\n\n");
        }
        if(count==2) {
            imageView.setImageResource(R.drawable.icon_notifications);
            textView.setText("NOTIFICATIONS\n\n\n");
        }
        if(count==3) {
            imageView.setImageResource(R.drawable.icon_news_feed);
            textView.setText("FEED\n\n\n");
        }
        if(count==4) {
            imageView.setImageResource(R.drawable.icon_search);
            textView.setText("SEARCH\n\n\n");
        }
        LinearLayout linearLayout = findViewById(R.id.testLinearLayout);
        ScrollView scrollView = findViewById(R.id.scrollView2);
        //scrollView.addView(imageView);
        //scrollView.addView(textView);
        int dpi = getResources().getDisplayMetrics().densityDpi;
        int pixels = getResources().getDisplayMetrics().widthPixels;
        int density = (int) (100*getResources().getDisplayMetrics().density);
        float dp = pixels / (dpi/(density));
        Button b = findViewById(R.id.button);
        System.out.println("DPI = " + dpi + "\nPIXELS = "+ pixels+"\nDENSITY = " + density
        + "\nDP = " + dp +"\nDPI / DENSITY = "+ (dpi/density) + "\nPIXELS/(DPI / DENSITY)" + dp );
        String s = "";
        s+=density;
        b.setText(s);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        linearLayout.addView(imageView, layoutParams);
        linearLayout.addView(textView);
        imageView.getLayoutParams().height = (int) (dp*.8);
        imageView.getLayoutParams().width =(int) (dp*.8);


        count++;
    }

    public void button2(View view){
        ImageView imageView = new ImageView(this);
        TextView textView = new TextView(this);
        String s ="";
        if(count%2==1) {
            s = "https://firebasestorage.googleapis.com/v0/b/cs4084-project-fae83.appspot.com/o/user_image%2Fp06TYGz2B3aR2q4pK0zvX4UyVsV2.jpeg?alt=media&token=c00160f5-dc40-4c67-b8ef-221a6bb26711";

            textView.setText("SETTINGS\n\n\n");
        }
        if(count%2 == 0) {
            s="https://firebasestorage.googleapis.com/v0/b/cs4084-project-fae83.appspot.com/o/user_image%2Fp06TYGz2B3aR2q4pK0zvX4UyVsV2.jpeg?alt=media&token=66cbe4fc-5e84-4396-938a-6fd52da1e1a9";

            textView.setText("PROFILE\n\n\n");
        }
        LinearLayout linearLayout = findViewById(R.id.testLinearLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        linearLayout.addView(getIv(s), layoutParams);
        linearLayout.addView(textView);


        count++;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private ImageView getIv(String s){
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        new DownloadImageTask(imageView)
                .execute(s);
        return imageView;
    }

    private ImageView getLp(){
        ImageView imageView = new ImageView(this);
        return imageView;
    }
}