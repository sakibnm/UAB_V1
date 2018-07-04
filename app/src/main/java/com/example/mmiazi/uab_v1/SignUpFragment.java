package com.example.mmiazi.uab_v1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;

    private static final int CAM_REQ = 1111;

    private ImageView userPhoto;
    private User user;
    private Button createButton;
    private Button cancelButton;
    private Bitmap bitmap;

    public SignUpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap =null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        userPhoto = rootView.findViewById(R.id.pick_camera);
        userPhoto.setOnClickListener(new takePhoto());
        createButton = rootView.findViewById(R.id.button_register);

//        createButtonOnClick(rootView , createButton);



        final EditText textFirst = (EditText) rootView.findViewById(R.id.text_first);

        final EditText textEmail = (EditText) rootView.findViewById(R.id.test_email);
        final Spinner spinner = rootView.findViewById(R.id.spinner_gender);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                rootView.getContext(), R.array.gender, android.R.layout.simple_spinner_dropdown_item
        );

        final int[] genderInt = {0};

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    genderInt[0] = 1;
                } else if (position == 1) {
                    genderInt[0] = 2;
                } else {
                    genderInt[0] = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(rootView.getContext(),"Please select a gender.", Toast.LENGTH_SHORT);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = textFirst.getText().toString().trim();
                String lastName = "";
                String email = textEmail.getText().toString().trim();
                String password = "";
                String repPassword = "";
                String phone = "";
                String address = "";
                String genderSelected = "";
                Log.d("test", firstName+lastName+email+password+repPassword+phone+address);

                boolean inputValid = true;

                if(firstName.equals("")||lastName.equals("")||email.equals("")||phone.equals("")||address.equals("")) {
                    inputValid = false;
                    Toast.makeText(getContext(),"Please fill up all the fields", Toast.LENGTH_SHORT).show();

                }

                if (genderInt[0] == 0) {
                    inputValid = false;
                } else if (genderInt[0] == 1) {
                    genderSelected = "Female";
                } else if (genderInt[0] == 2) {
                    genderSelected = "Male";
                } else {
                    genderSelected = "Other";
                }

                if(bitmap == null){
                    inputValid = false;
                    Toast.makeText(getContext(),"Please take your profile photo", Toast.LENGTH_SHORT).show();
                }

                if(!password.equals(repPassword)){
                    inputValid = false;
                    Toast.makeText(getContext(),"Please check the password", Toast.LENGTH_SHORT).show();
                }

                if(inputValid){
                    Bitmap userPhoto = bitmap;
                    //((ImageView)getView().findViewById(R.id.testView)).setImageBitmap(userPhoto);
                    user = new User(firstName, lastName, email, password, repPassword, genderSelected, phone, address);
                    userSignUp(user);
//                    ((MenuItem)getView().findViewById(R.id.nav_signUp)).setTitle(R.string.logOut);
//                    ((MenuItem)getView().findViewById(R.id.nav_signUp)).setIcon(R.mipmap.logout_foreground);
                    getActivity().onBackPressed();
                }


            }
        });

        cancelButton = rootView.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","Cancel pressed");
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    private void userSignUp(User user) {
        mListener.onFragmentInteraction(user);
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

    class takePhoto implements ImageButton.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQ);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQ) {
            bitmap = (Bitmap) data.getExtras().get("data");
            userPhoto.setImageBitmap(bitmap);
            mListener.onPhotoCaptured(bitmap);
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
        void onFragmentInteraction(User user);

        void onPhotoCaptured(Bitmap bitmap);

    }


}
