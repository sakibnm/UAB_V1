package com.example.mmiazi.uab_v1.createAds_activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmiazi.uab_v1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CAd3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad3);

        final String name;
        float rating;
        String productName;
        final String[] comment = new String[1];
        final String userPhoto;
        String productPhoto;
        final boolean[] nameIsChecked = new boolean[1];
        final boolean[] ratingIsChecked = new boolean[1];
        final boolean[] commentIsChecked = new boolean[1];
        final boolean[] userPhotoIsChecked = new boolean[1];

        CAdStruct cad = (CAdStruct) getIntent().getSerializableExtra("cad3");

        name = cad.getName();
        rating = cad.getRating();
        productName = cad.getProductName();
        comment[0] = cad.getComment();
        userPhoto = cad.getUserPhoto();
        productPhoto = cad.productPhoto;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        final ImageView switchCad3 = findViewById(R.id.switchCad3);
        TextView productNameCad3 = findViewById(R.id.tv_cad3_Title);
        final RatingBar ratingBarCad3 = findViewById(R.id.ratingBar_cad3);
        final CheckBox cb_ratingBarCad3 = findViewById(R.id.cb_Rating_cad3);
        final ImageView iv_Cad3_userPhoto = findViewById(R.id.iv_cad3_photo);
        final CheckBox ctv_cad3_name = findViewById(R.id.ctv_cad3_name);
        final CheckBox ctv_cad3_reviewCheck = findViewById(R.id.ctv_cad3_reviewCheck);
        final EditText ctv_cad3_review = findViewById(R.id.ctv_cad3_review);
        final CheckBox ctv_cad3_photo = findViewById(R.id.ctv_cad3_photo);

        ratingBarCad3.setRating(rating);
        cb_ratingBarCad3.setChecked(false);
        ratingBarCad3.setEnabled(false);
        LayerDrawable stars = (LayerDrawable) ratingBarCad3.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        productNameCad3.setText(productName);
        new GetImageFromURL().execute(productPhoto);

        //switchCad3.setChecked(false);
        ctv_cad3_name.setChecked(false);
        ctv_cad3_photo.setChecked(false);
        ctv_cad3_reviewCheck.setChecked(false);

        cb_ratingBarCad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_ratingBarCad3.isChecked()) {
                    ratingBarCad3.setEnabled(true);
                    ratingIsChecked[0] = true;
                } else {
                    ratingBarCad3.setEnabled(false);
                    ratingIsChecked[0] = false;
                }
            }
        });


        ctv_cad3_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctv_cad3_name.isChecked()) {
                    ctv_cad3_name.setText(name);
                    nameIsChecked[0] = true;
                } else {
                    ctv_cad3_name.setText("Share name");
                    nameIsChecked[0] = false;
                }
            }
        });

        ctv_cad3_reviewCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewPut = ctv_cad3_review.getText().toString().trim();
                if (reviewPut.equals("") || reviewPut.equals(null)) {
                    ctv_cad3_reviewCheck.setChecked(false);
                    Toast.makeText(getBaseContext(), "Please write the review first!", Toast.LENGTH_SHORT).show();
                    commentIsChecked[0] = false;
                } else if (ctv_cad3_reviewCheck.isChecked()) {
                    commentIsChecked[0] = true;
                    comment[0] = reviewPut;
                } else {
                    commentIsChecked[0] = false;
                }
            }
        });

        ctv_cad3_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctv_cad3_photo.isChecked()) {
                    userPhotoIsChecked[0] = true;
                    try {
                        iv_Cad3_userPhoto.setImageBitmap(decodeFromFireBase64(userPhoto));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    userPhotoIsChecked[0] = false;
                    iv_Cad3_userPhoto.setImageResource(R.drawable.user_photo_not_selected);
                }
            }
        });

        switchCad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    String currentUserName;
                    String currentUserRating;
                    String currentUserPhoto;
                    String currentUserComment;
                    if (ctv_cad3_name.isChecked()) currentUserName = name;
                    else currentUserName = "false";

                    if (cb_ratingBarCad3.isChecked())
                        currentUserRating = String.valueOf(ratingBarCad3.getRating());
                    else currentUserRating = "false";

                    if (ctv_cad3_photo.isChecked()) currentUserPhoto = userPhoto;
                    else currentUserPhoto = "false";

                    if (ctv_cad3_reviewCheck.isChecked()) {
                        currentUserComment = ctv_cad3_review.getText().toString();
                    } else currentUserComment = "false";
//                    TempStruct currentUser = new TempStruct();

                    TempStruct currentUser = new TempStruct(currentUserComment, currentUserName, currentUserPhoto, currentUserRating);

                    databaseReference.child("currentUser").child("createAd3").setValue(currentUser);
                    databaseReference.child("signalToAdmin").child("command").setValue("advertised3");
                    finish();

                }

            }
        });


    }

    private Bitmap decodeFromFireBase64(String photoUrl) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(photoUrl, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
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
            ConstraintLayout constraintLayout = findViewById(R.id.back_cad3);
            Drawable dr = new BitmapDrawable(bitmap);
            dr.setBounds(0, 0, 480, 840);
            constraintLayout.setBackground(dr);
            Log.d("test", "background set");

        }
    }
}
