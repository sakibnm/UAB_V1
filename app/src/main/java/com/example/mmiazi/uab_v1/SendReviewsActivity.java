package com.example.mmiazi.uab_v1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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


public class SendReviewsActivity extends AppCompatActivity {

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
                            cads[i] = data.getValue(CAdStruct.class);
                            String currentUID = dataSnapshot.child("currentUser").child("uID").getValue().toString();
                            cads[i].setName(dataSnapshot.child("users").child(currentUID).child("firstName").toString()
                                    +" "+dataSnapshot.child("users").child(currentUID).child("lastName").toString());
                            cads[i].setComment("");
                            cads[i].setCommentIsChecked(false);
                            cads[i].setNameIsChecked(false);
                            cads[i].setRating(0);
                            cads[i].setRatingIsChecked(false);
                            cads[i].setUserPhoto(dataSnapshot.child("users").child(currentUID).child("imageDownloadUrl").toString());
                            cads[i].setUserPhotoIsChecked(false);
                            Log.d("test", "cads test: "+cads[i].toString());
                            i++;
                        }

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
