package es.rafapuig.persistence.preferences;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    public static final float DEFAULT_OPACITY = 1.0f;
    public static final int DEFAULT_BACKCOLOR = Color.WHITE;

    int layoutBackgroundColor = DEFAULT_BACKCOLOR;
    float imageTransparency = DEFAULT_OPACITY;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPreferences();
        updateView();
    }

    @Override
    protected void onPause() {
        storePreferences();
        super.onPause();
    }

    void loadPreferences() {
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
        layoutBackgroundColor = prefs.getInt("background_color", DEFAULT_BACKCOLOR);
        imageTransparency = prefs.getFloat("image_transparency", DEFAULT_OPACITY);
    }

    void storePreferences() {
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE); //Always private mode
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("background_color", layoutBackgroundColor);
        editor.putFloat("image_transparency", imageTransparency);

        editor.commit();
    }

    private void updateView() {
        image = this.findViewById(R.id.imageView);
        image.setAlpha(imageTransparency);

        ConstraintLayout layout = this.findViewById(R.id.containerLayout);
        layout.setBackgroundColor(layoutBackgroundColor);
    }

    public void onClickOpenConfigurationCallback(View view) {
        Intent configActivity = new Intent(this, ConfigActivity.class);

        configActivity.putExtra("color", layoutBackgroundColor);
        configActivity.putExtra("transparency", imageTransparency);

        configActivityLauncher.launch(configActivity);
    }

    ActivityResultLauncher configActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> onActivityResultCallback(result)
    );

    private void onActivityResultCallback(ActivityResult result) {

        if (result.getResultCode() == Activity.RESULT_OK) {

            Intent data = result.getData();

            if (data.hasExtra("transparency")) {
                imageTransparency = data.getFloatExtra("transparency", DEFAULT_OPACITY);
            }
            if (data.hasExtra("color")) {
                layoutBackgroundColor = data.getIntExtra("color", DEFAULT_BACKCOLOR);
            }
            updateView();
        }
    }
}