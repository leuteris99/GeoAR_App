package com.lefalexiou.geoar_app.layout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.models.Hologram;
import com.lefalexiou.geoar_app.models.Place;
import com.lefalexiou.geoar_app.models.PolylineData;
import com.lefalexiou.geoar_app.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener {
    private static final int REQUEST_LOCATION = 123;
    private Context context;
    private static final String TAG = "MainActivity";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GeoApiContext mGeoApiContext = null;
    private LatLng mUserPosition;
    //    private FloatingActionButton fab; TODO: to remove
    private Button nextButton;
    private Button prevButton;
    private int selectedPlace = -1;
    private List<Marker> markersList = new ArrayList<>();
    private ArrayList<PolylineData> mPolylinesData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MapFragmentListener listener;
    private ArrayList<Place> places;
    private Route changeRoute;
    boolean isCurrentPlaceSelected = false;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: thread processing");

            for (Place place : places) {
                getDeviceLocation(false);
                double distance = Math.sqrt(Math.pow((place.getLatLng().latitude - mUserPosition.latitude), 2) + Math.pow((place.getLatLng().longitude - mUserPosition.longitude), 2));
//                Log.d(TAG, "getNearbyPlace: distance : "+ distance);
                if (distance <= (place.getAOE() * 0.00001)) {
                    listener.onMapDataTransfer(place);
                    isCurrentPlaceSelected = true;
                    break;
                } else  {//if (isCurrentPlaceSelected)
//                    isCurrentPlaceSelected = false;
                    listener.gettingAwayFromPlaces();
                }
            }

            timerHandler.postDelayed(this, 10000);
        }
    };

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        initMap();
//        fab = (FloatingActionButton) v.findViewById(R.id.fab); TODO: to remove
        nextButton = (Button) v.findViewById(R.id.next_bt);
        prevButton = (Button) v.findViewById(R.id.prev_bt);

        timerHandler.postDelayed(timerRunnable, 3000);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);

        if (context instanceof MapFragmentListener) {
            listener = (MapFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        timerHandler.removeCallbacks(timerRunnable);
        Log.d(TAG, "run: onDetach: finishing");

        super.onDetach();

        listener = null;
    }

    private void initMap() {
        Log.d(TAG, "initMap: started");
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_maps_api_key))
                    .build();
        }
    }

    private void getDeviceLocation(boolean toMoveCamera) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: I found you!");
                        Location currentLocation = (Location) task.getResult();

                        assert currentLocation != null;
                        mUserPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                        if (toMoveCamera) {
                            moveCamera(mUserPosition, 15f);
                        }
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(context, "where are you?", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (
                Exception e) {
            Log.e(TAG, "getDeviceLocation: " + e.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: lat:" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void zoomRoute(List<LatLng> latLngList) {
        if (mMap == null || latLngList == null || latLngList.isEmpty())
            return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : latLngList) {
            boundsBuilder.include(latLngPoint);
        }
        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding), 600, null);
    }

    private void resetMap() {
        if (mMap != null) {
            mMap.clear();
            if (markersList.size() > 0) {
                markersList.clear();
                markersList = new ArrayList<>();
            }
            if (mPolylinesData.size() > 0) {
                mPolylinesData.clear();
                mPolylinesData = new ArrayList<>();
            }
            selectedPlace = -1;
        }
    }

    private void resetPlacesList() {
        if (places.size() > 0) {
            places.clear();
            places = new ArrayList<>();
        }
    }

    private void addMarkers(ArrayList<Place> placeArrayList) {//List<Place> placesList
        // TODO: Reset the markers. On live add this when you change all your markers.
        Log.d(TAG, "addMarker: add marker");
        resetMap();
        for (Place place : placeArrayList) {
            MarkerOptions options = new MarkerOptions()
                    .position(place.getLatLng())
                    .title(place.getTitle());
            Marker marker = mMap.addMarker(options);
            markersList.add(marker);
        }
    }

    private void calculateDirections(Marker marker) {
        Log.d(TAG, "calculateDirections: calculating directions");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);
        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        mUserPosition.latitude,
                        mUserPosition.longitude
                )
        );
        Log.d(TAG, "calculateDirections: Destination: " + destination.toString());

        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
                Log.d(TAG, "onResult: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "onResult: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "onResult: geoCodedWayPoint: " + result.geocodedWaypoints[0].toString());

                addPolyLinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "onFailure: Failed to get directions: " + e.getMessage());
            }
        });
    }

    private void addPolyLinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                if (mPolylinesData.size() > 0) {
                    for (PolylineData polylineData : mPolylinesData) {
                        polylineData.getPolyline().remove();
                    }
                    mPolylinesData.clear();
                    mPolylinesData = new ArrayList<>();
                }

                double duration = 9999999;
                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();
                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {
                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(context, R.color.unselected_poly_line));
                    polyline.setClickable(true);
                    mPolylinesData.add(new PolylineData(polyline, route.legs[0]));

                    double tempDuration = route.legs[0].duration.inSeconds;
                    if (tempDuration < duration) {
                        duration = tempDuration;
                        onPolylineClick(polyline);
                        zoomRoute(polyline.getPoints());
                    }
                }
            }
        });
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        for (PolylineData polylineData : mPolylinesData) {
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());

            if (polyline.getId().equals(polylineData.getPolyline().getId())) {
                polylineData.getPolyline().setColor(ContextCompat.getColor(context, R.color.selected_poly_line));
                polylineData.getPolyline().setZIndex(1);
            } else {
                polylineData.getPolyline().setColor(ContextCompat.getColor(context, R.color.unselected_poly_line));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map ready");
        mMap = googleMap;
        mMap.setOnPolylineClickListener(this);

        //getting device position and displaying it on the map
        getDeviceLocation(true);

        //TODO: remove that only for testing
        /*addData(new Place(
                new LatLng(37.971774741979004, 23.72580165163131),
                "Acropolis of Athens",
                50,
                "https://lp-cms-production.imgix.net/2019-06/b3d692e130903b3693711c7b3b1d2a3d887403c4c9ab747773647864e71a51f4.jpg",
                "This is acropolis of athens",
                new ArrayList<>(), ""));
        addData(new Place(
                new LatLng(37.9647036, 23.7312254),
                "Temple of Olympian Zeus",
                40,
                "https://upload.wikimedia.org/wikipedia/commons/b/b0/Attica_06-13_Athens_25_Olympian_Zeus_Temple.jpg",
                "This is the temple of the olympian zeus in athens",
                new ArrayList<>(),
                ""));
        addData(new Place(
                new LatLng(37.96836337537587, 23.7061584792439),
                "Home",
                30,
                "",
                "That's my home",
                new ArrayList<>(),
                ""));
        addData(new Place(
                new LatLng(37.9665086, 23.7081846),
                "Best",
                20,
                "https://s3-eu-west-1.amazonaws.com/static.myjobnow.com/photos/RackMultipart20191121-2151-syopak.png",
                "",
                new ArrayList<>(),
                "https://besttaste.gr/"));*/

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onMapReady: permission deny");
            return;
        }
        mMap.setMyLocationEnabled(true);
        Log.d(TAG, "onMapReady: location enable");

        //TODO: Testing marker remove it on live
        askData(true);

        /*fab.setOnClickListener(new View.OnClickListener() { TODO: to remove
            @Override
            public void onClick(View view) {
                calculateDirections(markersList.get(0));
            }
        });*/
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPlace + 1 >= 0 && selectedPlace + 1 <= markersList.size() - 1) {
                    calculateDirections(markersList.get(++selectedPlace));
                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPlace - 1 >= 0 && selectedPlace - 1 <= markersList.size() - 1) {
                    calculateDirections(markersList.get(--selectedPlace));
                }
            }
        });
    }

    private void askData(boolean isFirstRun) {
        db.collection("marker")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            places = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Map<String, Object> data = document.getData();
                                Map<String, Object> geoData = (Map<String, Object>) data.get("latLng");
                                Map<String, Object> holoData = (Map<String, Object>) data.get("hologram");
                                Log.d(TAG, "onComplete: holodata: answers:" + holoData.get("answerArray") + ", question: " + holoData.get("question"));

                                ArrayList<String> qArray = new ArrayList<>();
                                qArray.add((String) holoData.get("question"));
                                ArrayList<String> tmpAnswers = (ArrayList<String>) holoData.get("answerArray");
                                qArray.addAll(tmpAnswers);

                                LatLng latLng = new LatLng((double) geoData.get("latitude"), (double) geoData.get("longitude"));
                                Hologram hologram = new Hologram(
                                        (String) holoData.get("title"),
                                        (String) holoData.get("imageURL"),
                                        (String) holoData.get("description"),
                                        qArray,
                                        (String) holoData.get("webURL"));
                                Place place = new Place(latLng, (String) data.get("title"), (long) data.get("aoe"), hologram);
//                                Log.d(TAG, "onMap: place: " + place);

                                if (!isFirstRun) {
                                    for (String p : changeRoute.getPlaces()) {
                                        if (place.getTitle().equals(p))
                                            places.add(place);
                                    }
                                }

                            }
                            addMarkers(places);
                            Log.d(TAG, "places: " + places);

                        } else {
                            Log.d(TAG, "onMap: fail");
                        }
                    }
                });
    }

    private void addData(Place place) {
        db.collection("marker")
                .add(place)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onMap: DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public interface MapFragmentListener {
        void onMapDataTransfer(Place nearbyPlace);

        void gettingAwayFromPlaces();
    }

    public void updatePlacesData(Route route) {
        changeRoute = new Route(route.getTitle(), route.getPlaces());
        askData(false);
    }
}