package com.phuongnv.vanmauthpt.vanmauthpt.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.phuongnv.vanmauthpt.vanmauthpt.R;
import com.phuongnv.vanmauthpt.vanmauthpt.adapter.LessonAdapter;
import com.phuongnv.vanmauthpt.vanmauthpt.data.Lesson;
import com.phuongnv.vanmauthpt.vanmauthpt.event.OnClickLessonAdapter;
import com.phuongnv.vanmauthpt.vanmauthpt.utils.Database;
import com.phuongnv.vanmauthpt.vanmauthpt.utils.KeyboardUtil;
import com.rw.keyboardlistener.KeyboardUtils;

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
        readData();
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

    private void readData() {
        database = Database.initDatabase(this, "vanmauthpt.sqlite");
        Cursor cursor = database.rawQuery("select * from lesson where class =10", null);
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
                    lessons.add(new Lesson(id, name, des, chapter, path));
                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        lessonAdapter.notifyDataSetChanged();
        rcvLesson.smoothScrollToPosition(0);


    }
    @Override
    public void onClick(String path, String name) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("path", path);
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


}
