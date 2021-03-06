package com.example.mmiazi.uab_v1.receivedAds_fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mmiazi.uab_v1.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Ad1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Ad1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ad1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private OnFragmentInteractionListener mListener;
    View view;
    private AdStruct ad;
    private String name;
    private float rating;
    private String userPhoto;
    private String productPhoto;
    private String comment;
    private String productName;
    private int fontColor;

    //    private StorageReference mStorageRef;
    public Ad1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment Ad1.
     */
    // TODO: Rename and change types and number of parameters
    public static Ad1 newInstance(AdStruct ad) {
        Ad1 fragment = new Ad1();
        Bundle args = new Bundle();
        if (ad != null) {
            args.putString("name", ad.getName());
            args.putFloat("rating", ad.getRating());
            args.putString("userPhoto", ad.getUserPhoto());
            args.putString("productPhoto", ad.getProductPhoto());
            args.putString("comment", ad.getComment());
            args.putString("productName", ad.getProductName());
        }

        fragment.setArguments(args);
        return fragment;
    }

    private void readBundle(Bundle args) {
        if (args != null) {
            this.name = args.getString("name");
            this.rating = args.getFloat("rating");
            this.comment = args.getString("comment");
            this.productName = args.getString("productName");
            this.userPhoto = args.getString("userPhoto");
            this.productPhoto = args.getString("productPhoto");
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
//        fontColor = Color.parseColor("#323B47");
        view = inflater.inflate(R.layout.fragment_ad1, container, false);

//        view.findViewById(R.id.back_ad1).setBackground();

        RatingBar ratingBar_ad1 = view.findViewById(R.id.ratingBar_ad1);
        TextView tv_ad1_Title = view.findViewById(R.id.tv_ad1_Title);
        ImageView iv_ad1_photo = view.findViewById(R.id.iv_ad1_photo);
        TextView tv_ad1_Name = view.findViewById(R.id.tv_ad1_Name);
        TextView tv_ad1_Comment = view.findViewById(R.id.tv_ad1_Comment);

        readBundle(getArguments());

        Log.d("test", rating + productName + name + comment);

        ratingBar_ad1.setRating(rating);
        ratingBar_ad1.setClickable(false);
        LayerDrawable stars = (LayerDrawable) ratingBar_ad1.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        tv_ad1_Title.setText(productName);
        tv_ad1_Name.setText(name);
        tv_ad1_Comment.setText(comment);

        Picasso.get().load(userPhoto).into(iv_ad1_photo);

        //background = null;

        new GetImageFromURL().execute(productPhoto);

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
            ConstraintLayout constraintLayout = view.findViewById(R.id.back_ad1);
            Drawable dr = new BitmapDrawable(bitmap);
            dr.setBounds(0, 0, 480, 840);
            constraintLayout.setBackground(dr);
            Log.d("test", "background set");

        }
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
}
