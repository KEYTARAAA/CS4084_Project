package ie.ul.cs4084project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFriends#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFriends extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static String viewProfileId;
    private FirebaseFirestore db;
    private LinearLayout linearLayout;
    private EditText editText;
    private Button button;

    public ProfileFriends(String viewProfileId) {
        this.viewProfileId = viewProfileId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFriends.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFriends newInstance(String param1, String param2) {
        ProfileFriends fragment = new ProfileFriends(viewProfileId);
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
        Intent intent = getActivity().getIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = getActivity().findViewById(R.id.scrollFriend);
        editText = getActivity().findViewById(R.id.editTextFriendSearch);
        button = getActivity().findViewById(R.id.buttonSearchFriend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        load();

    }

    private void search(){
        linearLayout.removeAllViews();
        String search = editText.getText().toString();
        if(search.charAt(0) == '@'){
            String sub = search.substring(1);
            findDisplay(sub);
        }else if(search.isEmpty()){
            load();
        }else{
            findName(search);
        }
    }

    private void findDisplay(final String name){
        db.collection("Profiles").document(viewProfileId).collection("Friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> users = task.getResult().getDocuments();
                for (final DocumentSnapshot ds : users) {
                    String n = ds.get("Display Name").toString();
                    if(name.compareToIgnoreCase(n) == 0){

                        CardView cardView = new CardView(getContext());
                        LinearLayout layout = new LinearLayout(getContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(layoutParams);
                        imageView.getLayoutParams().width = 200;
                        imageView.getLayoutParams().height = 200;
                        imageView.requestLayout();
                        new DownloadImageTask(imageView)
                                .execute(ds.get("Profile Picture").toString());
                        TextView textView = new TextView(getContext());
                        textView.setText("@" + ds.get("Display Name").toString() + "\n" + ds.get("Name").toString());
                        layout.addView(imageView);
                        layout.addView(textView);
                        cardView.addView(layout);
                        cardView.setRadius(80);
                        cardView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                getProfile(ds.get("ID").toString());
                            }
                        });
                        linearLayout.addView(cardView);
                    }
                }
            }
        });


    }

    private void findName(final String name){
        db.collection("Profiles").document(viewProfileId).collection("Friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> users = task.getResult().getDocuments();
                for (final DocumentSnapshot ds : users) {
                    String n = ds.get("Name").toString();
                    String compare = "";
                    if(n.length()>name.length()) {
                        compare = n.substring(0, name.length());
                    }else {
                        compare = name.substring(0, n.length());
                    }
                    if(name.compareToIgnoreCase(compare) == 0){

                        CardView cardView = new CardView(getContext());
                        LinearLayout layout = new LinearLayout(getContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(layoutParams);
                        imageView.getLayoutParams().width = 200;
                        imageView.getLayoutParams().height = 200;
                        imageView.requestLayout();
                        new DownloadImageTask(imageView)
                                .execute(ds.get("Profile Picture").toString());
                        TextView textView = new TextView(getContext());
                        textView.setText("@" + ds.get("Display Name").toString() + "\n" + ds.get("Name").toString());
                        layout.addView(imageView);
                        layout.addView(textView);
                        cardView.addView(layout);
                        cardView.setRadius(80);
                        cardView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                getProfile(ds.get("ID").toString());
                            }
                        });
                        linearLayout.addView(cardView);
                    }
                }
            }
        });

    }

    private void load(){
        db.collection("Profiles").document(viewProfileId).collection("Friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> users = task.getResult().getDocuments();
                for (final DocumentSnapshot ds : users) {
                    String n = ds.get("Name").toString();
                    String d = ds.get("Display Name").toString();
                    String p = ds.get("Profile Picture").toString();
                    final String i = ds.get("ID").toString();

                        CardView cardView = new CardView(getContext());
                        LinearLayout layout = new LinearLayout(getContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(layoutParams);
                        imageView.getLayoutParams().width = 200;
                        imageView.getLayoutParams().height = 200;
                        imageView.requestLayout();
                        new DownloadImageTask(imageView)
                                .execute(p);
                        TextView textView = new TextView(getContext());
                        textView.setText("@" + d + "\n" + n);
                        layout.addView(imageView);
                        layout.addView(textView);
                        cardView.addView(layout);
                        cardView.setRadius(80);
                        cardView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                getProfile(i);
                            }
                        });
                        linearLayout.addView(cardView);
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

    private void getProfile(String id) {
        Intent intent = new Intent(getActivity(), ViewProfile.class);
        intent.putExtra(MainActivity.ID, getActivity().getIntent().getStringExtra(MainActivity.ID));
        intent.putExtra(MainActivity.EMAIL, getActivity().getIntent().getStringExtra(MainActivity.EMAIL));
        intent.putExtra(MainActivity.NAME, getActivity().getIntent().getStringExtra(MainActivity.NAME));
        intent.putExtra(Search.VIEW_PROFILE_ID, id);
        startActivity(intent);
    }
}