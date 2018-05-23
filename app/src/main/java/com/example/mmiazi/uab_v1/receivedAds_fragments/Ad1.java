package com.example.mmiazi.uab_v1.receivedAds_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mmiazi.uab_v1.R;

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

    public Ad1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ad1.
     */
    // TODO: Rename and change types and number of parameters
    public static Ad1 newInstance(String param1, String param2) {
        Ad1 fragment = new Ad1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_ad1, container, false);

        RatingBar ratingBar_ad1 = view.findViewById(R.id.ratingBar_ad1);
        TextView tv_ad1_Title = view.findViewById(R.id.tv_ad1_Title);
        ImageView iv_ad1_photo = view.findViewById(R.id.iv_ad1_photo);
        TextView tv_ad1_Name = view.findViewById(R.id.tv_ad1_Name);
        TextView tv_ad1_Comment = view.findViewById(R.id.tv_ad1_Comment);

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
}
