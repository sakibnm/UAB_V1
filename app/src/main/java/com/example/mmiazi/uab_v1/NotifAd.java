package com.example.mmiazi.uab_v1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mmiazi.uab_v1.receivedAds_fragments.AdStruct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotifAd extends AppCompatActivity {
    private AdStruct ad;
    private String name;
    private float rating;
    private String userPhoto;
    private String productPhoto;
    private String comment;
    private String productName;
    private int fontColor;
    private RatingBar ratingBar;
    private TextView tv_Title;
    private TextView tv_Name;
    private TextView tv_Comment;
    private ImageView iv_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_ad);
        ratingBar = findViewById(R.id.ratingBar_Notif);
        tv_Title = findViewById(R.id.tv_Notif_Title);
        tv_Name = findViewById(R.id.tv_Notif_Name);
        tv_Comment = findViewById(R.id.tv_Notif_Comment);
        iv_User = findViewById(R.id.iv_Notif_photo);

        String command = getIntent().getStringExtra("command").trim();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);
        switch (command) {
            case "notifyAd1":
                DatabaseReference adRef = databaseReference.child("adstoSend").child("ad1");
                adRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AdStruct ad = dataSnapshot.getValue(AdStruct.class);
                        ratingBar.setRating(ad.getRating());
                        tv_Title.setText(ad.getProductName());
                        tv_Name.setText(ad.getName());
                        tv_Comment.setText(ad.getComment());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case "notifyAd2":
                break;
            case "notifyAd3":
                break;
            default:
                break;
        }
    }
}
