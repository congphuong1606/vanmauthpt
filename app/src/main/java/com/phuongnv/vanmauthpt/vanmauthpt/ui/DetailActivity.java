package com.phuongnv.vanmauthpt.vanmauthpt.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.phuongnv.vanmauthpt.vanmauthpt.R;
import com.phuongnv.vanmauthpt.vanmauthpt.event.OnTaskFinished;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;
    private String htmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deital);
        ButterKnife.bind(this);
        getBundle();
//        webview.loadData(htmlData, "text/html", "UTF-8");
    }

    private void getBundle() {
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("path");
        String link = intent.getStringExtra("link");
        String name =intent.getStringExtra("name");
//        htmlData=getData(fileName+".html");
        getDataFromUrl(link,fileName);
    }

    private void getDataFromUrl(String link, final String fileName) {
        new RetrieveFeedTask(new OnTaskFinished() {
            @Override
            public void onFeedRetrieved(String feeds) {

                String html=feeds;
                html=html.split("<div class=\"entry\">")[1];
                html=html.split("<div class=\"kk-star-ratings  bottom-right rgt\"")[0];

                String[] a=html.split("<ul");
                if(a.length>0){
                    html=html.split("<ul")[0];
                }


                html=html.replace("href","styless");
                html=html.replace("src","styless");
                html=html.replace("<img","<img  style=\"display:none\"");
                html=html.replace("\">(<a","display:none\">(<a");
                html=html.replace("\">(Kenhvanmau.com)","display:none\">(Kenhvanmau.com)");
                html=html.replace("http://kenhvanmau.com/","http://phuongcong.com/");
                html=html.replace("<strong>(Kenhvanmau.com)","<strong  style=\"display:none\">");
                html=html.replace("style=\"float:none;margin:","style=\"display:none;margin:");
                html=html.replace("data-ad-client=","e=");
                html=html.replace("height=","heightt=");

                html=html.replace("Mời c&aacute;c bạn xem th&ecirc;m:&nbsp;</strong>","</strong>");
                html=html.replace("Mời c&aacute;c bạn xem th&ecirc;m","");

                webview.loadData(html, "text/html", "UTF-8");
                saveFile(html,fileName);
                //do whatever you want to do with the feeds
            }
        }).execute(link);
    }
    private void saveFile(String htmlCOntent, String fileName) {
        String filename = fileName+".html";
        String outputString = htmlCOntent;
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        try {
            File secondFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator, filename);
            try {
                // Make sure the Pictures directory exists.
                path.mkdirs();

                secondFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream fos = new FileOutputStream(secondFile);

            fos.write(outputString.getBytes());
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File secondInputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator, filename);
            InputStream secondInputStream = new BufferedInputStream(new FileInputStream(secondInputFile));
            BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            secondInputStream.close();
            Log.d("File", "File contents: " + total);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getData(String fileName) {
        StringBuilder buf = new StringBuilder();
        InputStream json = null;
        try {
            json = getAssets().open(fileName);
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
        } catch (UnsupportedEncodingException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = buf.toString();
        return html;
    }



    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
        String HTML_response = "";

        OnTaskFinished onOurTaskFinished;


        public RetrieveFeedTask(OnTaskFinished onTaskFinished) {
            onOurTaskFinished = onTaskFinished;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]); // enter your url here which to download

                URLConnection conn = url.openConnection();

                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;

                while ((inputLine = br.readLine()) != null) {
                    // System.out.println(inputLine);
                    HTML_response += inputLine;
                }
                br.close();

                System.out.println("Done");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return HTML_response;
        }

        @Override
        protected void onPostExecute(String feed) {
            onOurTaskFinished.onFeedRetrieved(feed);
        }
    }
}
