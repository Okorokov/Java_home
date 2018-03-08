package com.example.asus.oap;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<Message> list;
    private String nickName;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false));
    }

    public UserAdapter(List<Message> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Message mess=list.get(position);
        holder.textName.setText(mess.name);
        holder.textMessage.setText(mess.massage);
        if (!mess.name.equals(nickName)) {
            holder.textName.setGravity(Gravity.LEFT);
            holder.textMessage.setGravity(Gravity.LEFT);
            Log.d("TEST", mess.name + " = RIGHT");
        }
            else{
            Log.d("TEST", mess.name + " = LEFT");
            holder.textName.setGravity(Gravity.RIGHT);
            holder.textMessage.setGravity(Gravity.RIGHT);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textName,textMessage;
        public UserViewHolder(View itemView) {
            super(itemView);

            textName=itemView.findViewById(R.id.text_Name);
            textMessage=itemView.findViewById(R.id.text_Message);
        }
    }
}
