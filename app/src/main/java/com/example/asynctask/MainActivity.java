package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asynctask.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding bind;
    Bitmap bmimg;
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskExample asyncTaskExample = new AsyncTaskExample();
                asyncTaskExample.execute("https://learn.g2crowd.com/hubfs/UI-Trends.jpg");
                //this code sets a click listener on a button to create and execute
                // a new instance of an AsyncTaskExample class that downloads an image from a specified URL.
                // When the button is clicked,
                // the AsyncTaskExample instance is executed, and the image is downloaded in the background.
            }
        });
    }
    private class AsyncTaskExample extends AsyncTask<String, String, Bitmap>{
        //String (for the URL of the image to download),
        // String (for the progress update value, which is not used in this example),
        // and Bitmap (for the result value of the background operation).

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(MainActivity.this);
            //creates a new ProgressDialog instance and
            // sets the activity context to MainActivity.this.
            p.setMessage("Please wait.....Image is being downloaded.");
            p.setIndeterminate(false);
            // This sets the progress dialog to show the progress bar.
            p.setCancelable(false);
            // This sets the progress dialog to be not cancelable by the user.
            p.show();
            // This shows the progress dialog on the screen.
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL ImageUrl = new URL(strings[0]);
                // This creates a URL object from the first element of the strings parameter,
                // which should contain the URL of the image to download.
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                //This opens a connection to the URL using an HttpURLConnection object.
                conn.setDoInput(true);
                //This sets the flag indicating that this HttpURLConnection allows input.
                conn.connect();
                //This connects to the resource referenced by the URL.
                InputStream is = conn.getInputStream();
                //This gets an InputStream that reads from this open connection.
                BitmapFactory.Options options = new BitmapFactory.Options();
                //This creates an options object for the BitmapFactory.
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                //This sets the preferred color space for the bitmap to RGB_565,
                // which is a format that uses 16 bits per pixel.
                bmimg= BitmapFactory.decodeStream(is, null, options);
                //This decodes an input stream into a Bitmap using the specified options.
                // The resulting Bitmap object is stored in the bmimg variable.
            } catch (IOException e) {
               e.printStackTrace();
            }
            return bmimg;
        }

        @Override //update ui of the background operation result
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            try {
                Thread.sleep(3000); // wait for 3 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            p.hide(); //This hides the progress dialog.
            bind.image.setImageBitmap(bitmap); //This sets the Bitmap object bitmap as the image
                                                // for the ImageView object bind.image
        }
    }
    //Asynchronous tasks, or AsyncTask, is a built-in class in Android
    // that allows you to perform background operations and update the UI on the main thread.

    // do not forget to allow Internet permission in manifest file.

}