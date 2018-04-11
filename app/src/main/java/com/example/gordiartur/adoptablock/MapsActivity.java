package com.example.gordiartur.adoptablock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnMapClickListener {

    private static final int COLOR_TRANSPARENT_GREEN = 0x55388E3C;
    private static final int COLOR_TRANSPARENT_RED = 0x55FF0000;

    private static final int POLYGON_STROKE_WIDTH_PX = 3;
    private static final int PATTERN_DASH_LENGTH_PX = 10;
    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA =
            Arrays.asList(DASH, GAP);

    private ArrayList<Polygon> polygonList = new ArrayList<>();
    private Polygon currentPolygon;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        userData = ((UserData) getApplicationContext());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_map);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        InputStream inputStream = getResources().openRawResource(R.raw.blocks);

        Scanner scan = new Scanner(inputStream);

        String str = "";

        while(scan.hasNextLine()) {
            String line = scan.nextLine();

            str = str + line;
        }

        try {
            JSONObject root = new JSONObject(str);
            JSONArray features = root.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject first = features.getJSONObject(i);
                JSONObject geometry = first.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");

                JSONObject properties = first.getJSONObject("properties");
                String block = properties.getString("BLOCK");
                Double area = properties.getDouble("Shape_Area");

                System.out.println(block);

                for (int j = 0; j < coordinates.length(); j++) {

                    JSONArray second = coordinates.getJSONArray(j);
                    ArrayList<LatLng> latLng = new ArrayList<>();

                    for (int k = 0; k < second.length(); k++) {

                        JSONArray third = second.getJSONArray(k);

                        latLng.add(new LatLng(third.getDouble(1), third.getDouble(0)));
                    }

                    PolygonOptions options = new PolygonOptions();

                    for (LatLng location : latLng) {
                        options.add(location);
                    }

                    Polygon polygon = googleMap.addPolygon(options.clickable(true));
                    polygon.setTag(block + " " + area);
                    stylePolygon(polygon);
                    polygonList.add(polygon);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LatLng newWest = new LatLng(49.2057, -122.9110);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newWest, 13));
        googleMap.setOnPolygonClickListener(this);
        googleMap.setOnMapClickListener(this);
    }

    /**
     * Styles the polygon.
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
        polygon.setStrokePattern(PATTERN_POLYGON_ALPHA);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setFillColor(COLOR_TRANSPARENT_GREEN);
    }

    /**
     * Listens for clicks on a polygon.
     * @param polygon The polygon object that the user has clicked.
     */
    @Override
    public void onPolygonClick(Polygon polygon) {
        currentPolygon = polygon;
        LinearLayout blockInfo = findViewById(R.id.blockInfo);

        TextView numAdoptees = findViewById(R.id.numAdoptees);
        TextView spotsAvailable = findViewById(R.id.spotsAvailable);

        String tag = (String) polygon.getTag();
        String[] split = tag.split(" ");
        String name = split[0];
        Double area = Double.parseDouble(split[1]);
        int totalAdoptees;

        if (area < 50000) {
            totalAdoptees = 2;
        } else if (area < 100000) {
            totalAdoptees = 4;
        } else if (area < 200000) {
            totalAdoptees = 6;
        } else {
            totalAdoptees = 8;
        }

        for (Polygon p : polygonList) {
            if (!p.equals(polygon)) {
                p.setFillColor(COLOR_TRANSPARENT_GREEN);
                blockInfo.setVisibility(View.INVISIBLE);
            }
        }

        if (polygon.getFillColor() == COLOR_TRANSPARENT_GREEN) {
            polygon.setFillColor(COLOR_TRANSPARENT_RED);
            blockInfo.setVisibility(View.VISIBLE);
            String numAdopteeText = getString(R.string.adopted_by) + 0; // SET TO GET NUMBER OF ADOPTEES FROM DB
            String spotsAvailableText = getString(R.string.spots_available) + totalAdoptees; // SUBTRACT NUMBER OF ADOPTEES
            numAdoptees.setText(numAdopteeText);
            spotsAvailable.setText(spotsAvailableText);
            blockInfo.bringToFront();
        } else {
            polygon.setFillColor(COLOR_TRANSPARENT_GREEN);
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        LinearLayout blockInfo = findViewById(R.id.blockInfo);

        for (Polygon p : polygonList) {
            p.setFillColor(COLOR_TRANSPARENT_GREEN);
            blockInfo.setVisibility(View.INVISIBLE);

        }
    }

    public void onAdoptClick(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        String tag = (String) currentPolygon.getTag();
        String[] split = tag.split(" ");
        final String blockName = split[0];

        if (userData.isAuthenticated()) {
            if (userData.getBlockName().equals("")) {
                dialogBuilder.setTitle(getString(R.string.map_confirm));

                dialogBuilder
                        .setMessage(getString(R.string.confirm_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                userData.setBlockName(blockName);
                                userData.incrementBlockAdoptedBy(blockName);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
            } else {
                dialogBuilder.setTitle(getString(R.string.map_warning));

                dialogBuilder
                        .setMessage(getString(R.string.warning_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                userData.decrementBlockAdoptedBy(userData.getBlockName());
                                userData.setBlockName(blockName);
                                userData.incrementBlockAdoptedBy(blockName);

                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
            }
        } else {
            dialogBuilder.setTitle("");

            dialogBuilder
                    .setMessage(getString(R.string.signin_message))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.signin_prompt), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getString(R.string.signin_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        AlertDialog dialog = dialogBuilder.create();

        dialog.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_block:
                    Intent intentBlock = new Intent(getApplicationContext(), BlockActivity.class);
                    startActivity(intentBlock);
                    return true;
                case R.id.navigation_profile:
                    Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intentProfile);
                    return true;
                case R.id.navigation_map:
                    Intent intentMaps = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intentMaps);
                    return true;
                case R.id.navigation_more:
                    Intent intentMore = new Intent(getApplicationContext(), MoreActivity.class);
                    startActivity(intentMore);
                    return true;
            }
            return false;
        }
    };
}
