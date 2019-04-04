package com.envy.patrema.envy_patrema;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Adapter.DoctorOnboardingAdapter;
import com.marcoscg.licenser.Library;
import com.marcoscg.licenser.License;
import com.marcoscg.licenser.LicenserDialog;

public class DoctorOnboarding extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotsLayout;
    DoctorOnboardingAdapter onboardingAdapter;
    TextView[] mDots;
    Button mBtnNext, mBtnPrev;
    int mCurrentPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("docFirstStart", false);
        editor.apply();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotsLayout = findViewById(R.id.dotsLayout);

        mBtnNext = findViewById(R.id.btnNext);
        mBtnPrev = findViewById(R.id.btnPrev);

        onboardingAdapter = new DoctorOnboardingAdapter(this);

        mSlideViewPager.setAdapter(onboardingAdapter);


        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnNext.getText().equals("Accept")){
                    termsAndConditions();
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstStart", false);
                    editor.apply();
                }
                else
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

    }

    private void termsAndConditions() {
        // TODO: before 20/03/2019
        // accept terms and conditions
        new LicenserDialog(this)
                .setTitle("Terms and Conditons")
                .setCustomNoticeTitle("Notices for files:")
                .setBackgroundColor(Color.WHITE) // Optional
                .setLibrary(new Library("Android Support Libraries",
                        "https://developer.android.com/topic/libraries/support-library/index.html",
                        License.APACHE))
                .setLibrary(new Library("Example Library",
                        "https://github.com/marcoscgdev",
                        License.APACHE))
                .setLibrary(new Library("Licenser",
                        "https://github.com/marcoscgdev/Licenser",
                        License.MIT))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), DoctorDashboard.class));
                    }
                })
                .show();
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.dot_inactive));

            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.active_dots));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;

            if (i == 0){
                mBtnNext.setEnabled(true);
                mBtnPrev.setEnabled(false);
                mBtnPrev.setVisibility(View.INVISIBLE);
                mBtnNext.setText("Next");
                mBtnPrev.setText("");
            }
            else if (i == mDots.length -1){
                mBtnNext.setEnabled(true);
                mBtnPrev.setEnabled(true);
                mBtnPrev.setVisibility(View.VISIBLE);
                mBtnNext.setText("Accept");
                mBtnPrev.setText("Back");
            }
            else {
                mBtnNext.setEnabled(true);
                mBtnPrev.setEnabled(true);
                mBtnPrev.setVisibility(View.VISIBLE);
                mBtnNext.setText("Next");
                mBtnPrev.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
