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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    List<Chat> list = new ArrayList<>();
    RequestManager glide;
    ChatAdapterClickListener listener;

    public ChatsAdapter (RequestManager glide) {
        this.glide = glide;
    }
    public void setData(List<Chat> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(ChatAdapterClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        glide.clear(holder.chatIcon);
        holder.chatIcon.setImageDrawable(null);
        holder.chatIcon.setImageBitmap(null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = list.get(holder.getAdapterPosition());
        glide.load(chat.getChatIcon()).into(holder.chatIcon);
        holder.chatName.setText(chat.getChatName());
        holder.chatLastMessage.setText(chat.getLastMessage());
        holder.time.setText(DateUtils.getRelativeTimeSpanString(chat.getUpdatedAt(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout root;
        CircleImageView chatIcon;
        TextView chatName, chatLastMessage, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.chatItemRoot);
            chatIcon = itemView.findViewById(R.id.chatIcon);
            chatName = itemView.findViewById(R.id.chatName);
            chatLastMessage = itemView.findViewById(R.id.chatLastMessage);
            time = itemView.findViewById(R.id.chatUpdatedAt);
            if(listener != null) {
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(getAdapterPosition());
                    }
                });
            }
        }
    }

    public interface ChatAdapterClickListener {
        void onClick(int position);
    }
}
