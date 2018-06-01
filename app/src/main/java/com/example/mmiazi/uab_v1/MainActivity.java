package com.example.mmiazi.uab_v1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmiazi.uab_v1.receivedAds_fragments.Ad1;
import com.example.mmiazi.uab_v1.receivedAds_fragments.Ad2;
import com.example.mmiazi.uab_v1.receivedAds_fragments.AdStruct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SignUpFragment.OnFragmentInteractionListener, Ad1.OnFragmentInteractionListener, Ad2.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    private Bitmap userPhoto;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String CHANNEL_ID;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(this, "Please Log out", Toast.LENGTH_LONG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPhoto = null;

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mAuth = FirebaseAuth.getInstance();

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
                            case R.id.test_tv:
                                showAds();
                                break;
                        }
                        return true;
                    }
                }
        );

//        TODO: Notifications.................
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        String adminPhoto = "https://firebasestorage.googleapis.com/v0/b/uabv1-d5718.appspot.com/o/receivedAds%2FadminPhoto%2FuserPhoto.png?alt=media&token=44ab6a20-334f-41c6-aa21-318572603a22";
        DatabaseReference adminCommand = databaseReference.child("signalFromAdmin").child("command");

        DatabaseReference notifAdRef = databaseReference.child("adstoSend");

        final AdStruct [] notifAds = new AdStruct[3];
        notifAdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    AdStruct ad = data.getValue(AdStruct.class);
                    notifAds[i++] = ad;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adminCommand.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String command = dataSnapshot.getValue().toString().trim();
                Log.d("test", command+"");
                CHANNEL_ID = "none";
                String Notif_Title = "Someone posted a review nearby!";

                switch(command){
                    case "notifyAd1":
                        CHANNEL_ID = "ad1";
                        createNotificationChannel();
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.notif_small)
                                .setContentTitle(Notif_Title)
                                .setContentText(notifAds[0].getComment())
                                .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(notifAds[0].getComment()))
                                .setPriority(NotificationCompat.PRIORITY_HIGH);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(1, mBuilder.build());
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

    private void createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channels";
            String description = "channels descriptions";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showAds() {

        Intent intent = new Intent(this, ShowAdsActivity.class);
        startActivity(intent);
        mDrawerLayout.closeDrawers();
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
//
    }

    public void createUser(final User user) {
        Log.d("test", user.getEmail() + " " + user.getPassword());
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("test", "create user with email password successful");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "SignUp Failed: put valid email, phone, and address", Toast.LENGTH_SHORT);
                        Log.d("test", "create user with email password failed");
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("test", "create user with email password successful!");
                        FirebaseUser usr = mAuth.getCurrentUser();
                        String uID = usr.getUid();

                        setUpAccount(uID, user);

                        //mRef.child(uID).setValue(user);

                    }
        });
    }

    private void setUpAccount(String uID, User user) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = firebaseDatabase.getReference();

        String photoUrl = "";
        if (uID != null && user != null) {
            photoUrl = encodeBitmap(userPhoto, uID);
        }
        user.setImageDownloadUrl(photoUrl);
        ImageView welcomeImage = findViewById(R.id.imageView_Welcome);
        TextView tv_userName = findViewById(R.id.tv_user);

        dataRef.child("users").child(uID).setValue(user);
        dataRef.child("currentUser").child("email").setValue(user.getEmail());
        dataRef.child("currentUser").child("firstName").setValue(user.getFirstName());
        dataRef.child("currentUser").child("lastName").setValue(user.getLastName());
        dataRef.child("currentUser").child("gender").setValue(user.getGender());
        dataRef.child("currentUser").child("uID").setValue(uID);
        Bitmap decodedBitmap = null;
        try {
            decodedBitmap = decodeFromFireBase64(photoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        welcomeImage.setImageBitmap(decodedBitmap);
        tv_userName.setText(user.getFirstName() + "!");
    }

    private Bitmap decodeFromFireBase64(String photoUrl) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(photoUrl, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    private String encodeBitmap(Bitmap userPhoto, String uID) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userPhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = "";
        imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        return imageEncoded;
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
        createUser(user);
    }

    @Override
    public void onPhotoCaptured(Bitmap bitmap) {
        userPhoto = bitmap;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
