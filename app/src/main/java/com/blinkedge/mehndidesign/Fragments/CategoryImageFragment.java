package com.blinkedge.mehndidesign.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.blinkedge.mehndidesign.API.API;
import com.blinkedge.mehndidesign.RecyclerViewAdapter.RecyclerViewCategoryImageAdapter;
import com.blinkedge.mehndidesign.Modal.Modal;
import com.blinkedge.mehndidesign.R;
import com.blinkedge.mehndidesign.Singlation;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryImageFragment extends Fragment {

    KProgressHUD kProgressHUD;
    RecyclerView recyclerView;
    StringRequest stringRequest;
    List<Modal> categoryModalList;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category_image, container, false);
        ids(root);
        progressLoader();
        jsonResponse();

        return root;
    }

    private void progressLoader() {
        try {
            kProgressHUD = KProgressHUD.create(getContext())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setMaxProgress(100).setCancellable(true)
                    .setLabel("Please wait...")
                    .setDimAmount(0.5f)
                    .setAutoDismiss(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SetUp recyclerView
    private void setUpRecyclerView(List<Modal> categoryModalList) {
        RecyclerViewCategoryImageAdapter recyclerViewCategoryImageAdapter = new
                RecyclerViewCategoryImageAdapter(getContext(), categoryModalList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewCategoryImageAdapter);
    }

    // Fetching Ddata From API
    private void jsonResponse() {
        stringRequest = new StringRequest(Request.Method.POST, API.GET_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    kProgressHUD.dismiss();
                    Log.d("all_response", response);
                    JSONObject jsonObject = new JSONObject(response);
                    // Fetching Status
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectFetchData;
                            jsonObjectFetchData = jsonArray.getJSONObject(i);

                            // Checking specific response
                            Log.d("specific_response", response);

                            int catId = jsonObjectFetchData.getInt("cat_id");
                            String catName = jsonObjectFetchData.getString("cat_name");
                            String catImg = jsonObjectFetchData.getString("image");

                            Modal categoryModal = new Modal();
                            categoryModal.setCatId(catId);
                            categoryModal.setCatImage(catImg);
                            categoryModal.setCatName(catName);
                            categoryModalList.add(categoryModal);
                        }
                        setUpRecyclerView(categoryModalList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Server Did not responding", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            //Passing app_id 1
            // If there is one or more app on admin panel then we have to pass the app_id so the API will hit the specific app
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("app_id", "1");
                return hashmap;
            }
        };

        // if server is not getting the response then it shows hits the API in every 5 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singlation.getInstance(getContext()).addToRequestQueue(stringRequest, "");

    }

    // Id Function
    public void ids(View root) {
        recyclerView = root.findViewById(R.id.recycler_view_cat_image);
        categoryModalList = new ArrayList<>();
    }
}