package ie.ul.cs4084project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {
private boolean profileComplete = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String sViewProfileId, sId, sString;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  String string;
    private String userType, name, dateOB, age, gender, goal, bio, displayName, profilePic;
    private ArrayList<Fragment> fragments;
    private String viewProfileId, id;
    private Button friend;
    private FirebaseFirestore db;

    public profile(String s, String viewProfileId) {
         String[] array = s.split("\n");
        userType = array[0].substring(12);
        name = array[1].substring(7);
        dateOB = array[2].substring(16);
        age = array[3].substring(6);
        gender = array[4].substring(9);
        goal = array[5].substring(7);
        bio = array[6].substring(6);
        displayName = array[7].substring(15);
        profilePic = array[8].substring(18);

        this.viewProfileId = viewProfileId;

        Bundle bundle = new Bundle();
        bundle.putString(ProfileSetUp.USER_TYPE, userType);
        bundle.putString(ProfileSetUp.DOB, dateOB);
        bundle.putString(ProfileSetUp.AGE, age);
        bundle.putString(ProfileSetUp.GENDER, gender);
        bundle.putString(ProfileSetUp.GOAL, goal);
        if(gender.compareToIgnoreCase("male") == 0){
            bundle.putInt(ProfileSetUp.GENDER_IMAGE, R.drawable.male_blue);
        }else if(gender.compareToIgnoreCase("female") == 0){
            bundle.putInt(ProfileSetUp.GENDER_IMAGE, R.drawable.female_pink);
        }else if(gender.compareToIgnoreCase("other") == 0){
            bundle.putInt(ProfileSetUp.GENDER_IMAGE, R.drawable.other_gender_rainbow);
        }

        if (goal.compareToIgnoreCase("lose weight")==0){
            bundle.putInt(ProfileSetUp.GOAL_IMAGE, R.drawable.weight_green);
        }else if (goal.compareToIgnoreCase("gain muscle")==0){
            bundle.putInt(ProfileSetUp.GOAL_IMAGE, R.drawable.muscle_green);
        }

        fragments = new ArrayList<Fragment>();

        fragments.add(new ProfilePosts(viewProfileId));
        fragments.add(new ProfilePicsAndVids());
        fragments.add(new ProfileAbout(bundle));
        fragments.add(new ProfileFriends(viewProfileId));
        sString = string;
        sId = id;
        sViewProfileId = viewProfileId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile(sString, sViewProfileId);
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
        id = getActivity().getIntent().getStringExtra(MainActivity.ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageView imageView = getActivity().findViewById(R.id.imageViewProfile);
        new DownloadImageTask(imageView)
                .execute(profilePic);

        friend = getActivity().findViewById(R.id.buttonFriendProfile);
        setFriend();
        TextView textView = getActivity().findViewById(R.id.textViewProfileDisplayName);
        textView.setText("@"+displayName);
        textView = getActivity().findViewById(R.id.textViewProfileName);
        textView.setText(name);
        textView = getActivity().findViewById(R.id.textViewProfileBio);
        textView.setText(bio);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        ViewPager viewPager = getActivity().findViewById(R.id.viewPagerProfile);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayoutProfile);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Posts");
        tabLayout.getTabAt(0).setTag("Posts");
        tabLayout.getTabAt(1).setText("Pics");
        tabLayout.getTabAt(1).setTag("Pics");
        tabLayout.getTabAt(2).setText("About");
        tabLayout.getTabAt(2).setTag("About Feed");
        tabLayout.getTabAt(3).setText("Friends");
        tabLayout.getTabAt(3).setTag("Friends");
    }

    private void setFriend(){

        if(viewProfileId.compareTo(id) == 0){
            friend.setVisibility(View.INVISIBLE);
        }else {
            checkFriends();
        }


    }

    private void checkFriends(){
        friend.setBackgroundColor(Color.rgb(0,255,0));
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> data = new HashMap();
                data.put("Name", name);
                data.put("Display Name", displayName);
                data.put("ID", viewProfileId);
                data.put("Profile Picture", profilePic);
                db.collection("Profiles").document(id).collection("Friends")
                        .document(viewProfileId).set(data);


                 db.collection("Profiles").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         DocumentSnapshot ds = task.getResult();
                         String myProfilePicture = ds.get("Profile Picture").toString();
                         String myName = ds.get("Name").toString();
                         String myId = ds.get("ID").toString();
                         String myDisplayName = ds.get("Display Name").toString();

                         Map<String, String> myData = new HashMap<>();
                         myData.put("Name", myName);
                         myData.put("Display Name", myDisplayName);
                         myData.put("ID", myId);
                         myData.put("Profile Picture", myProfilePicture);
                         db.collection("Profiles").document(viewProfileId).collection("Friends")
                                 .document(id).set(myData);
                         checkFriends();
                     }
                 });
                checkFriends();
            }
        });
        db.collection("Profiles").document(id).collection("Friends").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> friends = task.getResult().getDocuments();
                for (DocumentSnapshot ds : friends) {
                    String compare = ds.get("ID").toString();
                    if(viewProfileId.compareToIgnoreCase(compare) == 0){
                        friend.setBackgroundColor(Color.rgb(11, 140, 4));
                        friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.collection("Profiles").document(viewProfileId).collection("Friends").
                                        document(id).delete();
                                db.collection("Profiles").document(id).collection("Friends").
                                        document(viewProfileId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        checkFriends();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
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
}