package com.example.mmiazi.uab_v1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mmiazi.uab_v1.createAds_fragments.CAd1;
import com.example.mmiazi.uab_v1.createAds_fragments.CAd2;
import com.example.mmiazi.uab_v1.createAds_fragments.CAd3;
import com.example.mmiazi.uab_v1.createAds_fragments.CAdStruct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SendReviewsActivity extends AppCompatActivity implements  CAd1.OnFragmentInteractionListener, CAd2.OnFragmentInteractionListener, CAd3.OnFragmentInteractionListener{

    PagerAdapater pagerAdapater;
    String name;
    float rating;
    String productName;
    String comment;
    String userPhoto;
    String productPhoto;
    boolean nameIsChecked;
    boolean ratingIsChecked;
    boolean commentIsChecked;
    boolean userPhotoIsChecked;

    String gender;
    static CAdStruct[] cads = new CAdStruct[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reviews);

        gender = "other";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gender = dataSnapshot.child("currentUser").child("gender").getValue().toString();
                Log.d("test", "Gender: "+gender);

                switch(gender){
                    case "Female":
                        DataSnapshot cadsRef = dataSnapshot.child("cads").child("female");
                        Iterable<DataSnapshot> cadsChildren = cadsRef.getChildren();
                        int i = 0;
                        for(DataSnapshot data: cadsChildren){
                            CAdStruct cad = data.getValue(CAdStruct.class);
                            String currentUID = dataSnapshot.child("currentUser").child("uID").getValue().toString();
                            cad.setName(dataSnapshot.child("users").child(currentUID).child("firstName").getValue().toString()
                                    +" "+dataSnapshot.child("users").child(currentUID).child("lastName").getValue().toString());
                            cad.setComment("");
                            cad.setCommentIsChecked(false);
                            cad.setNameIsChecked(false);
                            cad.setRating(0);
                            cad.setRatingIsChecked(false);
                            cad.setUserPhoto(dataSnapshot.child("users").child(currentUID).child("imageDownloadUrl").getValue().toString());
                            cad.setUserPhotoIsChecked(false);
                            if(cads[i] != null)Log.d("test", "cads test: "+cads[i].toString());
                            cads[i++] = cad;
                        }
                        ViewPager sendReviewsPager = findViewById(R.id.sendReviewsPager);
                        pagerAdapater = new PagerAdapater(getSupportFragmentManager());
                        sendReviewsPager.setAdapter(pagerAdapater);
                        break;

                    case "male":
                        break;

                    case "other":
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private static class PagerAdapater extends FragmentPagerAdapter{

        private static int NUM_ITEMS = 3;
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
            return NUM_ITEMS;
        }
    }
}
