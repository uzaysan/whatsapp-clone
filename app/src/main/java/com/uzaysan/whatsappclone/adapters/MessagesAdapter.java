package com.uzaysan.whatsappclone.adapters;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.parse.ParseUser;
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.message.Message;
import com.uzaysan.whatsappclone.models.message.MessageWithUser;
import com.uzaysan.whatsappclone.models.user.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    List<MessageWithUser> list;
    RequestManager glide;
    ChatAdapterClickListener listener;

    private final int TYPE_ME = 1;
    private final int TYPE_OTHERS = 2;

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getMessage().getOwner().equals(ParseUser.getCurrentUser().getObjectId())) {
            return TYPE_ME;
        } else {
            return TYPE_OTHERS;
        }
    }

    public MessagesAdapter(RequestManager glide,List<MessageWithUser> list) {
        this.glide = glide;
        this.list = list;
    }

    public void setOnClickListener(ChatAdapterClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        if(holder.profilePhoto != null) {
            glide.clear(holder.profilePhoto);
            holder.profilePhoto.setImageBitmap(null);
            holder.profilePhoto.setImageDrawable(null);
        }
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_by_me_layout,parent,false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_by_others_layout,parent,false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = list.get(holder.getAdapterPosition()).getMessage();

        holder.message.setText(message.getMessage());
        holder.time.setVisibility(View.GONE);
        holder.time.setText(DateUtils.getRelativeTimeSpanString(message.getCreated_at(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));

        if(getItemViewType(holder.getAdapterPosition()) == TYPE_OTHERS) {

            User user = list.get(holder.getAdapterPosition()).getUser();
            if(user == null) return;

            if(holder.getAdapterPosition() + 1 < list.size()
                    && list.get(holder.getAdapterPosition() + 1).getUser().getId().equals(user.getId())){
                holder.profilePhoto.setVisibility(View.GONE);
                holder.name.setVisibility(View.GONE);
            }
            else {
                holder.profilePhoto.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
                glide.load(user.getProfile_photo()).into(holder.profilePhoto);
                holder.name.setText(user.getName());
            }

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView message, time, name;
        RelativeLayout root;
        CircleImageView profilePhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            root = itemView.findViewById(R.id.root);
            profilePhoto = itemView.findViewById(R.id.profile_photo);
            name = itemView.findViewById(R.id.name);

            if(root != null) {
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        time.setVisibility(time.getVisibility() == View.VISIBLE ? View.GONE: View.VISIBLE);
                    }
                });
            }
        }


    }

    public interface ChatAdapterClickListener {
        void onClick(String id);
    }
}
