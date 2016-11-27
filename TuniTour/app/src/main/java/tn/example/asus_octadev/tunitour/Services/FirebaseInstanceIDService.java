package tn.example.asus_octadev.tunitour.Services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by ASUS-OCTADEV on 2016-11-27.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static String token;
    @Override
    public void onTokenRefresh() {

         token = FirebaseInstanceId.getInstance().getToken();
         token = FirebaseInstanceId.getInstance().getToken();

    }


}
