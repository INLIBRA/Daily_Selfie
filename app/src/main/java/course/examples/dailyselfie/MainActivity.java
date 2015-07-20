package course.examples.dailyselfie;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.provider.MediaStore;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import java.util.Date;
import android.os.Environment;
import java.text.SimpleDateFormat;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    protected static final String EXTRA_RES_ID = "POS";
    private static final long ALARM_DELAY = 2 * 60 * 1000L;


    private AlarmManager mAlarmManager;
    private PendingIntent mNotificationReceiverPendingIntent;
    private Intent mNotificationReceiverIntent;
    ListView list;
    ArrayList<String> uriList = new ArrayList<>();
    Uri fileUri;
    String mCurrentPhotoPath;
    ImageListAdapter mAdapter;
    File storageDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startAlarm();

        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            String path = files[i].getAbsolutePath();
            uriList.add(path);
        }
        mAdapter = new ImageListAdapter(MainActivity.this, uriList);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                Toast.makeText(MainActivity.this, "You Clicked at image " + (position + 1), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_TRADITIONAL);
                builder.setMessage(R.string.dialog_message)
                        .setPositiveButton(R.string.enlarge, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(MainActivity.this,
                                        ImageViewActivity.class);

                                // pass image ura to ImageViewActivity
                                intent.putExtra(EXTRA_RES_ID, uriList.get(position));

                                // Start the ImageViewActivity
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                File[] files = storageDir.listFiles();
                                uriList.remove(position);
                                mAdapter.notifyDataSetChanged();
                                files[position].delete();

                            }
                        }).show();

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_camera) {

            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            fileUri = Uri.fromFile(photoFile);
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            uriList.add(mCurrentPhotoPath);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void startAlarm() {
        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(MainActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mNotificationReceiverIntent, 0);

        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + ALARM_DELAY,
                ALARM_DELAY, mNotificationReceiverPendingIntent);
    }

}
