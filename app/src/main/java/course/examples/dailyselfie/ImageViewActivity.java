package course.examples.dailyselfie;

/**
 * Created by DVTrukhin on 7/18/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the Intent used to start this Activity
        Intent intent = getIntent();

        // Make a new ImageView
        imageView = new ImageView(getApplicationContext());

        // Retrieve uri from intent
        String uri = intent.getStringExtra(MainActivity.EXTRA_RES_ID);
        Drawable d = Drawable.createFromPath(uri);
        imageView.setImageDrawable(d);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        // Get the ID of the image to display and set it as the image for this ImageView
        this.setContentView(imageView);
    }

}