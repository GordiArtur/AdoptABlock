package com.example.gordiartur.adoptablock;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
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
        GoogleMap.OnPolygonClickListener {

    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

                for (int j = 0; j < coordinates.length(); j++) {

                    JSONArray second = coordinates.getJSONArray(j);
                    ArrayList<LatLng> latLng = new ArrayList<LatLng>();

                    for (int k = 0; k < second.length(); k++) {

                        JSONArray third = second.getJSONArray(k);

                        latLng.add(new LatLng(third.getDouble(1), third.getDouble(0)));
                    }

                    PolygonOptions options = new PolygonOptions();

                    for (LatLng location : latLng) {
                        options.add(location);
                    }

                    Polygon polygon = googleMap.addPolygon(options.clickable(true));
                    polygon.setTag(j);
                    stylePolygon(polygon);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LatLng newWest = new LatLng(49.2057, -122.9110);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newWest, 13));
        googleMap.setOnPolygonClickListener(this);
    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
        polygon.setStrokePattern(PATTERN_POLYGON_ALPHA);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(COLOR_GREEN_ARGB);
        polygon.setFillColor(COLOR_PURPLE_ARGB);
    }

    /**
     * Listens for clicks on a polygon.
     * @param polygon The polygon object that the user has clicked.
     */
    @Override
    public void onPolygonClick(Polygon polygon) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor() ^ 0x00ffffff;
        polygon.setStrokeColor(color);
        color = polygon.getFillColor() ^ 0x00ffffff;
        polygon.setFillColor(color);

        Toast.makeText(this, "Area number " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
    }
}
