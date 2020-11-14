package com.blinkedge.mehndidesign.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedge.mehndidesign.Activities.CategoryAllImages;
import com.blinkedge.mehndidesign.Activities.FullimageScreen;
import com.blinkedge.mehndidesign.Modal.Modal;
import com.blinkedge.mehndidesign.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecyclerViewAllImagesAdapter extends RecyclerView.Adapter<AllImagesAdapter> {

    private Context context;
    private List<Modal> allImagesList;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public RecyclerViewAllImagesAdapter(Context context, List<Modal> modalList) {
        this.context = context;
        this.allImagesList = modalList;

        sharedPreferences = context.getSharedPreferences("images", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(allImagesList);
        editor.putString("images", json);
        editor.apply();

    }

    @NonNull
    @Override
    public AllImagesAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.custom_all_images, parent, false);

        return new AllImagesAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllImagesAdapter holder, final int position) {
        Log.d("path", allImagesList.get(position).getAllImages());
        Picasso.get().load(allImagesList.get(position).getAllImages()).placeholder(R.drawable.loading)
                .into(holder.all_image_show, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(context, "Image Error", Toast.LENGTH_SHORT).show();
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullimageScreen.class);
                intent.putExtra("imagePath", allImagesList.get(position).getAllImages());
                context.startActivity(intent);
            }
        });

        holder.ic_un_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ic_un_favourite.setVisibility(View.INVISIBLE);
                holder.ic_favorite.setVisibility(View.VISIBLE);


            }
        });

        holder.ic_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ic_favorite.setVisibility(View.INVISIBLE);
                holder.ic_un_favourite.setVisibility(View.VISIBLE);

                Toast.makeText(context, "Image remove from favourite", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return allImagesList.size();
    }
}

class AllImagesAdapter extends RecyclerView.ViewHolder {

    ImageView all_image_show;
    ImageView ic_un_favourite;
    ImageView ic_favorite;

    public AllImagesAdapter(@NonNull View itemView) {
        super(itemView);

        all_image_show = itemView.findViewById(R.id.all_image_show);
        ic_un_favourite = itemView.findViewById(R.id.ic_un_favourite);
        ic_favorite = itemView.findViewById(R.id.ic_favorite);


    }
}