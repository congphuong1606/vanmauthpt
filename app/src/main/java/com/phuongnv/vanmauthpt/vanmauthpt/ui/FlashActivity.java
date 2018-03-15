package com.phuongnv.vanmauthpt.vanmauthpt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.phuongnv.vanmauthpt.vanmauthpt.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlashActivity extends AppCompatActivity {

    @BindView(R.id.activity_splash)
    RelativeLayout activitySplash;
    @BindView(R.id.imv_splass)
    ImageView imvSplass;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flash);
        ButterKnife.bind(this);
        Glide.with(this).asGif().load(getResources().getDrawable(R.drawable.splash_ic)).into(imvSplass);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(FlashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }

    @OnClick({R.id.activity_splash, R.id.imv_splass, R.id.progressBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_splash:
                break;
            case R.id.imv_splass:
                break;
            case R.id.progressBar:
                break;
        }
    }
}
