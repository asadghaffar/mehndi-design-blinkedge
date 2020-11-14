package com.blinkedge.mehndidesign.RecyclerViewAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedge.mehndidesign.Modal.Modal;
import com.blinkedge.mehndidesign.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteRecyclerView extends RecyclerView.Adapter<favouriteRecyclerView>{

    private Context context;
    private List<Modal> modals;

    private Gson gson;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public FavouriteRecyclerView(Context context, List<Modal> modal) {
        this.context = context;
        this.modals = modal;

        gson = new Gson();
        sharedPreferences = context.getSharedPreferences("mehndidesign", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public favouriteRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View  view = LayoutInflater.from(context).inflate(R.layout.custom_favourite_view, parent, false);

        return new favouriteRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favouriteRecyclerView holder, final int position) {

        Glide.with(context).load(modals.get(position).getAllImages()).centerCrop().placeholder(R.drawable.loading).
                into(holder.favourite_all_image_show);

        holder.un_favourite_ic_un_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modals.remove(position);
                String removeImageUrl = gson.toJson(modals);
                editor.putString("imageshow", removeImageUrl).apply();
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return modals.size();
    }
}

class favouriteRecyclerView extends RecyclerView.ViewHolder {

    ImageView favourite_all_image_show;
    ImageView un_favourite_ic_un_favourite;
    ImageView favourite_ic_favorite;

    public favouriteRecyclerView(@NonNull View itemView) {
        super(itemView);

        favourite_all_image_show = itemView.findViewById(R.id.favourite_all_image_show);
        un_favourite_ic_un_favourite = itemView.findViewById(R.id.un_favourite_ic_un_favourite);
        favourite_ic_favorite = itemView.findViewById(R.id.favourite_ic_favorite);
    }
}