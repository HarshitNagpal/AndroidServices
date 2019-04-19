package com.example.harshitnagpal.service_application;

import android.support.v7.app.AppCompatActivity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;



public class downloadFiles extends AppCompatActivity {


    public static final String file1 = "https://www.cisco.com/web/about/ac79/docs/innov/IoE.pdf";
    public static final String file2 = "https://www.cisco.com/c/dam/en_us/about/ac79/docs/innov/IoE_Economy.pdf";
    public static final String file3 = "https://www.cisco.com/c/dam/en_us/solutions/industries/docs/gov/everything-for-cities.pdf";
    public static final String file4 = "http://www.orimi.com/pdf-test.pdf";
    public static final String file5 = "http://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf";
    static String currDownloadLink;

    public static final String name1 = file1.substring(file1.lastIndexOf("/") + 1, file1.length());
    public static final String name2 = file2.substring(file2.lastIndexOf("/") + 1, file2.length());
    public static final String name3 = file3.substring(file3.lastIndexOf("/") + 1, file3.length());
    public static final String name4 = file4.substring(file4.lastIndexOf("/") + 1, file4.length());
    public static final String name5 = file5.substring(file5.lastIndexOf("/") + 1, file5.length());
    static String currFileName;


    static TextView fileView1;
    static TextView fileView2;
    static TextView fileView3;
    static TextView fileView4;
    static TextView fileView5;
    static TextView displayFileView;

    public static int flag1 = 0;
    public static int flag2 = 0;
    public static int flag3 = 0;
    public static int flag4 = 0;
    public static int flag5 = 0;
    static int currFileFlag;

    static String downloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerReceiver(broadcasrR , new IntentFilter(
                downloadService.NOTIFICATION));
        fileView1 = (TextView) findViewById(R.id.textViewFile1);
        fileView2 = (TextView) findViewById(R.id.textViewFile2);
        fileView3 = (TextView) findViewById(R.id.textViewFile3);
        fileView4 = (TextView) findViewById(R.id.textViewFile4);
        fileView5 = (TextView) findViewById(R.id.textViewFile5);
        refreshAllFileViews();
        downloadPath = getFilesDir().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAllFileViews();
        registerReceiver(broadcasrR , new IntentFilter(
                downloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcasrR );
    }

    public void downloadAllFiles (View v) {
        Toast.makeText(this, "Starting all downloads !", Toast.LENGTH_SHORT).show();
        fileDownloadStart(file1, name1);
        flag1 = 1;
        refreshDisplay(1);

        fileDownloadStart(file2, name2);
        flag2 = 1;
        refreshDisplay(2);

        fileDownloadStart(file3, name3);
        flag3 = 1;
        refreshDisplay(3);

        fileDownloadStart(file4, name4);
        flag4 = 1;
        refreshDisplay(4);

        fileDownloadStart(file5, name5);
        flag5 = 1;
        refreshDisplay(5);
    }

    private void fileDownloadStart (String link, String file) {
        Intent intent = new Intent(getBaseContext(), downloadService.class);
        intent.putExtra("Url", link);
        intent.putExtra("File", file);
        startService(intent);
    }

    private void refreshDisplay (int x) {
        if (x > 5 || x < 1) {
            System.out.println("Wrong input ");
            return;
        }

        if (x == 1) {
            currFileFlag = flag1;
            displayFileView = fileView1;
            currFileName = name1;
            currDownloadLink = file1;
        } else if (x == 2) {
            currFileFlag = flag2;
            displayFileView = fileView2;
            currFileName = name2;
            currDownloadLink = file2;
        } else if (x == 3) {
            currFileFlag = flag3;
            displayFileView = fileView3;
            currFileName = name3;
            currDownloadLink = file3;
        } else if (x == 4) {
            currFileFlag = flag4;
            displayFileView = fileView4;
            currFileName = name4;
            currDownloadLink = file4;
        } else {
            currFileFlag = flag5;
            displayFileView = fileView5;
            currFileName = name5;
            currDownloadLink = file5;
        }

        if (currFileFlag == 0) displayFileView.setText("Download Link " + x + " - " + currDownloadLink);
        else if (currFileFlag == 1) displayFileView.setText(currFileName + " is downloading...");
        else if (currFileFlag == 2) displayFileView.setText(currFileName + " is successfully downloaded." );
        else if (currFileFlag == -1) displayFileView.setText(currFileName + " downloaded failed!");
        else displayFileView.setText("unknown internal error");
    }

    private void refreshAllFileViews () {
        refreshDisplay(1);
        refreshDisplay(2);
        refreshDisplay(3);
        refreshDisplay(4);
        refreshDisplay(5);
    }

    private BroadcastReceiver broadcasrR = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int x;
            if (bundle != null) {
                String filename = bundle.getString("file");
                System.out.println("Downloaded File - " + filename);
                if (filename.equals(name1)) x=1;
                else if (filename.equals(name2)) x=2;
                else if (filename.equals(name3)) x=3;
                else if (filename.equals(name4)) x=4;
                else x=5;
                refreshDisplay(x);
            }
        }
    };
}

