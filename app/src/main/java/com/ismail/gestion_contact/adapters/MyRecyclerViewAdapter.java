package com.ismail.gestion_contact.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismail.gestion_contact.R;
import com.ismail.gestion_contact.listners.ContactRecyclerViewListener;
import com.ismail.gestion_contact.models.Contact;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ContactHolder> {


    private ArrayList<Contact> contacts = new ArrayList<>();
    private Context context ;
    private ContactRecyclerViewListener listener;
    public MyRecyclerViewAdapter(ArrayList<Contact> contacts,Context context,ContactRecyclerViewListener listener){
        this.contacts=contacts;
        this.context=context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ContactHolder(view,this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ContactHolder holder, int position) {
        holder.contactNameView.setText(contacts.get(position).getName());
        holder.lastMessageView.setText(contacts.get(position).getService());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private ImageView imageView;
        private TextView contactNameView,lastMessageView;

        public ContactHolder(@NonNull View itemView,ContactRecyclerViewListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.contact_image);
            contactNameView=itemView.findViewById(R.id.nameContactTextView);
            lastMessageView=itemView.findViewById(R.id.emailContact);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position =getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Choose option");
            contextMenu.add(getAdapterPosition(),101,0,"Edit contact");
            contextMenu.add(getAdapterPosition(),102,1,"Delete Contact");
        }
    }

}
