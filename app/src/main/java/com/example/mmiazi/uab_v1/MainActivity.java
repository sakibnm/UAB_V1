package com.example.mmiazi.uab_v1;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SignUpFragment.OnFragmentInteractionListener{

    private DrawerLayout mDrawerLayout;
    private ImageButton userPhoto;
    private boolean loggedIn;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private User user;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        //TODO: write code here for main functionalities of the user.....
        currentUserUI(firebaseUser);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef= FirebaseStorage.getInstance().getReference();

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_View);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.nav_signUp:
                                //Toast.makeText(getApplicationContext(), "Sign Up pressed!", Toast.LENGTH_SHORT).show();
                                signUpGUI();
                                break;
                            case R.id.nav_aboutUs:
                                //Toast.makeText(getApplicationContext(), "About Us pressed!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                }
        );



    }

    public void signUpGUI() {
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, signUpFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        mDrawerLayout.closeDrawers();
    }

    public void currentUserUI(FirebaseUser firebaseUser){
//        TODO: UI after sign in......
        if(firebaseUser==null) {
//            TODO: Firebase user not found.... Sign in or Sign Up....
        }
    }

    public void createUser(){
        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("test", "Create user successful");
                            final FirebaseUser userFirebase = mAuth.getCurrentUser();
                            String uID = userFirebase.getUid();

//                          TODO:  Save the user Bitmap to internal storage...
                            ContextWrapper cw = new ContextWrapper(getApplicationContext());
                            File directory = cw.getDir("tempImage", Context.MODE_PRIVATE);
                            File imagePath = new File(directory, "userPhoto.png");

                            FileOutputStream fos = null;
                            Bitmap bitmap = user.getUserPhoto();

                            //ImageView testView = findViewById(R.id.testView);

                            //testView.setImageBitmap(bitmap);

                            try {
                                fos = new FileOutputStream(imagePath);
                                if(bitmap !=null)bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }finally{
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


//                          TODO: Upload the image to Firebase storage...

                            Uri imageFile = Uri.fromFile(imagePath);
                            StorageReference imageRef = mStorageRef.child("userImages/user_"+uID+".png");
                            imageRef.putFile(imageFile)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                            user.setImageDownloadUri(downloadUrl);
                                            updateProfileImageUrl(downloadUrl, userFirebase);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Image Upload failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }else{
//                            Toast.makeText(getApplicationContext(), "Create account failed: please check all the fields!", Toast.LENGTH_SHORT).show();
                            Log.d("test", "Create user failure");
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            currentUserUI(null);
                        }
                    }
                });
    }

    private void updateProfileImageUrl(Uri downloadUrl, FirebaseUser userFirebase) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(downloadUrl)
                .build();
        userFirebase.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("test", "Photo Uploaded!");
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(User user) {
        this.user = user;
//                new User(user.getFirstName(),user.getLastName(),user.getEmail(),user.getPassword(), user.getRepeatPassword(),user.getPhone(),user.getAddress(), user.getUserPhoto());
        createUser();
    }

}
