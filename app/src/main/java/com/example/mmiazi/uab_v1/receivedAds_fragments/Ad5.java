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
 * {@link Ad5.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Ad5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ad5 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View view;
    private AdStruct ad;
    private String name;
    private float rating;
    private String userPhoto;
    private String productPhoto;
    private String comment;
    private String productName;

    private OnFragmentInteractionListener mListener;

    public Ad5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Ad5.
     */
    // TODO: Rename and change types and number of parameters
    public static Ad5 newInstance(AdStruct ad) {
        Ad5 fragment = new Ad5();
        Bundle args = new Bundle();
        if (ad != null) {
            args.putString("name", ad.getName());
            args.putFloat("rating", ad.getRating());
            args.putString("userPhoto", ad.getUserPhoto());
            args.putString("productPhoto", ad.getProductPhoto());
            args.putString("comment", ad.getComment());
            args.putString("productName", ad.getProductName());
        } else {
            Log.d("test", "null aise... :(");
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
        view = inflater.inflate(R.layout.fragment_ad5, container, false);
        RatingBar ratingBar_ad5 = view.findViewById(R.id.ratingBar_ad5);
        TextView tv_ad5_Title = view.findViewById(R.id.tv_ad5_Title);
        ImageView iv_ad5_photo = view.findViewById(R.id.iv_ad5_photo);
        TextView tv_ad5_Name = view.findViewById(R.id.tv_ad5_Name);
        TextView tv_ad5_Comment = view.findViewById(R.id.tv_ad5_Comment);
        readBundle(getArguments());
        ratingBar_ad5.setRating(rating);
        ratingBar_ad5.setClickable(false);
        LayerDrawable stars = (LayerDrawable) ratingBar_ad5.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        tv_ad5_Title.setText(productName);
        tv_ad5_Name.setText(name);
        tv_ad5_Comment.setText(comment);

        Log.d("test", rating + productName + name + comment);

        Picasso.get().load(userPhoto).into(iv_ad5_photo);

        //background = null;

        new Ad5.GetImageFromURL().execute(productPhoto);
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
            ConstraintLayout constraintLayout = view.findViewById(R.id.back_ad4);
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
