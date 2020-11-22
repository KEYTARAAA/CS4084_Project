package ie.ul.cs4084project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.okhttp.internal.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.LOCATION_SERVICE;

public class RunFragment extends Fragment {


    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 100;
    private final float MIN_DISTANCE = (float) 0.1;
    private LatLng latLng;
    private Marker startLocation, currentLocation;
    private boolean begin, start, set;
    private Polyline currentPolyline;
    private ArrayList<LatLng> all;
    private TextView currentDistance;
    private double currentD;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private FusedLocationProviderClient client;
    private int kmCounter;
    int count = 0;
    private String name, id, email;


    public void setUp(String name, String id, String email){
        this.name=name;
        this.id=id;
        this.email=email;
    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            //Task<Location> task = client.getLastLocation();
            try {

                Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                        startLocation = mMap.addMarker(new MarkerOptions().position(me).title("Start"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 17));
                    }
                });
                mMap.setMyLocationEnabled(true);
            }catch (SecurityException e){
                e.printStackTrace();
            }
            // Add a marker in Sydney and move the camera
            currentD=0;
            currentDistance =  getActivity().findViewById(R.id.textViewCurrentDistance);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    try{
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if(begin) {
                            mMap.clear();
                            MarkerOptions startMO = new MarkerOptions().position(latLng).title("Start");
                            startLocation = mMap.addMarker(startMO);
                            MarkerOptions currentMO = new MarkerOptions().position(latLng).title("You are Here");
                            currentLocation = mMap.addMarker(currentMO);
                            currentLocation.setVisible(false);
                            addMileStone();
                            if(start) {
                                begin = false;
                                all.add(startLocation.getPosition());
                                addMileStone();
                                kmCounter++;
                                PolylineOptions polylineOptions = new PolylineOptions().addAll(all).color(Color.BLUE).width(5);
                                currentPolyline = mMap.addPolyline(polylineOptions);
                            }
                        }else {
                            currentPolyline.remove();
                            LatLng latLng1 = currentLocation.getPosition();
                            if(!set) {
                                currentLocation.remove();
                            }else{
                                set = false;
                            }
                            if (currentD>=kmCounter) {
                                MarkerOptions currentMO = new MarkerOptions().position(latLng).title(Integer.toString(kmCounter)+" km");//Integer.toString(kmCheckPoint)+" km");
                                currentLocation = mMap.addMarker(currentMO);
                                currentLocation.setVisible(true);
                                addMileStone();
                                kmCounter++;
                                //kmCheckPoint++;
                                set = true;
                            }else{
                            MarkerOptions currentMO = new MarkerOptions().position(latLng);
                            currentLocation = mMap.addMarker(currentMO);
                            currentLocation.setVisible(false);
                        }
                            LatLng latLng2 = currentLocation.getPosition();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            all.add(currentLocation.getPosition());
                            PolylineOptions polylineOptions = new PolylineOptions().addAll(all).color(Color.BLUE).width(30);
                            currentPolyline = mMap.addPolyline(polylineOptions);

                            currentD += distance(latLng1.latitude, latLng1.longitude, latLng2.latitude, latLng2.longitude);
                            currentDistance.setText("Current: " + df2.format(currentD)+"km");
                        }
                    }catch (SecurityException s){
                        s.printStackTrace();
                    }
                }
            };

            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_run, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        all = new ArrayList<LatLng>();
        begin = true;
        start = false;
        set = true;
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        kmCounter = 0;
         currentDistance =  getActivity().findViewById(R.id.textViewCurrentDistance);
    }


    public double getMilestones(){

        return  getFullDistance(all);
    }


    private double getFullDistance(ArrayList<LatLng> all) {
        double full = 0;
        for (int i=1; i<all.size(); i++) {
            LatLng point1 = all.get(i-1);
            LatLng point2 = all.get(i);
                full+=distance(point1.latitude, point1.longitude, point2.latitude, point2.longitude);

        }

        return full;
    }

    public void start(){
        start = true;
        all.add(startLocation.getPosition());
        addMileStone();
        kmCounter++;
        PolylineOptions polylineOptions = new PolylineOptions().addAll(all).color(Color.BLUE).width(5);
        currentPolyline = mMap.addPolyline(polylineOptions);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; //km
        return d;//* 1000 for m;
    }/**/

    private void addMileStone(){
        ((Run)getActivity()).addMileStone(kmCounter);
    }

    public void finish() {

        /*if (count == 0) {

            LatLng testLL = new LatLng(52.663297, -8.578516);
            MarkerOptions mo = new MarkerOptions().position(testLL).title("test");
            Marker m = mMap.addMarker(mo);
            all.add(testLL);
            PolylineOptions po = new PolylineOptions().addAll(all);
            mMap.addPolyline(po);
            count++;
        } else if (count == 1) {

            LatLng testLL = new LatLng(52.665141, -8.579926);
            MarkerOptions mo = new MarkerOptions().position(testLL).title("test");
            Marker m = mMap.addMarker(mo);
            all.add(testLL);
            PolylineOptions po = new PolylineOptions().addAll(all);
            mMap.addPolyline(po);
            count++;
        }else if (count == 2) {

            LatLng testLL = new LatLng(52.666457, -8.570910);
            MarkerOptions mo = new MarkerOptions().position(testLL).title("test");
            Marker m = mMap.addMarker(mo);
            all.add(testLL);
            PolylineOptions po = new PolylineOptions().addAll(all);
            mMap.addPolyline(po);
            count++;
        }else if (count == 3) {

            LatLng testLL = new LatLng(52.662033, -8.568834);
            MarkerOptions mo = new MarkerOptions().position(testLL).title("test");
            Marker m = mMap.addMarker(mo);
            all.add(testLL);
            PolylineOptions po = new PolylineOptions().addAll(all);
            mMap.addPolyline(po);
            count++;
        }
        if(count>5){*/
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : all) {
            builder.include(latLng);
        //}

        LatLngBounds bounds = builder.build();
        int padding = 50;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
            GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                Bitmap bitmap;

                @Override
                public void onSnapshotReady(Bitmap snapshot) {
                    bitmap = snapshot;
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                        ImageView imageView = getActivity().findViewById(R.id.imageView9);
                        imageView.setImageBitmap(bitmap);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        Intent intent = new Intent(getActivity(), PostRunActivity.class);
                        intent.putExtra(MainActivity.ID, id);
                        intent.putExtra(MainActivity.EMAIL, email);
                        intent.putExtra(MainActivity.NAME, name);
                        Run run = (Run) getActivity();
                        ArrayList<RunMilestone> runMilestones = run.getMilestones();
                        String[] strings = new String[runMilestones.size()];
                        for(int i=0; i<runMilestones.size(); i++){
                            strings[i] = runMilestones.get(i).toString();
                        }
                        intent.putExtra(Run.MILESTONES, strings);

                        //startActivity(intent);
                        intent.putExtra(Run.RUN_PICTURE, bytes);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            mMap.snapshot(callback);

         }
        //count++;
    }
}