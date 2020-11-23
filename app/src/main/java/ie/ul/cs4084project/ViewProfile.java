package ie.ul.cs4084project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ViewProfile extends AppCompatActivity {

    private static  String id;
    private static  String email;
    public static  String name;
    private byte[] bytes;
    private String viewProfileId;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.ID);
        email = intent.getStringExtra(MainActivity.EMAIL);
        name = intent.getStringExtra(MainActivity.NAME);
        viewProfileId = intent.getStringExtra(Search.VIEW_PROFILE_ID);
        storageReference = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_view_profile);
        final ArrayList<Fragment>fragments = new ArrayList<Fragment>();


        Task<byte[]> task = storageReference.child("profilesInfo")
                .child(viewProfileId + ".txt").getBytes(1000000000).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        bytes = task.getResult();
                        fragments.add(new profile(new String(bytes, StandardCharsets.UTF_8), viewProfileId));
                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),  fragments);
                        ViewPager viewPager = findViewById(R.id.viewPagerViewProfile);
                        viewPager.setAdapter(adapter);
                    }
                });
    }


}