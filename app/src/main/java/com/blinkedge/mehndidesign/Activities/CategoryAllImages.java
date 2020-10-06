package com.blinkedge.mehndidesign.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAllImages extends AppCompatActivity {

    RecyclerView recyclerViewAllImage;
    KProgressHUD kProgressHUD;
    StringRequest stringRequest;
    List<Modal> allImagesModalList;
    int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_all_images);

        /*progressLoader();*/
        getData();
        jsonResponse();
        ids();

    }

    // Fetching cat_id through Intent from previous activity
    private void getData() {
        try {
            catId = getIntent().getExtras().getInt("cat_id", 0);
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

    // Fetching Ddata From API
    private void jsonResponse() {
        stringRequest = new StringRequest(Request.Method.POST, API.CATEGORY_ALL_IMAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /* kProgressHUD.dismiss();*/
                try {
                    JSONObject jsonStatusObject = new JSONObject(response);
                    int status = jsonStatusObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonStatusObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonFetchAllImages = jsonArray.getJSONObject(i);

                            String fetchImage = jsonFetchAllImages.getString("wallpaper_image");

                            Modal fetchImageModal = new Modal();
                            fetchImageModal.setAllImages(fetchImage);

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

   /* private void progressLoader()
     {
        try {
            kProgressHUD = KProgressHUD.create(CategoryAllImages.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setMaxProgress(100).setCancellable(true)
                    .setLabel("Please wait...")
                    .setDimAmount(0.5f)
                    .setAutoDismiss(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    // Id Function
    private void ids() {
        recyclerViewAllImage = findViewById(R.id.recycler_view_all_images);
        allImagesModalList = new ArrayList<>();
    }

}