package com.phuongnv.vanmauthpt.vanmauthpt.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phuongnv.vanmauthpt.vanmauthpt.R;
import com.phuongnv.vanmauthpt.vanmauthpt.adapter.LessonAdapter;
import com.phuongnv.vanmauthpt.vanmauthpt.data.Lesson;
import com.phuongnv.vanmauthpt.vanmauthpt.event.OnClickLessonAdapter;
import com.phuongnv.vanmauthpt.vanmauthpt.event.OnTaskFinished;
import com.phuongnv.vanmauthpt.vanmauthpt.utils.Database;
import com.phuongnv.vanmauthpt.vanmauthpt.utils.KeyboardUtil;
import com.rw.keyboardlistener.KeyboardUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnClickLessonAdapter {


    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.btnS)
    ImageButton btnS;
    @BindView(R.id.btnCloseS)
    ImageButton btnCloseS;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.marginLeft)
    View marginLeft;
    @BindView(R.id.rcvLesson)
    RecyclerView rcvLesson;
    private SQLiteDatabase database;
    ArrayList<Lesson> lessons = new ArrayList<>();
    private LessonAdapter lessonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAdapter();
        keyBoard();
       // readData("10");
        readDataAll();
        //Spiner
        setupSpinner();
    }

    private void setAdapter() {
        rcvLesson.setLayoutManager(new
                GridLayoutManager(this, 1));
        rcvLesson.setHasFixedSize(true);
        lessonAdapter = new LessonAdapter(lessons,this);
        rcvLesson.setAdapter(lessonAdapter);
    }

    private void readData(String c) {
        database = Database.initDatabase(this, "vanmauthpt.sqlite");
        Cursor cursor = database.rawQuery("select * from lesson where class = "+c, null);
        lessons.clear();
//        listSeach.clear();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    String id = cursor.getString(0);
                    String name = cursor.getString(1);
                    String des = cursor.getString(2);
                    String chapter = cursor.getString(3);
                    String path = cursor.getString(4);
                    String link = cursor.getString(5);
                    lessons.add(new Lesson(id, name, des, chapter, path,link));
                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        lessonAdapter.notifyDataSetChanged();
        rcvLesson.smoothScrollToPosition(0);


    }
    private void readDataAll() {
        database = Database.initDatabase(this, "vanmauthpt.sqlite");
        Cursor cursor = database.rawQuery("select * from lesson", null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    String path = cursor.getString(4);
                    String link = cursor.getString(5);
                    getDataFromUrl(link,path);
                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }



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

                //webview.loadData(html, "text/html", "UTF-8");
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




    @Override
    public void onClick(String path,String link, String name) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("link", link);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private void keyBoard() {
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    spinner.setVisibility(View.GONE);
                    btnCloseS.setVisibility(View.VISIBLE);
                    marginLeft.setVisibility(View.VISIBLE);
                    edtSearch.setBackgroundDrawable(getResources().getDrawable(R.drawable.edtb));
                } else {
                    edtSearch.setBackgroundDrawable(getResources().getDrawable(R.drawable.edt));
                    btnCloseS.setVisibility(View.GONE);
                    marginLeft.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    edtSearch.clearFocus();
                    edtSearch.setText("");

                }
//                Toast.makeText(ChapterActivity.this, "keyboard visible: "+isVisible,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupSpinner() {
        spinner.setItems("VĂN MẪU LỚP 10", "VĂN MẪU LỚP 11", "VĂN MẪU LỚP 12");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (item){
                    case "VĂN MẪU LỚP 11":
                        readData("11");
                        break;
                    case "VĂN MẪU LỚP 10":
                        readData("10");
                        break;
                    case "VĂN MẪU LỚP 12":
                        readData("12");
                        break;

                }
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @OnClick({R.id.spinner, R.id.btnS, R.id.btnCloseS, R.id.edt_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.spinner:
                break;
            case R.id.btnS:
                break;
            case R.id.btnCloseS:
                KeyboardUtil.hideKeyboard(this, edtSearch);
                break;
            case R.id.edt_search:
                break;
        }
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
