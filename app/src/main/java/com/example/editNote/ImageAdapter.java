package com.example.editNote;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DataBase.Notes;
import com.example.myNote.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
public static List<Uri> uriPictures;

    public ImageAdapter(List<Uri> uriPictures) {
        this.uriPictures = uriPictures;
    }

    public void setImages(Uri uriImages ) {
        uriPictures.add(uriImages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = uriPictures.get(position);
        holder.ivPicture.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return uriPictures.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
      private final ImageView ivPicture;
      private final ImageView ivCloseButton;


      public ImageViewHolder(@NonNull View itemView) {
          super(itemView);
          ivPicture = itemView.findViewById(R.id.ivItemPicture);
          ivCloseButton = itemView.findViewById(R.id.ivButtonClosePicture);
      }
  }

}

