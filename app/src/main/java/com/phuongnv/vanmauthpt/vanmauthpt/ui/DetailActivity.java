package com.phuongnv.vanmauthpt.vanmauthpt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.phuongnv.vanmauthpt.vanmauthpt.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
        webview.loadData(htmlData, "text/html", "UTF-8");
    }

    private void getBundle() {
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("path");
        String name =intent.getStringExtra("name");
        htmlData=getData(fileName+".html");
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
}
