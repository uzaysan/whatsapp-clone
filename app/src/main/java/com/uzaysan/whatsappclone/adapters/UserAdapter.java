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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    RequestManager glide;
    List<User> data = new ArrayList<>();
    private UserAdapter.OnUserClick userClick;


    public UserAdapter(RequestManager glide) {
        this.glide = glide;
    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = data.get(holder.getAdapterPosition());
        glide.load(user.getProfile_photo()).into(holder.profilePhoto);
        holder.name.setText(user.getName());
        holder.username.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePhoto;
        TextView name, username;
        RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profile_photo);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            root = itemView.findViewById(R.id.root);

            if(userClick != null && root != null) {
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userClick.onUserClick(getAdapterPosition());
                    }
                });
            }
        }
    }

    public interface OnUserClick {
        void onUserClick(int position);
    }
    public void setOnClickListener(UserAdapter.OnUserClick userClick) {
        this.userClick = userClick;
    }

}
