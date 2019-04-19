package com.example.harshitnagpal.service_application;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class downloadService extends Service {
    private boolean success = false;
    public static final String NOTIFICATION = "service receiver";

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        downloadService getService() {
            return downloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId){
        String urlPath = intent.getStringExtra("Url");
        String fileName = intent.getStringExtra("File");
        new downloadPDF ().execute(urlPath,fileName);
        return START_STICKY;
    }

    class downloadPDF extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
        }

        protected String doInBackground(String... params) {
            int count;
            File output = new File(getBaseContext().getFilesDir(),params[1]);
            try {

                System.out.println("Downloading " + params[1]);
                URL url = new URL(params[0]);

                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                OutputStream outputStream = new FileOutputStream(output.getPath());
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    outputStream.write(data, 0, count);

                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                success = true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            if (success) flagSuccess (params[1]);
            else flagFailed(params[1]);
            publishResults(params[1]);
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("onPostExecute");
            stopSelf();
        }

        private void publishResults(String filename) {
            Intent intent = new Intent(NOTIFICATION);
            intent.putExtra("file", filename);
            sendBroadcast(intent);
        }

        private void flagFailed (String filename) {
            if (filename.equals(downloadFiles.name1)) downloadFiles.flag1 = -1;
            else if (filename.equals(downloadFiles.name2)) downloadFiles.flag2 = -1;
            else if (filename.equals(downloadFiles.name3)) downloadFiles.flag3 = -1;
            else if (filename.equals(downloadFiles.name4)) downloadFiles.flag4 = -1;
            else if (filename.equals(downloadFiles.name5)) downloadFiles.flag5 = -1;
        }

        private void flagSuccess (String filename) {
            if (filename.equals(downloadFiles.name1)) downloadFiles.flag1 = 2;
            else if (filename.equals(downloadFiles.name2)) downloadFiles.flag2 = 2;
            else if (filename.equals(downloadFiles.name3)) downloadFiles.flag3 = 2;
            else if (filename.equals(downloadFiles.name4)) downloadFiles.flag4 = 2;
            else if (filename.equals(downloadFiles.name5)) downloadFiles.flag5 = 2;
        }
    }
}

