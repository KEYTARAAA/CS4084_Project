package ie.ul.cs4084project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Feed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Feed extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Button postButton;
    private EditText postText;
    private ImageView postImage;
    private ProgressDialog progressDialog;
    private String id, name, email;
    private int devicePixelWidth;

    private Uri imageUri;
    private Bitmap compressor;


    private String mParam1;
    private String mParam2;

    public Feed() {
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    postButton = getView().findViewById(R.id.postButton);
        postText = getView().findViewById(R.id.postText);
        postImage = getView().findViewById(R.id.postImage);
        postImage.setImageResource(R.drawable.ic_camera);
        progressDialog = new ProgressDialog(getContext());
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        name = getActivity().getIntent().getStringExtra(MainActivity.NAME);
        id = getActivity().getIntent().getStringExtra(MainActivity.ID);;
        email = getActivity().getIntent().getStringExtra(MainActivity.EMAIL);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        devicePixelWidth = metrics.widthPixels;
        load10Posts();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feed.
     */
    // TODO: Rename and change types and number of parameters
    public static Feed newInstance(String param1, String param2) {
        Feed fragment = new Feed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }



    private void load10Posts(){


        final LinearLayout scrollView = getView().findViewById(R.id.scroll);
        //scrollView.setLayoutManager(new LinearLayoutManager(getContext()));
        db.collection("Profiles").document(id).collection("News Feed").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<DocumentSnapshot> posts = task.getResult().getDocuments();
                            if (posts.size()>500000000) {
                                int tb = 0;
                                for (int i = (posts.size() - 1); i > (posts.size() - 6); i--) {
                                    DocumentSnapshot document = posts.get(i);
                                    TextView p = new TextView(getContext());
                                    p.setText(document.get("post").toString() + '\n' + '~' + document.get("author") + "\n\n");
                                    scrollView.addView(p);
                                }
                            } else {
                                for (int i = (posts.size() - 1); i >-1; i--) {
                                    DocumentSnapshot document = posts.get(i);
                                    if (!document.get("image").toString().equals("")) {

                                        ImageView imageView = new ImageView(getContext());
                                        new DownloadImageTask(imageView)
                                                .execute(document.get("image").toString());
                                        TextView textView = new TextView(getContext());
                                        textView.setText(document.get("post").toString() + '\n' + '~' + document.get("name") + "\n\n");


                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                                        scrollView.addView(imageView, layoutParams);
                                        scrollView.addView(textView);

                                    } else{
                                        TextView p = new TextView(getContext());
                                        p.setText(document.get("post").toString() + '\n' + '~' + document.get("name") + "\n\n");
                                        scrollView.addView(p);
                                    }
                                }
                            }
                        }else {
                            Log.w("tag", "Error retrieving documents!", task.getException());
                        }
                    }
                }
        );
    }

    public void post(View v){
        progressDialog.setMessage("Storing data...");
        progressDialog.show();


        if((TextUtils.isEmpty(postText.getText())) && (imageUri == null)){
            Toast.makeText(getContext(), "Nothing to post", Toast.LENGTH_SHORT);

        }else {
            if(imageUri != null){
            File newFile = new File(imageUri.getPath());
            try {
                compressor = new Compressor(getContext()).setMaxHeight((int)(devicePixelWidth*0.8))
                        .setMaxWidth((int)(devicePixelWidth*0.8)).setQuality(50).compressToBitmap(newFile);
            } catch (IOException e) {

                Toast.makeText(getContext(), "Compression Error : " + e, Toast.LENGTH_SHORT);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            compressor.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] thumb = byteArrayOutputStream.toByteArray();
            UploadTask image_past = storageReference.child("user_image").child(System.currentTimeMillis()+"-"+id + ".jpeg").putBytes(thumb);
            image_past.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        uploadImage(task);
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), "Image Error : " + error, Toast.LENGTH_SHORT);
                        progressDialog.dismiss();
                    }
                }
            });
        }else {
                uploadPost();
            }
        }
    }

    private void uploadImage(Task<UploadTask.TaskSnapshot> task) {
        final Uri download_uri;
        if(task != null){
            Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful());
            download_uri = urlTask.getResult();
        }else{
            download_uri = imageUri;
        }
        final Map<String,String> userData = new HashMap<String, String>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("id", id);
        userData.put("post", postText.getText().toString());
        userData.put("image", download_uri.toString());
        String time = "";
        time += System.currentTimeMillis();
        db.collection("Pictures").document(time + "-" + id).set(userData).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Upload Successful!", Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getContext(), "FireStore Error : "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT);
                }
            }
        });


        db.collection("Profiles").document(id).collection("Posts").document(time).set(userData).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Upload Successful!", Toast.LENGTH_SHORT);
                        }else{
                            Toast.makeText(getContext(), "FireStore Error : "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    }
                });

        final String finalTime = time;
        db.collection("Profiles").document(id).collection("Friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> friends = task.getResult().getDocuments();
                for (DocumentSnapshot ds : friends) {
                    String friendId = ds.get("ID").toString();
                    db.collection("Profiles").document(friendId).collection("News Feed").document(finalTime).set(userData);
                }
            }
        });



        postText.setText("");
        postImage.setImageResource(R.drawable.ic_camera);
        imageUri = null;
    }



    private void uploadPost() {

        Map<String,String> userData = new HashMap<String, String>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("id", id);
        userData.put("post", postText.getText().toString());
        userData.put("image", "");
        userData.put("post", postText.getText().toString());
        String time = "";
        time += System.currentTimeMillis();
        db.collection("Pictures").document(time + "-" + id).set(userData).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Upload Successful!", Toast.LENGTH_SHORT);
                        }else{
                            Toast.makeText(getContext(), "FireStore Error : "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    }
                });

        db.collection("Profiles").document(id).collection("Posts").document(time).set(userData).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Upload Successful!", Toast.LENGTH_SHORT);
                        }else{
                            Toast.makeText(getContext(), "FireStore Error : "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    }
                });

        postText.setText("");
    }

    public void addImage(View view){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                }else{
                    pickImage();
                }
            }else{
                pickImage();
            }
    }

    private void pickImage() {
        Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT);
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView tv = getView().findViewById(R.id.FeedText);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == getActivity().RESULT_OK){
                imageUri = result.getUri();
                postImage.setImageURI(imageUri);

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            };
        };
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