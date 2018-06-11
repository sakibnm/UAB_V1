package com.example.mmiazi.uab_v1.createAds_fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CAd2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CAd2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CAd2 extends Fragment {

    private OnFragmentInteractionListener mListener;
    View view;

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

    public CAd2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * this fragment using the provided parameters.
     * @return A new instance of fragment CAd2.
     */
    // TODO: Rename and change types and number of parameters
    public static CAd2 newInstance(CAdStruct cad) {
        CAd2 fragment = new CAd2();
        Bundle args = new Bundle();
        if (cad != null) {
            args.putString("name", cad.getName());
            args.putFloat("rating", cad.getRating());
            args.putString("userPhoto", cad.getUserPhoto());
            args.putString("productPhoto", cad.getProductPhoto());
            args.putString("comment", cad.getComment());
            args.putString("productName", cad.getProductName());
            args.putBoolean("nameIsChecked", cad.isNameIsChecked());
            args.putBoolean("ratingIsChecked", cad.isRatingIsChecked());
            args.putBoolean("commentIsChecked", cad.isCommentIsChecked());
            args.putBoolean("userPhotoIsChecked", cad.isUserPhotoIsChecked());
        }

        fragment.setArguments(args);
        return fragment;
    }

    private void readBundle(Bundle args){
        if(args != null){
            this.name = args.getString("name");
            this.rating = args.getFloat("rating");
            this.comment = args.getString("comment");
            this.commentIsChecked = args.getBoolean("commentIsChecked");
            this.userPhoto = args.getString("userPhoto");
            this.productPhoto = args.getString("productPhoto");
            this.productName = args.getString("productName");
            this.nameIsChecked = args.getBoolean("nameIsChecked");
            this.ratingIsChecked = args.getBoolean("ratingIsChecked");
            this.userPhotoIsChecked = args.getBoolean("userPhotoIsChecked");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_cad2, container, false);

        final Switch switchCad2 = view.findViewById(R.id.switchCad2);
        TextView productNameCad2 = view.findViewById(R.id.tv_cad2_Title);
        final RatingBar ratingBarCad2 = view.findViewById(R.id.ratingBar_cad2);
        final CheckBox cb_ratingBarCad2 = view .findViewById(R.id.cb_Rating_cad2);
        final ImageView iv_Cad2_userPhoto  = view.findViewById(R.id.iv_cad2_photo);
        final CheckBox ctv_cad2_name = view.findViewById(R.id.ctv_cad2_name);
        final CheckBox ctv_cad2_reviewCheck = view.findViewById(R.id.ctv_cad2_reviewCheck);
        final EditText ctv_cad2_review = view.findViewById(R.id.ctv_cad2_review);
        final CheckBox ctv_cad2_photo = view.findViewById(R.id.ctv_cad2_photo);

        readBundle(getArguments());

        ratingBarCad2.setRating(rating);
        cb_ratingBarCad2.setChecked(false);
        ratingBarCad2.setEnabled(false);
        LayerDrawable stars = (LayerDrawable) ratingBarCad2.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        productNameCad2.setText(productName);
        new GetImageFromURL().execute(productPhoto);

        switchCad2.setChecked(false);
        ctv_cad2_name.setChecked(false);
        ctv_cad2_photo.setChecked(false);
        ctv_cad2_reviewCheck.setChecked(false);

        cb_ratingBarCad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_ratingBarCad2.isChecked()){
                    ratingBarCad2.setEnabled(true);
                    ratingIsChecked = true;
                }
                else {
                    ratingBarCad2.setEnabled(false);
                    ratingIsChecked = false;
                }
            }
        });


        ctv_cad2_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctv_cad2_name.isChecked()){
                    ctv_cad2_name.setText(name);
                    nameIsChecked = true;
                }
                else {
                    ctv_cad2_name.setText("Share name");
                    nameIsChecked = false;
                }
            }
        });



        ctv_cad2_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctv_cad2_photo.isChecked()){
                    userPhotoIsChecked = true;
                    try {
                        iv_Cad2_userPhoto.setImageBitmap(decodeFromFireBase64(userPhoto));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    userPhotoIsChecked = false;
                    iv_Cad2_userPhoto.setImageResource(R.drawable.user_photo_not_selected);
                }
            }
        });

        ctv_cad2_reviewCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewPut = ctv_cad2_review.getText().toString().trim();
                if(reviewPut.equals("") || reviewPut.equals(null)){
                    ctv_cad2_reviewCheck.setChecked(false);
                    Toast.makeText(getContext(),"Please write the review first!", Toast.LENGTH_SHORT).show();
                    commentIsChecked = false;
                }else if(ctv_cad2_reviewCheck.isChecked()){
                    commentIsChecked = true;
                    comment = reviewPut;
                }else{
                    commentIsChecked = false;
                }
            }
        });

        switchCad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchCad2.isChecked()){
                    String currentUserName;
                    String currentUserRating;
                    String currentUserPhoto;
                    String currentUserComment;
                    if(ctv_cad2_name.isChecked()) currentUserName = name;
                    else currentUserName = "false";

                    if(cb_ratingBarCad2.isChecked()) currentUserRating = String.valueOf(ratingBarCad2.getRating());
                    else currentUserRating = "false";

                    if(ctv_cad2_photo.isChecked())currentUserPhoto = userPhoto;
                    else currentUserPhoto = "false";

                    if(ctv_cad2_reviewCheck.isChecked()){
                        currentUserComment = ctv_cad2_review.getText().toString();
                    }
                    else currentUserComment = "false";
//                    TempStruct currentUser = new TempStruct();

                    TempStruct currentUser = new TempStruct(currentUserComment, currentUserName, currentUserPhoto, currentUserRating);

                    databaseReference.child("currentUser").child("createAd2").setValue(currentUser);

                    databaseReference.child("signalToAdmin").child("command").setValue("advertised2");

                }else {
                    TempStruct currentUser = new TempStruct("false", "false", "false", "false");
                    databaseReference.child("currentUser").child("createAd2").setValue(currentUser);
                    databaseReference.child("signalToAdmin").child("command").setValue("empty");

                }

            }
        });

        return view;

    }

    private Bitmap decodeFromFireBase64(String photoUrl) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(photoUrl, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            ConstraintLayout constraintLayout = view.findViewById(R.id.back_cad2);
            Drawable dr = new BitmapDrawable(bitmap);
            dr.setBounds(0, 0, 480, 840);
            constraintLayout.setBackground(dr);
            Log.d("test", "background set");

        }
    }
}
