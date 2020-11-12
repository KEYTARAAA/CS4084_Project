package ie.ul.cs4084project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_up);
    }
}