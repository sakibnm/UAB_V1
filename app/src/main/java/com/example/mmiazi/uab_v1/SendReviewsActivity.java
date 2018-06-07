package com.example.mmiazi.uab_v1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.example.mmiazi.uab_v1.createAds_fragments.CAd1;
import com.example.mmiazi.uab_v1.createAds_fragments.CAd2;
import com.example.mmiazi.uab_v1.createAds_fragments.CAd3;
import com.example.mmiazi.uab_v1.createAds_fragments.CAdStruct;


public class SendReviewsActivity extends AppCompatActivity {

    PagerAdapater pagerAdapater;
    static CAdStruct[] cads = new CAdStruct[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reviews);
    }

    private static class PagerAdapater extends FragmentPagerAdapter{
        public PagerAdapater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CAd1.newInstance(cads[0]);
                case 1:
                    return CAd2.newInstance(cads[1]);
                case 2:
                    return CAd3.newInstance(cads[2]);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
