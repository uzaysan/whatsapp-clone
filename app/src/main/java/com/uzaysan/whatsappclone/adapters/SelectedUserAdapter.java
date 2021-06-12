package com.uzaysan.whatsappclone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.models.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedUserAdapter extends RecyclerView.Adapter<SelectedUserAdapter.ViewHolder> {

    RequestManager glide;
    List<User> data = new ArrayList<>();
    private SelectedUserAdapter.OnUserRemove userRemove;


    public SelectedUserAdapter(RequestManager glide) {
        this.glide = glide;
    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = data.get(holder.getAdapterPosition());
        glide.load(user.getProfile_photo()).into(holder.profilePhoto);
        holder.name.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePhoto;
        TextView name;
        RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profile_photo);
            name = itemView.findViewById(R.id.name);
            root = itemView.findViewById(R.id.root);

            if(userRemove != null && root != null) {
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userRemove.onUserRemove(getAdapterPosition());
                    }
                });
            }
        }
    }

    public interface OnUserRemove {
        void onUserRemove(int position);
    }

    public void setOnRemoveListener(SelectedUserAdapter.OnUserRemove userRemove) {
        this.userRemove = userRemove;
    }

}
