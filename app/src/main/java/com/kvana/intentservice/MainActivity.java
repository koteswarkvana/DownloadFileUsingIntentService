package com.kvana.intentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

// https://www.vogella.com/tutorials/AndroidServices/article.html
public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String string = bundle.getString(DownloadFileUsingIntentService.FILEPATH);
                int resultCode = bundle.getInt(DownloadFileUsingIntentService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this,"Download complete. Download URI: " + string, Toast.LENGTH_LONG).show();
                    tvStatus.setText("Download done");
                } else {
                    Toast.makeText(MainActivity.this, "Download failed", Toast.LENGTH_LONG).show();
                    tvStatus.setText("Download failed");
                }
            }
        }
    };

    // NOTE: Need to give storage permission manually to download file.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.status);
        // Disable the user from taking screen shot of your activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadFileUsingIntentService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    // Download button click functionality.
    public void onClick(View view) {
        Intent intent = new Intent(this, DownloadFileUsingIntentService.class);
        // add information for the service which file to download and where to store
        intent.putExtra(DownloadFileUsingIntentService.FILENAME,"index.html");
        intent.putExtra(DownloadFileUsingIntentService.URL,"https://www.vogella.com/index.html");
//        intent.putExtra(DownloadFileUsingIntentService.URL,"https://www.youtube.com/watch?v=jlmyZ_x5vjc");
        startService(intent);
        tvStatus.setText("Service started");
    }
}
