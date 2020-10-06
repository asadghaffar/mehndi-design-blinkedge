package com.blinkedge.mehndidesign.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedge.mehndidesign.Activities.FullimageScreen;
import com.blinkedge.mehndidesign.Modal.Modal;
import com.blinkedge.mehndidesign.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Logger;

public class RecyclerViewAllImagesAdapter extends RecyclerView.Adapter<AllImagesAdapter> {

    Context context;
    List<Modal> allImagesList;

    public RecyclerViewAllImagesAdapter(Context context, List<Modal> modalList) {
        this.context = context;
        this.allImagesList = modalList;

    }

    @NonNull
    @Override
    public AllImagesAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.custom_all_images, parent, false);

        return new AllImagesAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllImagesAdapter holder, final int position) {
        Log.d("path", allImagesList.get(position).getAllImages());
        Picasso.get().load(allImagesList.get(position).getAllImages()).placeholder(R.drawable.loading)
                .into(holder.all_image_show, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(context, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,FullimageScreen.class);
                intent.putExtra("imagePath",allImagesList.get(position).getAllImages());
                context.startActivity(intent);
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

    public AllImagesAdapter(@NonNull View itemView) {
        super(itemView);

       all_image_show = itemView.findViewById(R.id.all_image_show);

    }
}