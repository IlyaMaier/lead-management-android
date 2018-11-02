package com.community.jboss.leadmanagement.intro;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.community.jboss.leadmanagement.R;
import com.community.jboss.leadmanagement.intro.fragments.IntroFragment;
import com.community.jboss.leadmanagement.intro.fragments.IntroFragment1;
import com.community.jboss.leadmanagement.intro.fragments.IntroFragment2;
import com.community.jboss.leadmanagement.main.MainActivity;

public class IntroActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    Button btnNext, btnBack;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.view_pager_intro);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            if (page != 0) {
                page--;
                viewPager.setCurrentItem(page);
                if (page == 0)
                    btnBack.setActivated(false);
            }
        });

        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            if (page < 2) {
                page++;
                viewPager.setCurrentItem(page);
            } else {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                switch (position){
                    case 0:
                        btnNext.setBackgroundColor(getResources().getColor(R.color.first_fragment));
                        btnBack.setBackgroundColor(getResources().getColor(R.color.first_fragment));
                        btnNext.setText(R.string.next);
                        break;
                    case 1:
                        btnNext.setBackgroundColor(getResources().getColor(R.color.second_fragment));
                        btnBack.setBackgroundColor(getResources().getColor(R.color.second_fragment));
                        btnNext.setText(R.string.next);
                        break;
                    case 2:
                        btnNext.setBackgroundColor(getResources().getColor(R.color.third_fragment));
                        btnBack.setBackgroundColor(getResources().getColor(R.color.third_fragment));
                        btnNext.setText(R.string.finish);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new IntroFragment();
                case 1:
                    return new IntroFragment1();
                case 2:
                    return new IntroFragment2();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

}
