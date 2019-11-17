package com.example.dipto.newsgoround;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.dipto.newsgoround.FragmentsClasses.LiveScoreTab;
import com.example.dipto.newsgoround.FragmentsClasses.LocalNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.ScienceNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.SportsNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.TechNewsTab;

import com.example.dipto.newsgoround.FragmentsClasses.TopHeadLinesNewsTab;
import com.example.dipto.newsgoround.FragmentsClasses.ViewPageAdapterTabView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener   {

    private FirebaseAuth mAuth;

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingSearchView mSearchView;


    private TextView username_nav , user_email_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);
        ViewPageAdapterTabView adapter = new ViewPageAdapterTabView(getSupportFragmentManager());
        adapter.AddFragment(new TopHeadLinesNewsTab(),  "Top Headlines");
        adapter.AddFragment(new LocalNewsTab(),  "Local");
        adapter.AddFragment(new ScienceNewsTab(),  "Science");
        adapter.AddFragment(new TechNewsTab() , "Tech");
        adapter.AddFragment(new SportsNewsTab() , "Sports");
        adapter.AddFragment(new LiveScoreTab() , "Live Scores");



        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        mSearchView = findViewById(R.id.floating_search);


        //navigation_drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        mSearchView.attachNavigationDrawerToMenuButton(drawerLayout);

        //navigation_drawer

        mAuth = FirebaseAuth.getInstance();



        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        View navigation_header_view =  navigationView.getHeaderView(0);


        username_nav = navigation_header_view.findViewById(R.id.username_navigation_drawer);
        user_email_nav = navigation_header_view.findViewById(R.id.user_email_navigation_drawer);









    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            openActivityLogin();
        }else{
            //setting username and email on navigation_drawer


            String u_id = mAuth.getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("users").child(u_id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("username").getValue().toString();
                    username_nav.setText(name);
                    String email = dataSnapshot.child("email").getValue().toString();
                    user_email_nav.setText(email);
                    //Log.d("T",name);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    //____________if not logged in__________

    public void openActivityLogin(){
        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        finish();

    }


    //_________Navigation Drawer Click Listener________


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_logOut:

                FirebaseAuth.getInstance().signOut();
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(new Intent(this , SignInActivity.class));

                break;
            case R.id.nav_sportsPreferenceCustomize:
                Intent intent = new Intent(MainActivity.this , SportsSettingsActivity.class);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(intent);
                break;
            case R.id.nav_contact:
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                startActivity(new Intent(this , ContactUsActivity.class));
                break;




        }



        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }





}
