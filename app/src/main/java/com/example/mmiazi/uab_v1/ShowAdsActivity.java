package com.example.mmiazi.uab_v1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mmiazi.uab_v1.receivedAds_fragments.Ad1;
import com.example.mmiazi.uab_v1.receivedAds_fragments.Ad2;
import com.example.mmiazi.uab_v1.receivedAds_fragments.Ad3;
import com.example.mmiazi.uab_v1.receivedAds_fragments.Ad4;
import com.example.mmiazi.uab_v1.receivedAds_fragments.Ad5;
import com.example.mmiazi.uab_v1.receivedAds_fragments.AdStruct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowAdsActivity extends AppCompatActivity implements Ad1.OnFragmentInteractionListener, Ad2.OnFragmentInteractionListener, Ad3.OnFragmentInteractionListener, Ad4.OnFragmentInteractionListener, Ad5.OnFragmentInteractionListener {

    PagerAdapter adapterViewPager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("ads");
    static AdStruct[] ads = new AdStruct[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ads);
        myRef.keepSynced(true);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AdStruct ad = data.getValue(AdStruct.class);
                    Log.d("test", ad.toString());
                    ads[i++] = ad;
                }
                ViewPager vpPager = findViewById(R.id.vpPager);
                adapterViewPager = new PagerAdapter(getSupportFragmentManager());
                vpPager.setAdapter(adapterViewPager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        //TODO: set the number of fragments........
        private static int NUM_ITEMS = 5;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Ad1.newInstance(ads[0]);
                case 1:
                    return Ad2.newInstance(ads[1]);
                case 2:
                    return Ad3.newInstance(ads[2]);
                case 3:
                    return Ad3.newInstance(ads[3]);
                case 4:
                    return Ad3.newInstance(ads[4]);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
