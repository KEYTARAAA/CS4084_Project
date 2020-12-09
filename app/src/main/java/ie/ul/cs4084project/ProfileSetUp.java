package ie.ul.cs4084project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class ProfileSetUp extends AppCompatActivity {

    protected static final String USER_TYPE = "USER_TYPE";
    protected static final String BIO = "BIO";
    protected static final String GENDER = "GENDER";
    protected static final String AGE = "AGE";
    protected static final String DOB = "DOB";
    protected static final String GOAL = "GOAL";
    protected static final String PROFILE_PIC = "PROFILE_PIC";
    protected static final String DISPLAY_NAME = "DISPLAY_NAME";
    protected static final String GENDER_IMAGE = "GENDER_IMAGE";
    protected static final String GOAL_IMAGE = "GOAL_IMAGE";
    protected String name, id, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_up);
        Intent intent=getIntent();
        name = intent.getStringExtra(MainActivity.NAME);
        id = intent.getStringExtra(MainActivity.ID);
        email = intent.getStringExtra(MainActivity.EMAIL);
    }
}