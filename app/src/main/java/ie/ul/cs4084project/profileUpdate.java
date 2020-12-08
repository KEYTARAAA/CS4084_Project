package ie.ul.cs4084project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class profileUpdate extends AppCompatActivity {

    private EditText editTextBio;
    private String mParam2;
    private FirebaseFirestore db;
    private String name,id,email,userType,dateOB,age,gender,goal,displayName,profilePic;
    private Button pu1;
    private byte[] bytes;
    private StorageReference storageReference;

    public profileUpdate(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_profile_update);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        id = intent.getStringExtra(MainActivity.ID);
        email = intent.getStringExtra(MainActivity.EMAIL);
        storageReference = FirebaseStorage.getInstance().getReference();
        pu1 = findViewById(R.id.pu1);
        editTextBio = findViewById(R.id.editProfileBio);
        Task<byte[]> task = storageReference.child("profilesInfo")
                .child(id + ".txt").getBytes(1000000000).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        bytes = task.getResult();
                        String s = new String(bytes, StandardCharsets.UTF_8);
                        String[] array = s.split("\n");
                        userType = array[0].substring(12);
                        dateOB = array[2].substring(16);
                        age = array[3].substring(6);
                        gender = array[4].substring(9);
                        goal = array[5].substring(7);
                        displayName = array[7].substring(15);
                        profilePic = array[8].substring(18);

                    }
                });
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



    }


    public void updateBio(View view){

        String newBio = editTextBio.getText().toString();
        storageReference.child("profilesInfo")
                .child(id + ".txt")
                .putBytes(("User Type = " + userType +
                                "\nName = " + name +
                                "\nDate Of Birth = " +dateOB +
                                "\nAge = " + age+
                                "\nGender = " + gender +
                                "\nGoal = " + goal +
                                "\nBio = " + newBio +
                                "\nDisplay Name = " + displayName +
                                "\nProfile Picture = " + profilePic ).getBytes()) ;






        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra(MainActivity.NAME,name);
        intent.putExtra(MainActivity.ID,id);
        intent.putExtra(MainActivity.EMAIL,email);
        startActivity(intent);



    }


}