package com.blinkedge.mehndidesign.RecyclerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.controls.actions.ModeAction;
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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAllImagesAdapter extends RecyclerView.Adapter<AllImagesAdapter> {

    private Context context;
    private List<Modal> allImagesList;
    public  List<Modal> emptyDataList; // empty
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public RecyclerViewAllImagesAdapter(Context context, List<Modal> modalList) {
        this.context = context;
        this.allImagesList = modalList;

        emptyDataList = new ArrayList<>();

        gson = new Gson();
        sharedPreferences = context.getSharedPreferences("MehndiDesign", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        try {
            String list = sharedPreferences.getString("imageshow", "");
            if (list != null) {
                if (!list.equals("")) {
                    Log.d("checkList", "= checkList");
                    emptyDataList = gson.fromJson(list, new TypeToken<List<Modal>>() {
                    }.getType());
                    if (emptyDataList != null) {
                        if (!emptyDataList.isEmpty()) {
                            for (int indexOuter = 0; indexOuter < allImagesList.size(); indexOuter++) {
                                boolean check = false;
                                for (int index = 0; index < emptyDataList.size(); index++) {
                                    if (emptyDataList.get(index).getImagesItemId() ==
                                            (allImagesList.get(indexOuter).getImagesItemId()))
                                        check = true;
                                }
                                allImagesList.get(indexOuter).setFavouriteItems(check);
                            }
                        } else
                            Toast.makeText(context, "Favourite list is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        emptyDataList = new ArrayList<>();
                    }
                }
            } else {
                Toast.makeText(context, "Favourite items not found!restart the app", Toast.LENGTH_SHORT).show();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }


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

        if (allImagesList.get(position).isFavouriteItems()) {
            holder.ic_un_favourite.setVisibility(View.INVISIBLE);
            holder.ic_favorite.setVisibility(View.VISIBLE);
        } else {
            holder.ic_favorite.setVisibility(View.INVISIBLE);
            holder.ic_un_favourite.setVisibility(View.VISIBLE);
        }

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

                // add to favourite
                emptyDataList.add(allImagesList.get(position));
                String save = gson.toJson(emptyDataList);
                Log.d("save", save);
                editor.putString("imageshow", save).apply();
                Toast.makeText(context, "item favourite successfully!", Toast.LENGTH_SHORT).show();

            }
        });

        // remove from favourite
        holder.ic_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ic_favorite.setVisibility(View.INVISIBLE);
                holder.ic_un_favourite.setVisibility(View.VISIBLE);

                // remove from favourite
                String list = sharedPreferences.getString("imageshow", "");
                if (list != null) {
                    emptyDataList = gson.fromJson(list, new TypeToken<List<Modal>>() {
                    }.getType());
                    if (!list.equals("")) {
                        for (int i = 0; i < emptyDataList.size(); i++) {
                            if (emptyDataList.get(i).getImagesItemId() == allImagesList.get(position).getImagesItemId()) {
                                emptyDataList.remove(i);
                                Toast.makeText(context, "item removed from favourite successfully!", Toast.LENGTH_SHORT).show();
                                String s = gson.toJson(emptyDataList);
                                editor.putString("imageshow", s).apply();
                                break;
                            }
                        }
                    } else Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(context, "List is null", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void save(int position) {
        emptyDataList.add(allImagesList.get(position));
        String save = gson.toJson(emptyDataList);
        Log.d("save", save);
        editor.putString("imageshow", save).apply();
        Toast.makeText(context, "item favourite successfully!", Toast.LENGTH_SHORT).show();
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