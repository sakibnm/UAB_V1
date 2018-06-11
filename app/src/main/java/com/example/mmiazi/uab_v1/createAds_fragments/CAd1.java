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
 * {@link CAd1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CAd1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CAd1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters


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

    public CAd1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CAd1.
     */
    // TODO: Rename and change types and number of parameters
    public static CAd1 newInstance(CAdStruct cad) {
        CAd1 fragment = new CAd1();
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
            Log.d("test", "Received Data: "+cad.toString());
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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_cad1, container, false);

        final Switch switchCad1 = view.findViewById(R.id.switchCad1);
        TextView productNameCad1 = view.findViewById(R.id.tv_cad1_Title);
        final RatingBar ratingBarCad1 = view.findViewById(R.id.ratingBar_cad1);
        final CheckBox cb_ratingBarCad1 = view .findViewById(R.id.cb_Rating_cad1);
        final ImageView iv_Cad1_userPhoto  = view.findViewById(R.id.iv_cad1_photo);
        final CheckBox ctv_cad1_name = view.findViewById(R.id.ctv_cad1_name);
        final CheckBox ctv_cad1_reviewCheck = view.findViewById(R.id.ctv_cad1_reviewCheck);
        final EditText ctv_cad1_review = view.findViewById(R.id.ctv_cad1_review);
        final CheckBox ctv_cad1_photo = view.findViewById(R.id.ctv_cad1_photo);

        readBundle(getArguments());

        ratingBarCad1.setRating(rating);
        cb_ratingBarCad1.setChecked(false);
        ratingBarCad1.setEnabled(false);
        LayerDrawable stars = (LayerDrawable) ratingBarCad1.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        productNameCad1.setText(productName);
        new GetImageFromURL().execute(productPhoto);

        switchCad1.setChecked(false);
        ctv_cad1_name.setChecked(false);
        ctv_cad1_photo.setChecked(false);
        ctv_cad1_reviewCheck.setChecked(false);

        cb_ratingBarCad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_ratingBarCad1.isChecked()){
                    ratingBarCad1.setEnabled(true);
                    ratingIsChecked = true;
                }
                else {
                    ratingBarCad1.setEnabled(false);
                    ratingIsChecked = false;
                }
            }
        });

        if(ratingBarCad1.isEnabled()){
            ratingBarCad1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rating = ratingBarCad1.getRating();
                }
            });
        }

        ctv_cad1_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctv_cad1_name.isChecked()){
                    ctv_cad1_name.setText(name);
                    nameIsChecked = true;
                }
                else {
                    ctv_cad1_name.setText("Share name");
                    nameIsChecked = false;
                }
            }
        });


//        TODO: Check if the code updated for checkbox...
        ctv_cad1_reviewCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewPut = ctv_cad1_review.getText().toString().trim();
                if(reviewPut.equals("") || reviewPut.equals(null)){
                    ctv_cad1_reviewCheck.setChecked(false);
                    Toast.makeText(getContext(),"Please write the review first!", Toast.LENGTH_SHORT).show();
                    commentIsChecked = false;
                }else if(ctv_cad1_reviewCheck.isChecked()){
                    commentIsChecked = true;
                    comment = reviewPut;
                }else{
                    commentIsChecked = false;
                }
            }
        });

        ctv_cad1_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctv_cad1_photo.isChecked()){
                    userPhotoIsChecked = true;
                    try {
                        iv_Cad1_userPhoto.setImageBitmap(decodeFromFireBase64(userPhoto));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    userPhotoIsChecked = false;
                    iv_Cad1_userPhoto.setImageResource(R.drawable.user_photo_not_selected);
                }
            }
        });

        switchCad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("currentUser").child("createAd1");

                if (switchCad1.isChecked()){
                    String currentUserName;
                    String currentUserRating;
                    String currentUserPhoto;
                    String currentUserComment;
                    if(nameIsChecked) currentUserName = name;
                    else currentUserName = "false";

                    if(ratingIsChecked) currentUserRating = String.valueOf(rating).trim();
                    else currentUserRating = "false";

                    if(userPhotoIsChecked)currentUserPhoto = userPhoto;
                    else currentUserPhoto = "false";

                    if(commentIsChecked) currentUserComment = comment;
                    else currentUserComment = "false";
//                    TempStruct currentUser = new TempStruct();

                    TempStruct currentUser = new TempStruct(currentUserComment, currentUserName, currentUserPhoto, currentUserRating);

                    databaseReference.setValue(currentUser);
                }else{
                    TempStruct currentUser = new TempStruct("false", "false", "false", "false");
                    databaseReference.setValue(currentUser);
                }

            }
        });

        return view;
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
            ConstraintLayout constraintLayout = view.findViewById(R.id.back_cad1);
            Drawable dr = new BitmapDrawable(bitmap);
            dr.setBounds(0, 0, 480, 840);
            constraintLayout.setBackground(dr);
            Log.d("test", "background set");

        }
    }

    private Bitmap decodeFromFireBase64(String photoUrl) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(photoUrl, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

}
