package tn.example.asus_octadev.tunitour;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;

import tn.example.asus_octadev.tunitour.Adaper.SlidingImage_Adapter;
import tn.example.asus_octadev.tunitour.Model.Event;
import tn.example.asus_octadev.tunitour.Model.User;
import tn.example.asus_octadev.tunitour.Utils.StaticV;


public class DetailEvent extends AppCompatActivity  {
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 99;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    TextView titre, title, type, datedebut, datefin, description, nblike, jours, mois, octadev_responsable,to;
    ImageView like, favorit, share, octadev_imageprofile;
    Button octadev_tel, octadev_email, octadev_sms;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    String id ;
    private GoogleMap mMap;
    Double lat, lon;
    Event event;
    RecyclerView recyclerView;
    ArrayList<Event> allSampleData;
    LinearLayout time;
    String name,mail,tel,photo;
    String TAG="DtailEvents";
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle extras = getIntent().getExtras();
        String id=extras.getString("id");
        get_ads(id);
        octadev_sms = (Button) findViewById(R.id.octadev_sms);
        octadev_email = (Button) findViewById(R.id.octadev_email);
        octadev_tel = (Button) findViewById(R.id.octadev_tel);
        octadev_responsable = (TextView) findViewById(R.id.octadev_responsable);
        title = (TextView) findViewById(R.id.title);
        titre = (TextView) findViewById(R.id.titre);
        jours = (TextView) findViewById(R.id.jours);
        mois = (TextView) findViewById(R.id.mois);
        nblike = (TextView) findViewById(R.id.nblike);
        type = (TextView) findViewById(R.id.type);
        to = (TextView) findViewById(R.id.to);
        datedebut = (TextView) findViewById(R.id.datedebut);
        datefin = (TextView) findViewById(R.id.datefin);
        description = (TextView) findViewById(R.id.description);
        like = (ImageView) findViewById(R.id.like);
        favorit = (ImageView) findViewById(R.id.favorite);
        share = (ImageView) findViewById(R.id.share);
        time= (LinearLayout) findViewById(R.id.time);
        octadev_imageprofile = (ImageView) findViewById(R.id.octadev_imageprofile);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/neoreg.ttf");
        description.setTypeface(tf);
        octadev_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" +mail));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        octadev_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://"+event.getWebsite()));
                startActivity(myIntent);
            }
        });
        octadev_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                if (tel != null)
                    callIntent.setData(Uri.parse("tel:" + tel));
                if (ActivityCompat.checkSelfPermission(DetailEvent.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    checkCallPermission();
                }
                try {
                    startActivity(callIntent);
                } catch (Exception e) {
                }
            }
        });
     /*   like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share();
            }
        });
        favorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorit();
            }
        });*/

    }


    private void Initializer() {
        title.setText(event.getName());
        titre.setText(event.getName());

        nblike.setText(event.getLike());
        description.setText(event.getDescription());
        datedebut.setText(event.getDate_debut());
        datefin.setText(event.getDate_fin());
        String jour = event.getDate_fin().substring(8, 10);
        int moi = Integer.parseInt(event.getDate_fin().substring(5,7));
        String monthString;
        switch (moi) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        type.setText(event.getType());


        jours.setText(jour + "");
        mois.setText(monthString);

        if(event.getType().equals(StaticV.ListeType[0])||event.getType().equals(StaticV.ListeType[1])||event.getType().equals(StaticV.ListeType[3])||event.getType().equals(StaticV.ListeType[7]))
        {
            time.setVisibility(View.GONE);
            jours.setText("");
            mois.setText("");
        }
        init();

    }

    @Nullable
    @Override
    public View getCurrentFocus() {
        return super.getCurrentFocus();

    }

    private void Share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, event.getName() + "");
        String[] strings = event.getImages().split("#");
        share.putExtra(Intent.EXTRA_TEXT, strings[0]);

        startActivity(Intent.createChooser(share, "Share link!"));
    }



    private void init() {
        String[] im = event.getImages().split("#");

        for (int i = 0; i < im.length; i++)
            ImagesArray.add(im[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(DetailEvent.this, ImagesArray));


        final float density = getResources().getDisplayMetrics().density;


        NUM_PAGES = im.length;


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator


    }

    public boolean checkCallPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            //chiheb
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private int veriftype(String type) {
        if(type.equals(StaticV.ListeType[0]))
            return R.drawable.marker11;

        if(type.equals(StaticV.ListeType[1]))
            return R.drawable.marker2;

        if(type.equals(StaticV.ListeType[2]))
            return R.drawable.marker3;

        if(type.equals(StaticV.ListeType[3]))
            return R.drawable.marker4;

        if(type.equals(StaticV.ListeType[4]))
            return R.drawable.marker5;
        if(type.equals(StaticV.ListeType[5]))
            return R.drawable.marker6;
        if(type.equals(StaticV.ListeType[6]))
            return R.drawable.marker6;

        if(type.equals(StaticV.ListeType[7]))
            return R.drawable.marker7;
        if(type.equals("me"))
            return R.drawable.memarker;
        return R.drawable.marker1;
    }
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
    public void infoPoster() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(event.getUseruid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User post = dataSnapshot.getValue(User.class);
                        if (post != null) {
                            octadev_responsable.setText(post.getFull_name());
                            if(! post.getProfile_image().equals(""))
                            Picasso.with(DetailEvent.this).load(post.getProfile_image()).into(octadev_imageprofile);
                            name=post.getFull_name();
                            octadev_responsable.setText(post.getFull_name());
                            if (!post.getEmail().equals(""))
                                mail = post.getEmail();
                            if (!post.getMobile().equals(""))
                                tel = post.getMobile();
                            if (!post.getFull_name().equals(""))
                                octadev_responsable.setText(post.getFull_name());
                            if (post.getProfile_image() == null)
                                post.setProfile_image("");
                            if (!post.getProfile_image().equals(""))
                            {
                                Picasso.with(DetailEvent.this).load(post.getProfile_image()).into(octadev_imageprofile);
                                photo=post.getProfile_image();
                            }


                        } else
                            Toast.makeText(DetailEvent.this, "null", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
    void get_ads(final String id) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Events").child(id + "").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        event = dataSnapshot.getValue(Event.class);
                        infoPoster();
                        Initializer();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }


}
