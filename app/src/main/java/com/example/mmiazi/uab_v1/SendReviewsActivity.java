package com.example.mmiazi.uab_v1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mmiazi.uab_v1.createAds_activities.CAd1Activity;
import com.example.mmiazi.uab_v1.createAds_activities.CAd2Activity;
import com.example.mmiazi.uab_v1.createAds_activities.CAd3Activity;
import com.example.mmiazi.uab_v1.createAds_activities.CAdStruct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SendReviewsActivity extends AppCompatActivity {

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

    TextView tv_cad1;
    TextView tv_cad2;
    TextView tv_cad3;
    ImageView iv_cad1;
    ImageView iv_cad2;
    ImageView iv_cad3;
    RelativeLayout review1;
    RelativeLayout review2;
    RelativeLayout review3;

    String gender;
    static CAdStruct[] cads = new CAdStruct[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reviews);

        tv_cad1 = findViewById(R.id.tv_cad1);
        tv_cad2 = findViewById(R.id.tv_cad2);
        tv_cad3 = findViewById(R.id.tv_cad3);
        iv_cad1 = findViewById(R.id.iv_cad1);
        iv_cad2 = findViewById(R.id.iv_cad2);
        iv_cad3 = findViewById(R.id.iv_cad3);
        review1 = findViewById(R.id.review1);
        review2 = findViewById(R.id.review2);
        review3 = findViewById(R.id.review3);

        gender = "other";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gender = dataSnapshot.child("currentUser").child("gender").getValue().toString();
                Log.d("test", "Gender: "+gender);
                DataSnapshot cadsRef;
                ViewPager sendReviewsPager;
                Iterable<DataSnapshot> cadsChildren;
                switch(gender){
                    case "Female":
                        cadsRef = dataSnapshot.child("cads").child("female");
                        cadsChildren = cadsRef.getChildren();
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
                        if (i >= 2) {
                            tv_cad1.setText(cads[0].getProductName());
                            tv_cad2.setText(cads[1].getProductName());
                            tv_cad3.setText(cads[2].getProductName());
                            new GetImageFromURL().execute("1", cads[0].getProductPhoto());
                            new GetImageFromURL().execute("2", cads[1].getProductPhoto());
                            new GetImageFromURL().execute("3", cads[2].getProductPhoto());
                        }
//                        sendReviewsPager = findViewById(R.id.sendReviewsPager);
//                        pagerAdapater = new PagerAdapater(getSupportFragmentManager());
//                        sendReviewsPager.setAdapter(pagerAdapater);
                        break;

                    case "male":
                        cadsRef = dataSnapshot.child("cads").child("male");
                        cadsChildren = cadsRef.getChildren();
                        i = 0;
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
//                        sendReviewsPager = findViewById(R.id.sendReviewsPager);
//                        pagerAdapater = new PagerAdapater(getSupportFragmentManager());
//                        sendReviewsPager.setAdapter(pagerAdapater);
                        if (i >= 2) {
                            tv_cad1.setText(cads[0].getProductName());
                            tv_cad2.setText(cads[1].getProductName());
                            tv_cad3.setText(cads[2].getProductName());
                            new GetImageFromURL().execute("1", cads[0].getProductPhoto());
                            new GetImageFromURL().execute("2", cads[1].getProductPhoto());
                            new GetImageFromURL().execute("3", cads[2].getProductPhoto());
                        }
                        break;

                    case "other":
                        cadsRef = dataSnapshot.child("cads").child("other");
                        cadsChildren = cadsRef.getChildren();
                        i = 0;
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
                        if (i >= 2) {
                            tv_cad1.setText(cads[0].getProductName());
                            tv_cad2.setText(cads[1].getProductName());
                            tv_cad3.setText(cads[2].getProductName());
                            new GetImageFromURL().execute("1", cads[0].getProductPhoto());
                            new GetImageFromURL().execute("2", cads[1].getProductPhoto());
                            new GetImageFromURL().execute("3", cads[2].getProductPhoto());
                        }
//                        sendReviewsPager = findViewById(R.id.sendReviewsPager);
//                        pagerAdapater = new PagerAdapater(getSupportFragmentManager());
//                        sendReviewsPager.setAdapter(pagerAdapater);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        review1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CAd1Activity.class);
                intent.putExtra("cad1", cads[0]);
                startActivity(intent);
            }
        });

        review2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CAd2Activity.class);
                intent.putExtra("cad2", cads[1]);
                startActivity(intent);
            }
        });

        review3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CAd3Activity.class);
                intent.putExtra("cad3", cads[2]);
                startActivity(intent);
            }
        });

    }

    class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        String cadNo = "";

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                cadNo = strings[0];
                URL url = new URL(strings[1]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            switch (cadNo) {
                case "1":
                    ((ImageView) findViewById(R.id.iv_cad1)).setImageBitmap(bitmap);
                case "2":
                    ((ImageView) findViewById(R.id.iv_cad2)).setImageBitmap(bitmap);
                case "3":
                    ((ImageView) findViewById(R.id.iv_cad3)).setImageBitmap(bitmap);
            }
        }
    }
}
