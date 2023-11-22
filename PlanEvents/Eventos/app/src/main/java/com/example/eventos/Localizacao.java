package com.example.eventos;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Localizacao {
    private static Geocoder geocoder;


    public static String getAddress(Context context, double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(context);
        String address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
        }
        return address;
    }

    public static LatLng getCoodenadas(Context context,String endereco) {
        geocoder = new Geocoder(context);
        List<Address> addressList;
        LatLng latLng = null;
        try {
            addressList = geocoder.getFromLocationName(endereco, 1);
            if (addressList != null) {
                latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLng;
    }


}
