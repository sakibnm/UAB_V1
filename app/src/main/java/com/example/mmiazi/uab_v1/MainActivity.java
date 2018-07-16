package com.example.mmiazi.uab_v1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements SignUpFragment.OnFragmentInteractionListener, InstructionsFragment.OnFragmentInteractionListener{

    private DrawerLayout mDrawerLayout;
    private Bitmap userPhoto;
    private Uri uriUserPhoto;
    private boolean loggedIn;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private User user;
    private String CHANNEL_ID = "Channel";
    private String notificationCommand = "empty";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static MenuItem navInstructions;
    public static MenuItem navCreateAd;
    public static MenuItem navLogout;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser != null){
            loggedIn =true;
            Toast.makeText(this, "Hi "+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
        }else{
            loggedIn = false;
        }

        this.getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("signalFromAdmin").child("command");

        userPhoto = null;

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

//        ((TextView)findViewById(R.id.nav_logout)).setVisibility(View.INVISIBLE);
//        ((TextView)findViewById(R.id.test_tv)).setVisibility(View.GONE);
//        ((TextView)findViewById(R.id.tv_SendReview)).setVisibility(View.GONE);


        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



        mAuth = FirebaseAuth.getInstance();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_View);

        Menu menu = navigationView.getMenu();
        navInstructions = menu.findItem(R.id.nav_aboutUs);
        navCreateAd = menu.findItem(R.id.nav_SendReview);
        navLogout = menu.findItem(R.id.nav_logout);

        navInstructions.setVisible(true);
        navCreateAd.setVisible(false);
        navLogout.setVisible(false);

        if(loggedIn){
            navInstructions.setVisible(true);
            navCreateAd.setVisible(true);
            navLogout.setVisible(true);
            instructionsGUI();
        }else{
            signUpGUI();
        }
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.nav_aboutUs:
                                //Toast.makeText(getApplicationContext(), "About Us pressed!", Toast.LENGTH_SHORT).show();
                                instructionsGUI();
                                break;
                            case R.id.nav_SendReview:
                                sendReview();
                            case R.id.nav_logout:
                                navCreateAd.setVisible(false);
                                navLogout.setVisible(false);
                                logOut();
                        }
                        return true;
                    }
                }
        );

//        TODO: Notification Listenner..........

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notificationCommand = dataSnapshot.getValue().toString();
                if(notificationCommand.equals("notifyAd1") || notificationCommand.equals("notifyAd2") || notificationCommand.equals("notifyAd3")) {
                    Log.d("test", notificationCommand+"");
                    createNotification();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void logOut() {
        mAuth.signOut();

//        TODO: Show the menu buttons again...


//        TODO: Cleanup firebase Current User...
    }

    private void sendReview() {
        Intent intent = new Intent(this, SendReviewsActivity.class);
        startActivity(intent);
        mDrawerLayout.closeDrawers();
    }

    private void createNotification(){
        //        TODO: Notification receive.....
        createNotificationChannel();

//        Intent backIntent = new (this, MainActivity.class);
//        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = new Intent(MainActivity.this, NotifAd.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Ad Received!")
                .setContentText("Someone nearby published a product review!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Someone nearby published a product review!"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
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

    public void instructionsGUI(){
        InstructionsFragment instructionsFragment = new InstructionsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, instructionsFragment);
        transaction.commit();
        mDrawerLayout.closeDrawers();
    }

    public void currentUserUI(FirebaseUser firebaseUser){
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

        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userPhotoRef = mStorageRef.child("userPhotos").child(uID+".jpg");

        Log.d("test", "Uri: "+userPhotoRef);

        if(uriUserPhoto!=null) {

            mStorageRef.putFile(uriUserPhoto)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadURL = taskSnapshot.getDownloadUrl();
                            Log.d("test", "Image URL: " + downloadURL.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("test", e.getMessage());
                }
            });
        }

        String photoUrl = "";
        if (uID != null && user != null) {
            photoUrl = encodeBitmap(userPhoto, uID);
        }
        user.setImageDownloadUrl(photoUrl);
        ImageView welcomeImage = findViewById(R.id.imageView_Welcome);
        TextView tv_userName = findViewById(R.id.tv_user);

        Bitmap decodedBitmap = null;
        try {
            decodedBitmap = decodeFromFireBase64(photoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        welcomeImage.setImageBitmap(decodedBitmap);
        tv_userName.setText(user.getFirstName() + "!");

        dataRef.child("users").child(uID).setValue(user);
        dataRef.child("currentUser").child("email").setValue(user.getEmail());
        dataRef.child("currentUser").child("firstName").setValue(user.getFirstName());
        dataRef.child("currentUser").child("lastName").setValue(user.getLastName());
        dataRef.child("currentUser").child("gender").setValue(user.getGender());
        dataRef.child("currentUser").child("uID").setValue(uID);
//        dataRef.child("currentUser").child("image").setValue(photoUrl);


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
//                new User(user.getFirstName(),user.getLastName(),user.getEmail(),user.getPassword(), user.getRepeatPassword(),user.getPhone(),user.getAddress(), user.getUserPhoto());
        createUser(user);
        navLogout.setVisible(true);
        navCreateAd.setVisible(true);
        navInstructions.setVisible(true);
        instructionsGUI();
    }

    @Override
    public void onPhotoCaptured(Bitmap bitmap) {
        userPhoto = bitmap;

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
