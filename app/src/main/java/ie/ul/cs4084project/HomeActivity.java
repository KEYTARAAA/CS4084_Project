package ie.ul.cs4084project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
    private List<Fragment> fragments;
    private StorageReference storageReference;
    private Feed feed;
    private notifications notifications;
    private FirebaseFirestore db;
    private int count;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        count = 0;
        db = FirebaseFirestore.getInstance();
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
        notifications = new notifications(id);


        fragments = new ArrayList<Fragment>();


        fragments.add(new settings(name, id, email));
        fragments.add(new Search());
        fragments.add(feed);
        fragments.add(notifications);
        fragments.add(new profile(new String(bytes, StandardCharsets.UTF_8), id));//vpid


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),  fragments);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout);

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

        getCount();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getApplicationContext(), tab.getTag().toString(), Toast.LENGTH_SHORT).show();
                if(tab.getTag().toString().equals("News Feed")){
                    //feed.refresh();
                }
                if(tab.getTag().toString().equals("Notifications")){
                    if(notifications.getCheck()) {
                        notifications.reload();
                    }
                    tab.removeBadge();
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

    public void getCount() {
        db.collection("Profiles").document(id).collection("Notifications")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> notifications = task.getResult().getDocuments();
                for (DocumentSnapshot ds : notifications) {
                    if (ds.get("Seen").toString().compareTo("false") == 0) {
                        count++;
                        db.collection("Profiles").document(id).collection("Notifications").document(ds.get("ID").toString())
                                .update("Seen", "true");
                        final BadgeDrawable badgeDrawable =tabLayout.getTabAt(3).getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(Color.rgb(0,255, 0));
                        badgeDrawable.setBadgeTextColor(Color.BLACK);
                        badgeDrawable.setMaxCharacterCount(5);
                        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
                        badgeDrawable.setNumber(count);
                    }
                }

            }
        });
    }


}