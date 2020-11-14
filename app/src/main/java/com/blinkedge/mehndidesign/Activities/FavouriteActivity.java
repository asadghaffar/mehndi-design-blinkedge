package com.blinkedge.mehndidesign.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedge.mehndidesign.Modal.Modal;
import com.blinkedge.mehndidesign.R;
import com.blinkedge.mehndidesign.RecyclerViewAdapter.FavouriteRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView imageFavoriteRecycler;
    private Gson gson;
    List<Modal> favList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        favList = new ArrayList<>();
        gson = new Gson();
        sharedPreferences = this.getSharedPreferences("MehndiDesign", Context.MODE_PRIVATE);
        id();
        loadData();

    }

    private void loadData() {
        String imageshow = sharedPreferences.getString("imageshow", "");
        Log.d("asdfse__", imageshow + "__");
        if (imageshow != null) {
            if (!imageshow.equals("")) {
                favList = gson.fromJson(imageshow, new TypeToken<List<Modal>>() {
                }.getType());
                if (favList.size() != 0) {
                    if (favList != null) {
                        Log.d("asdfwe__", favList.size() + "");
                        FavouriteRecyclerView favouriteRecyclerView = new FavouriteRecyclerView(FavouriteActivity.this, favList);
                        imageFavoriteRecycler.setLayoutManager(new LinearLayoutManager(this));
                        imageFavoriteRecycler.setAdapter(favouriteRecyclerView);
                    } else {
                        Toast.makeText(this, "fav list is not found", Toast.LENGTH_SHORT).show();
                        Log.d("fvtlistNotFound", "error = " +favList.size());
                    }
                } else {
                    Toast.makeText(this, "fvt list equal to zero", Toast.LENGTH_SHORT).show();
                    Log.d("FvtList", "error = " +favList.size());
                }
            } else {
                Toast.makeText(this, "image show equal to zero", Toast.LENGTH_SHORT).show();
                Log.d("imageShowEqualZero", "error = " + imageshow);
            }

        } else {
            Toast.makeText(this, "image show is null", Toast.LENGTH_SHORT).show();
            Log.d("imgaesShowNull", "error = " + imageshow);
        }
    }

    private void id() {
        imageFavoriteRecycler = findViewById(R.id.imageFavoriteRecycler);
    }
}