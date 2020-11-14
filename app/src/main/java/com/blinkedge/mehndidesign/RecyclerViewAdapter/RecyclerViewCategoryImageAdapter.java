package com.blinkedge.mehndidesign.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedge.mehndidesign.Activities.CategoryAllImages;
import com.blinkedge.mehndidesign.Modal.Modal;
import com.blinkedge.mehndidesign.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewCategoryImageAdapter extends RecyclerView.Adapter<TextViewHolder> {

    Context context;
    List<Modal> categoryModals;

    public RecyclerViewCategoryImageAdapter(Context context, List<Modal> categoryModals) {
        this.context = context;
        this.categoryModals = categoryModals;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_category_image, parent, false);

        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, final int position) {
        holder.cat_name_show.setText(categoryModals.get(position).getCatName());
        Picasso.get().load(categoryModals.get(position).getCatImage()).placeholder(R.drawable.loading).into(holder.cat_image_show,
                new Callback() {
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
                Log.d("check_onclick" , "id="+categoryModals.get(position).getCatId());

                // Passing the category ID to fetch all images according to category
                Intent intent = new Intent(context, CategoryAllImages.class);
                intent.putExtra("cat_id", categoryModals.get(position).getCatId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryModals.size();
    }
}

class TextViewHolder extends RecyclerView.ViewHolder {

    ImageView cat_image_show;
    TextView cat_name_show;

    public TextViewHolder(@NonNull View itemView) {
        super(itemView);

        cat_image_show = itemView.findViewById(R.id.cat_image_show);
        cat_name_show = itemView.findViewById(R.id.cat_name_show);

    }
}
