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
import com.uzaysan.whatsappclone.R;
import com.uzaysan.whatsappclone.models.chat.Chat;
import com.uzaysan.whatsappclone.models.message.Message;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    List<Message> list = new ArrayList<>();
    RequestManager glide;
    ChatAdapterClickListener listener;

    public MessagesAdapter(RequestManager glide) {
        this.glide = glide;
    }
    public void setData(List<Message> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(ChatAdapterClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_by_me_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = list.get(holder.getAdapterPosition());
        holder.message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
        }
    }

    public interface ChatAdapterClickListener {
        void onClick(String id);
    }
}
