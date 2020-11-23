package ie.ul.cs4084project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static  String id;
    private static  String email;
    public static  String name;
    private byte[] bytes;
    List<Fragment> fragments;
    StorageReference storageReference;
    Feed feed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.ID);
        email = intent.getStringExtra(MainActivity.EMAIL);
        name = intent.getStringExtra(MainActivity.NAME);
        storageReference = FirebaseStorage.getInstance().getReference();

        Task<byte[]> task = storageReference.child("profilesInfo")
                .child(id + ".txt").getBytes(1000000000).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        bytes = task.getResult();
                        gotProfile();
                    }
                });



    }

    public void addImage(View v) {
        feed.addImage(v);
    }

    public void post(View v) {
        feed.post(v);
    }

    private void gotProfile(){
        feed = new Feed();

        fragments = new ArrayList<Fragment>();


        fragments.add(new settings(name, email, id));
        fragments.add(new Search());
        fragments.add(feed);
        fragments.add(new notifications());
        fragments.add(new profile(new String(bytes, StandardCharsets.UTF_8)));
////

////
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),  fragments);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_settings);
        tabLayout.getTabAt(0).setTag("Settings");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(1).setTag("Search");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_news_feed);
        tabLayout.getTabAt(2).setTag("News Feed");
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications);
        tabLayout.getTabAt(3).setTag("Notifications");
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_profile);
        tabLayout.getTabAt(4).setTag("Profile");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getApplicationContext(), tab.getTag().toString(), Toast.LENGTH_SHORT).show();
                if(tab.getTag().toString().equals("News Feed")){
                    //feed.refresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}