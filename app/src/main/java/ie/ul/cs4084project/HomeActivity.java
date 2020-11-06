package ie.ul.cs4084project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static  String id;
    private static  String email;
    private static  String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.ID);
        email = intent.getStringExtra(MainActivity.EMAIL);
        name = intent.getStringExtra(MainActivity.NAME);

        List<Integer> images = new ArrayList<Integer>();
        images.add(R.drawable.icon_settings);
        images.add(R.drawable.icon_search);
        images.add(R.drawable.icon_news_feed);
        images.add(R.drawable.icon_notifications);
        images.add(R.drawable.icon_profile);

        List<Fragment> fragments = new ArrayList<Fragment>();


        fragments.add(new settings());
        fragments.add(new Search());
        fragments.add(new Feed());
        fragments.add(new notifications());
        fragments.add(new profile());
////

////
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),  fragments);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter((PagerAdapter) adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        /*TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0) {
                    tab.setTag("Settings");
                    tab.setIcon(R.drawable.ic_settings);
                }else if(position == 1) {
                    tab.setTag("Search");
                    tab.setIcon(R.drawable.ic_search);
                }else if(position == 2) {
                    tab.setTag("News Feed");
                    tab.setIcon(R.drawable.ic_news_feed);
                }else if(position == 3) {
                    tab.setTag("Notifications");
                    tab.setIcon(R.drawable.ic_notifications);
                }else if(position == 4) {
                    tab.setTag("Profile");
                    tab.setIcon(R.drawable.ic_profile);
                }
            }
        });


    tabLayoutMediator.attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getApplicationContext(), tab.getTag().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
//pageradapter
    //timestamp+idposts


}