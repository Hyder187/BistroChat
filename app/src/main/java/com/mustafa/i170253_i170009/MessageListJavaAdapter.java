package com.mustafa.i170253_i170009;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageListJavaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    ArrayList<Message> list;
    String rid;
    String sid;
    public static final int MESSAGE_TYPE_IN = 1;
    public static final int MESSAGE_TYPE_OUT = 2;
    public static final int MESSAGE_TYPE_OUT_IMAGE = 3;
    public static final int MESSAGE_TYPE_IN_IMAGE = 4;
    public static final int MESSAGE_TYPE_OUT_IMAGE_ONLY = 5;
    public static final int MESSAGE_TYPE_IN_IMAGE_ONLY = 6;

    public MessageListJavaAdapter(Context context, ArrayList<Message> list,String rid,String sid) { // you can pass other parameters in constructor
        this.context = context;
        this.sid=sid;
        for(int i=0;i<list.size();i++){
            Log.d("messages",list.get(i).text);
        }
        this.list=list;

        this.rid=rid;
    }

    private class MessageInViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV;
        MessageInViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.text_received);

        }
        void bind(int position) {
            Message message = list.get(position);
            messageTV.setText(message.text);

        }
    }

    private class MessageOutViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV;
        MessageOutViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.text_sent);

        }
        void bind(int position) {
            Message message = list.get(position);
            messageTV.setText(message.text);

        }
    }

    private class MessageImageInViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV;
        ImageView image;
        MessageImageInViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.text_received_image);
            image=itemView.findViewById(R.id.image_received);


        }
        void bind(int position) {
            Message message = list.get(position);
            messageTV.setText(message.text);
            if(message.getContainsImage()){
                Picasso.get().load(message.getImgSrc()).fit().centerCrop().into(image);
            }


        }
    }

    private class MessageImageOutViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV;
        ImageView image;
        MessageImageOutViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.text_sent_image);
            image=itemView.findViewById(R.id.image_sent);


        }
        void bind(int position) {
            Message message = list.get(position);
            messageTV.setText(message.text);
            if(message.getContainsImage()){
                Picasso.get().load(message.getImgSrc()).fit().centerCrop().into(image);
            }


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_TYPE_IN) {
            return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.message_received, parent, false));
        }
        else if(viewType == MESSAGE_TYPE_OUT){
            return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false));
        }
        else if(viewType == MESSAGE_TYPE_OUT_IMAGE){
            return new MessageImageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.message_sent_image, parent, false));
        }
        else{
            return new MessageImageInViewHolder(LayoutInflater.from(context).inflate(R.layout.message_received_image, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (list.get(position).senderId.equals(rid)  && list.get(position).containsImage.equals(false)) {
            ((MessageInViewHolder) holder).bind(position);
        }
        else if(list.get(position).senderId.equals(rid)  && list.get(position).containsImage.equals(true)){
            ((MessageImageInViewHolder) holder).bind(position);
        }
        else if(list.get(position).senderId.equals(sid) && list.get(position).containsImage.equals(true)){
            ((MessageImageOutViewHolder) holder).bind(position);
        }
        else{
            ((MessageOutViewHolder) holder).bind(position);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).senderId.equals(rid)  && list.get(position).containsImage.equals(false)) {
            return 1;
        }
        else if(list.get(position).senderId.equals(rid) && list.get(position).containsImage.equals(true)){
            return 4;
        }
        else if(list.get(position).senderId.equals(sid)&& list.get(position).containsImage.equals(false)){
            return 2;
        }
        else{
            return 3;
        }


    }
}
