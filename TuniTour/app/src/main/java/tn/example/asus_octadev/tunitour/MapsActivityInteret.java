package tn.example.asus_octadev.tunitour;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tn.example.asus_octadev.tunitour.Utils.MyApplication;

public class MapsActivityInteret extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_interet);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                doctor();

            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
loadhospitale();

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(34.914848, 10.603875);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(34.914848, 10.603875))
                .radius(10000)
                .strokeColor(getResources().getColor(R.color.colorPrimary2))
                .strokeWidth(1.5f)
                .fillColor(getResources().getColor(R.color.colorPrimary2)));


    }



    public void loadhospitale() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            try {


                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng sydney = new LatLng(location.getLatitude() , location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude() , location.getLongitude()))
                        .radius(10000)
                        .strokeColor(getResources().getColor(R.color.colorPrimary2))
                        .strokeWidth(1.5f)
                        .fillColor(getResources().getColor(R.color.colorPrimary2)));

                StringRequest strReq = new StringRequest(Request.Method.GET,
             /*   " http://maps.googleapis.com/maps/api/geocode/json?latlng="+ StaticApp.lat+","+StaticApp.lon+"&sensor=false"*/
                        "https://maps.googleapis.com/maps/api/place/search/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&rankby=distance&types=hospital&sensor=false&key=AIzaSyAD2im6QzNTe_iN79IhVkYx247M6K3fARc"

                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject array = new JSONObject(response);
                            JSONArray object = array.getJSONArray("results");
                            if (object.length() == 0) {
                                Toast.makeText(MapsActivityInteret.this, "place not found", Toast.LENGTH_SHORT).show();
                            } else {

                             for (int i=0;i<object.length();i++) {
                                 JSONObject box = object.getJSONObject(i);
                                 String url_icon = box.getString("icon");
                                 //    Picasso.with(MapsActivityInteret.this).load(url_icon).into(  (ImageView)  findViewById(R.id.supermarket));

                                 JSONObject box2 = box.getJSONObject("geometry");
                                 JSONObject box3 = box2.getJSONObject("location");


                                 Double lat = box3.getDouble("lat");
                                 Double lon = box3.getDouble("lng");
                                 String name_sper = box.getString("name");

                                 Marker marker2 = mMap.addMarker(new MarkerOptions()
                                         .position(new LatLng(lat, lon))
                                         .title(name_sper)
                                         .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.google_maps_mark2))));


                                 //     Toast.makeText(BuyActivity.this,name+ "\n "+lat+"\n"+lon, Toast.LENGTH_SHORT).show();
                             }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MapsActivityInteret.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //     Toast.makeText(mContext, error.toString()+"2", Toast.LENGTH_SHORT).show();
                        NetworkResponse networkResponse = error.networkResponse;
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }
                };
                strReq.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);

            } catch (Exception e) {

            }
        }

    }
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
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
    public void doctor() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            try {


                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng sydney = new LatLng(location.getLatitude() , location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude() , location.getLongitude()))
                        .radius(10000)
                        .strokeColor(getResources().getColor(R.color.colorPrimary2))
                        .strokeWidth(1.5f)
                        .fillColor(getResources().getColor(R.color.colorPrimary2)));

                StringRequest strReq = new StringRequest(Request.Method.GET,
             /*   " http://maps.googleapis.com/maps/api/geocode/json?latlng="+ StaticApp.lat+","+StaticApp.lon+"&sensor=false"*/
                        "https://maps.googleapis.com/maps/api/place/search/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&rankby=distance&types=doctor&sensor=false&key=AIzaSyAD2im6QzNTe_iN79IhVkYx247M6K3fARc"

                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject array = new JSONObject(response);
                            JSONArray object = array.getJSONArray("results");
                            if (object.length() == 0) {
                                Toast.makeText(MapsActivityInteret.this, "place not found", Toast.LENGTH_SHORT).show();
                            } else {

                                for (int i=0;i<object.length();i++) {
                                    JSONObject box = object.getJSONObject(i);
                                    String url_icon = box.getString("icon");
                                    //    Picasso.with(MapsActivityInteret.this).load(url_icon).into(  (ImageView)  findViewById(R.id.supermarket));

                                    JSONObject box2 = box.getJSONObject("geometry");
                                    JSONObject box3 = box2.getJSONObject("location");


                                    Double lat = box3.getDouble("lat");
                                    Double lon = box3.getDouble("lng");
                                    String name_sper = box.getString("name");

                                    Marker marker2 = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lon))
                                            .title(name_sper)
                                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.google_maps_mark2))));


                                    //     Toast.makeText(BuyActivity.this,name+ "\n "+lat+"\n"+lon, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MapsActivityInteret.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //     Toast.makeText(mContext, error.toString()+"2", Toast.LENGTH_SHORT).show();
                        NetworkResponse networkResponse = error.networkResponse;
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }
                };
                strReq.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);

            } catch (Exception e) {

            }
        }

    }

}
