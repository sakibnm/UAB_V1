package com.example.mmiazi.uab_v1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mmiazi.uab_v1.receivedAds_fragments.AdStruct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
    private String command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_from_admin_ad);
        ratingBar = findViewById(R.id.ratingBar_Notif);
        tv_Title = findViewById(R.id.tv_Notif_Title);
        tv_Name = findViewById(R.id.tv_Notif_Name);
        tv_Comment = findViewById(R.id.tv_Notif_Comment);
        iv_User = findViewById(R.id.iv_Notif_photo);
        final ConstraintLayout back_Notif = findViewById(R.id.back_Notif);

        command = "empty";

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        ArrayList<AdStruct> sentAdsFromAdmin = new ArrayList<AdStruct>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                command = dataSnapshot.child("signalFromAdmin").child("command").getValue().toString();
                Log.d("test", "Got Command from Admin: "+command);
                AdStruct ad = null;
                AdStruct ads[] = new AdStruct[3];
                Iterable<DataSnapshot> datas = dataSnapshot.child("adstoSend").getChildren();

                int j=0;
                int i=0;
                for(DataSnapshot data: datas){
                    ad = data.getValue(AdStruct.class);
                    ads[i] = ad;
                    Log.d("test2", "Got Ads from Admin: "+ads[i].toString());
                    i++;
                }

                if(ads!=null)switch (command) {
                    case "notifyAd1":
                        ratingBar.setRating(ads[0].getRating());
                        tv_Title.setText(ads[0].getProductName());
                        tv_Name.setText(ads[0].getName());
                        tv_Comment.setText(ads[0].getComment());
                        Picasso.get().load(ads[0].getUserPhoto()).into(iv_User);
                        new NotifAd.GetImageFromURL().execute(ads[0].getProductPhoto());
                        break;
                    case "notifyAd2":
                        ratingBar.setRating(ads[1].getRating());
                        tv_Title.setText(ads[1].getProductName());
                        tv_Name.setText(ads[1].getName());
                        tv_Comment.setText(ads[1].getComment());
                        Picasso.get().load(ads[1].getUserPhoto()).into(iv_User);
                        new NotifAd.GetImageFromURL().execute(ads[1].getProductPhoto());
                        break;
                    case "notifyAd3":
                        ratingBar.setRating(ads[2].getRating());
                        tv_Title.setText(ads[2].getProductName());
                        tv_Name.setText(ads[2].getName());
                        tv_Comment.setText(ads[2].getComment());
                        Picasso.get().load(ads[2].getUserPhoto()).into(iv_User);
                        new NotifAd.GetImageFromURL().execute(ads[2].getProductPhoto());
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
    class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                Log.d("test3", "Got Ads from Admin: "+url.toString());
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
            ConstraintLayout constraintLayout = findViewById(R.id.back_Notif);
            Drawable dr = new BitmapDrawable(bitmap);
            dr.setBounds(0, 0, 480, 840);
            constraintLayout.setBackground(dr);
            Log.d("test", "background set");

        }
    }
    @Override
    public void onBackPressed() {
        MainActivity.navLogout.setVisible(true);
        MainActivity.navCreateAd.setVisible(true);
        MainActivity.navInstructions.setVisible(true);
        super.onBackPressed();
    }
}
