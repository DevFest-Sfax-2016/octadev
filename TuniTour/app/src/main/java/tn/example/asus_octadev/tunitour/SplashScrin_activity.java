package tn.example.asus_octadev.tunitour;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hanks.htextview.HTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tn.example.asus_octadev.tunitour.Model.User;
import tn.example.asus_octadev.tunitour.Services.FirebaseInstanceIDService;
import tn.example.asus_octadev.tunitour.Utils.ErrorLabelLayout;
import tn.example.asus_octadev.tunitour.Utils.MyApplication;
import tn.example.asus_octadev.tunitour.Utils.UrlStatic;


public class SplashScrin_activity extends AppCompatActivity {
    String TAG="SplashScrin";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    User o;
    FirebaseUser user;
    String mail,password1,tel1,name1;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();
        FirebaseInstanceIDService.token = FirebaseInstanceId.getInstance().getToken();
        key= FirebaseInstanceIDService.token;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(SplashScrin_activity.this,MainActivity.class));

                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //  updateUI(user);
                // [END_EXCLUDE]
            }
        };
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        erreurclear();
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        final HTextView hTextView = (HTextView) findViewById(R.id.text);


        hTextView.animateText("TuniTour");
        final TextView hTextView2 = (TextView) findViewById(R.id.text2);

        hTextView2.setText("L'application TuniTour est la voie au tourisme et voyage agréable avec votre famille et vos amis");
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/lettre.ttf");
        hTextView2.setTypeface(tf);
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {



                    slideToTop2(findViewById(R.id.image));
                    slideToBu(findViewById(R.id.cardview2));

            }
        }, 2 * 1000);

        findViewById(R.id.loginbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.name).getVisibility() == View.GONE) {
                    ((TextView) findViewById(R.id.titel)).setText("create account");
                    slideToBu(findViewById(R.id.name));
                    slideToBu(findViewById(R.id.tel));
                    slideToBu(findViewById(R.id.passwordconfirm));

                    hTextView.setVisibility(View.GONE);
                    hTextView2.setVisibility(View.GONE);

                    TextView hTextView3 = (TextView) findViewById(R.id.text3);
                    hTextView3.setVisibility(View.VISIBLE);
                    if (hTextView3.getVisibility() == View.VISIBLE)
                        hTextView3.setText("TuniTour");
                    Typeface tf = Typeface.createFromAsset(getAssets(),
                            "fonts/agc.ttf");
                    hTextView3.setTypeface(tf);
                    visible();
                } else {
                    startActivity(new Intent(SplashScrin_activity.this, MainActivity.class));
                    finish();
                }
            }
        });
        findViewById(R.id.loginbt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ErrorLabelLayout errorLabelLayout = (ErrorLabelLayout) findViewById(R.id.emailerreur);
                ErrorLabelLayout errorpassword = (ErrorLabelLayout) findViewById(R.id.passworderreur);
                ErrorLabelLayout errorname = (ErrorLabelLayout) findViewById(R.id.nameerreur);
                ErrorLabelLayout telerreur = (ErrorLabelLayout) findViewById(R.id.telerreur);
                ErrorLabelLayout errorpasswordcon = (ErrorLabelLayout) findViewById(R.id.passwordconfirmerreur);

                EditText email = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);
                EditText passwordconfirm = (EditText) findViewById(R.id.passwordconfirm);
                EditText name = (EditText) findViewById(R.id.name);
                EditText tel = (EditText) findViewById(R.id.tel);
                Boolean verif = true;
                if (!passwordconfirm.getText().toString().equals(password.getText().toString())) {
                    errorpasswordcon.setError("password and password confirmation Not valid");
                    verif = false;

                }
                if (passwordconfirm.getText().toString().equals("")) {
                    errorpasswordcon.setError("password confirmation id required");
                    verif = false;

                }
                if (tel.getText().toString().equals("")) {
                    telerreur.setError("phone id required");
                    verif = false;

                }
                if (name.getText().toString().equals("")) {
                    errorname.setError("name id required");
                    verif = false;

                }
                if (email.getText().toString().equals("")) {
                    errorLabelLayout.setError("email is required");
                    verif = false;
                }

                if (password.getText().toString().equals("")) {
                    errorpassword.setError("password id required");
                    verif = false;

                }
                if (verif) {
                    mail = email.getText().toString();
                    password1 = password.getText().toString();
                    name1 = name.getText().toString();
                    tel1 = tel.getText().toString();
                    createAccount();
                    findViewById(R.id.cardview2).setVisibility(View.GONE);
                    findViewById(R.id.footer).setVisibility(View.VISIBLE);


                }
                   // signIn(email.getText().toString(),password.getText().toString());
            }
        });

        findViewById(R.id.signinbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorLabelLayout errorLabelLayout = (ErrorLabelLayout) findViewById(R.id.emailerreur);
                ErrorLabelLayout errorpassword = (ErrorLabelLayout) findViewById(R.id.passworderreur);

                EditText email = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);
                Boolean verif = true;
                if (email.getText().toString().equals("")) {
                    errorLabelLayout.setError("email is required");
                    verif = false;
                }

                if (password.getText().toString().equals("")) {
                    errorpassword.setError("password id required");
                    verif = false;

                }
                if (verif)
                    signIn(email.getText().toString(),password.getText().toString());
                findViewById(R.id.cardview2).setVisibility(View.GONE);
                findViewById(R.id.footer).setVisibility(View.VISIBLE);

                // Login();


            }
        });
        findViewById(R.id.signinbt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.name).getVisibility() == View.VISIBLE) {
                    ((TextView) findViewById(R.id.titel)).setText("signed in ");

                    slideToTop(findViewById(R.id.name));
                    slideToTop(findViewById(R.id.tel));
                    slideToTop(findViewById(R.id.passwordconfirm));

                    findViewById(R.id.text3).setVisibility(View.GONE);
                    hTextView.setVisibility(View.VISIBLE);
                    hTextView2.setVisibility(View.VISIBLE);

                    invisible();
                }


            }
        });
    }


    private void createAccount() {

        mAuth.createUserWithEmailAndPassword(mail,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            signIn(mail, password1);
                            User o = new User(mail, name1, tel1, "", password1,key);
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).setValue(o);

                        }

                    }
                });
    }
    public void slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight() + 400);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public void slideToBu(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight() + 40, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public void slideToTop2(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight() + 400);
        animate.setDuration(1500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public void visible() {
        findViewById(R.id.loginbt).setVisibility(View.GONE);
        findViewById(R.id.loginbt2).setVisibility(View.VISIBLE);
        findViewById(R.id.signinbt).setVisibility(View.GONE);
        findViewById(R.id.signinbt2).setVisibility(View.VISIBLE);


    }

    public void invisible() {
        findViewById(R.id.loginbt2).setVisibility(View.GONE);
        findViewById(R.id.loginbt).setVisibility(View.VISIBLE);
        findViewById(R.id.signinbt2).setVisibility(View.GONE);
        findViewById(R.id.signinbt).setVisibility(View.VISIBLE);


    }

    public void erreurclear() {
        EditText editFirsName = (EditText) findViewById(R.id.email);
        editFirsName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((ErrorLabelLayout) findViewById(R.id.emailerreur)).clearError();
                        break;
                }
                return false;
            }
        });
        EditText password = (EditText) findViewById(R.id.password);
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((ErrorLabelLayout) findViewById(R.id.passworderreur)).clearError();
                        break;
                }
                return false;
            }
        });
        EditText name = (EditText) findViewById(R.id.name);
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((ErrorLabelLayout) findViewById(R.id.nameerreur)).clearError();
                        break;
                }
                return false;
            }
        });
        EditText tel = (EditText) findViewById(R.id.tel);
        tel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((ErrorLabelLayout) findViewById(R.id.telerreur)).clearError();
                        break;
                }
                return false;
            }
        });
        EditText paa = (EditText) findViewById(R.id.passwordconfirm);
        paa.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ((ErrorLabelLayout) findViewById(R.id.passwordconfirmerreur)).clearError();
                        break;
                }
                return false;
            }
        });
    }


    private void signIn(final String email, final String password) {
        Log.v(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            startActivity(new Intent(SplashScrin_activity.this, MainActivity.class));
                        }
                    }
                });
        // [END sign_in_with_email]
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
