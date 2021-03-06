package com.blinkedge.mehndidesign.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blinkedge.mehndidesign.API.API;
import com.blinkedge.mehndidesign.Modal.Modal;
import com.blinkedge.mehndidesign.R;
import com.blinkedge.mehndidesign.RecyclerViewAdapter.RecyclerViewAllImagesAdapter;
import com.blinkedge.mehndidesign.Singlation;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAllImages extends AppCompatActivity {

    private ShimmerRecyclerView recyclerViewAllImage;
    private StringRequest stringRequest;
    private List<Modal> allImagesModalList;
    public static List<Modal> emptyfavoriteItemData; // Empty List
    private int catId;
    private ImageView iconBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_all_images);

        getSupportActionBar().hide();

        getData();
        jsonResponse();
        ids();
        onClick();

    }

    private void onClick() {
        iconBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Fetching cat_id through Intent from previous activity
    private void getData() {
        try {
            catId = getIntent().getExtras().getInt("cat_id", 0);
            Log.d("catid", String.valueOf(catId));
            if (catId == 0) {
                Toast.makeText(this, "Data is unavailable!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // SetUp recyclerView
    public void setUpRecyclerView() {
        RecyclerViewAllImagesAdapter recyclerViewAllImagesAdapter = new
                RecyclerViewAllImagesAdapter(this, allImagesModalList);
        recyclerViewAllImage.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewAllImage.setAdapter(recyclerViewAllImagesAdapter);

    }

    // Fetching Data From API
    private void jsonResponse() {
        stringRequest = new StringRequest(Request.Method.POST, API.CATEGORY_ALL_IMAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonStatusObject = new JSONObject(response);
                    int status = jsonStatusObject.getInt("status");
                    Log.d("status__", String.valueOf(status));
                    if (status == 1) {
                        JSONArray jsonArray = jsonStatusObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonFetchAllImages = jsonArray.getJSONObject(i);

                            String fetchImage = jsonFetchAllImages.getString("wallpaper_image");
                            int photoId = jsonFetchAllImages.getInt("photo_id");

                            Modal fetchImageModal = new Modal();
                            fetchImageModal.setAllImages(fetchImage);
                            fetchImageModal.setImagesItemId(photoId);

                            allImagesModalList.add(fetchImageModal);
                            Log.d("size_check", allImagesModalList.size() + "_");
                        }
                        setUpRecyclerView();
                    } else {
                        Toast.makeText(CategoryAllImages.this, "Server Busy", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error_", String.valueOf(error));
                Toast.makeText(CategoryAllImages.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            // Passing cat_id
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("cat_id", String.valueOf(catId));
                return hashmap;

            }
        };

        // if server is not getting the response then it shows hits the API in every 5 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singlation.getInstance(this).addToRequestQueue(stringRequest, "");

    }

    // Id Function
    private void ids() {
        recyclerViewAllImage = findViewById(R.id.recycler_view_all_images);
        allImagesModalList = new ArrayList<>();
        iconBackPress = findViewById(R.id.iconBackPress);
    }

}