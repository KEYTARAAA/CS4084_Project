package ie.ul.cs4084project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Summary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Summary extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private List<Fragment> fragments;
    private Bitmap compressor;
    private StorageReference storageReference;
    private Uri imageUri, download_uri;
    private String name, email, id;
    private FirebaseFirestore db;

    public Summary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Summary.
     */
    // TODO: Rename and change types and number of parameters
    public static Summary newInstance(String param1, String param2) {
        Summary fragment = new Summary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = FirebaseFirestore.getInstance();
        fragments = new ArrayList<Fragment>();

        fragments.add(new ProfilePosts());
        fragments.add(new ProfilePicsAndVids());
        fragments.add(new ProfileAbout(getArguments()));
        fragments.add(new ProfileFriends());

        Intent intent = getActivity().getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        email = intent.getStringExtra(MainActivity.EMAIL);
        id = intent.getStringExtra(MainActivity.ID);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        ViewPager viewPager = getActivity().findViewById(R.id.viewPagerSummary);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayoutSummary);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Posts");
        tabLayout.getTabAt(0).setTag("Posts");
        tabLayout.getTabAt(1).setText("Pics");
        tabLayout.getTabAt(1).setTag("Pics");
        tabLayout.getTabAt(2).setText("About");
        tabLayout.getTabAt(2).setTag("About Feed");
        tabLayout.getTabAt(3).setText("Friends");
        tabLayout.getTabAt(3).setTag("Friends");


        ImageView imageView = getActivity().findViewById(R.id.imageViewSummary);
        if(getArguments().getString(ProfileSetUp.PROFILE_PIC)!=null) {
            imageUri = Uri.parse(getArguments().getString(ProfileSetUp.PROFILE_PIC));
            imageView.setImageURI(imageUri);
        }else {
            imageView.setImageResource(R.drawable.icon_profile);
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageView.getLayoutParams().width = (int) (metrics.widthPixels * 0.297619);
        imageView.getLayoutParams().height = (int) (metrics.widthPixels * 0.297619);
        imageView.requestLayout();

        TextView textView = getActivity().findViewById(R.id.textViewName);
        textView.setText(name);

        textView = getActivity().findViewById(R.id.textViewDisplayName);
        textView.setText("@" + getArguments().getString(ProfileSetUp.DISPLAY_NAME));

        textView = getActivity().findViewById(R.id.textViewSummaryBio);
        textView.setText(getArguments().getString(ProfileSetUp.BIO));

        Button button = getActivity().findViewById(R.id.buttonSummarySubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageReference = FirebaseStorage.getInstance().getReference();
                uploadProfilePic();
            }
        });
    }


    private void uploadProfilePic() {
        //String id = "CM1111PBHATH";
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        File newFile = new File(imageUri.getPath());
        try {
            compressor = new Compressor(getContext()).setMaxHeight((int) (metrics.widthPixels * 0.8))
                    .setMaxWidth((int) (metrics.widthPixels * 0.8)).setQuality(50).compressToBitmap(newFile);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Compression Error : " + e, Toast.LENGTH_SHORT);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        compressor.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] thumb = byteArrayOutputStream.toByteArray();
        UploadTask image_past = storageReference.child("profilePics").child(id + ".jpeg").putBytes(thumb);
        image_past.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadImage(task);
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), "Image Error : " + error, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void uploadImage(Task<UploadTask.TaskSnapshot> task) {
        if(task != null){
            Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful());
            download_uri = urlTask.getResult();
            next();
        }else{
            download_uri = imageUri;
        }
    }

    private void next(){


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            Map<String, String> data = new HashMap<>();
            String dn = getArguments().getString(ProfileSetUp.DISPLAY_NAME);
            data.put("Display Name", dn);
            data.put("ID", id);
            data.put("Name", name);
            data.put("Profile Picture", download_uri.toString());
            db.collection("Display Names").document(dn).set(data);
            data = new HashMap<>();
            data.put("Name", name);
            data.put("ID", id);
            data.put("Display Name", dn);
            data.put("Profile Picture", download_uri.toString());
            db.collection("Names").document(dn).set(data);
            storageReference.child("profilesInfo")
                    .child(id + ".txt")
                    .putBytes(("User Type = " + getArguments().getString(ProfileSetUp.USER_TYPE)+
                            "\nName = " + name +
                            "\nDate Of Birth = " + getArguments().getString(ProfileSetUp.DOB)+
                            "\nAge = " + getArguments().getString(ProfileSetUp.AGE)+
                            "\nGender = " + getArguments().getString(ProfileSetUp.GENDER)+
                            "\nGoal = " + getArguments().getString(ProfileSetUp.GOAL)+
                            "\nBio = " + getArguments().getString(ProfileSetUp.BIO)+
                            "\nDisplay Name = " + getArguments().getString(ProfileSetUp.DISPLAY_NAME)+
                            "\nProfile Picture = " + download_uri
                    ).getBytes()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                    Task<Uri> task1= storageReference.child("profilesInfo")
                            .child(id + ".txt").getDownloadUrl();
                    task1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            done(uri);
                        }
                    });
                }
            });

        }
    }

    private void done(Uri uri){


        CollectionReference posts = db.collection("Profiles");
        Map<String, String> data = new HashMap<>();
        data.put("ID", id);
        data.put("File", uri.toString());
        posts.document(id).set(data);
        //db.collection("Profiles").document(id).;
        //posts.document(id).collection("Friends");


        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra(MainActivity.ID, id);
        intent.putExtra(MainActivity.EMAIL, email);
        intent.putExtra(MainActivity.NAME, name);

        Toast.makeText(getActivity(), "Sign in successful!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}