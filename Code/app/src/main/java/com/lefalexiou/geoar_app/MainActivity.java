package com.lefalexiou.geoar_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArFragment arFragment;
    private int itemSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        RadioButton viewButton = findViewById(R.id.radio_view);

        viewButton.setChecked(true);
        itemSelector = 0;

        assert arFragment != null;
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            switch (itemSelector) {
                case 0:
                    ViewObject viewObject = new ViewObject(arFragment, this, R.layout.placeholderview);
                    viewObject.createViewRenderable(hitResult.createAnchor());
                    break;
                case 1:
                    VideoObject videoObject = new VideoObject(arFragment, this, hitResult.createAnchor());
                    break;
                case 2:
                case 3:
                    ModelObject modelObject = new ModelObject(arFragment, this, itemSelector);
                    modelObject.createViewRenderable(hitResult.createAnchor());
                    break;
            }
        }));
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_view:
                if (checked)
                    // View selected
                    itemSelector = 0;
                break;
            case R.id.video:
                if (checked)
                    // Video model selected
                    itemSelector = 1;
                break;
            case R.id.model:
                if (checked)
                    // 3d transformable model selected
                    itemSelector = 2;
                break;
            case R.id.animated_model:
                if (checked)
                    // animated model selected
                    itemSelector = 3;
        }
    }
}