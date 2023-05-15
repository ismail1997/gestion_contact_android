package com.ismail.gestion_contact.dialogFragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismail.gestion_contact.R;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private TextView nameTextView;
    private TextView serviceTextView;

    private TextView emailTextView;
    private TextView phoneTextView;
    private ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle mArgs = getArguments();
        String nameContact = mArgs.getString("name");
        String serviceContact = mArgs.getString("service");
        String emailContact = mArgs.getString("email");
        String phoneContact = mArgs.getString("phone");
        String id = mArgs.getString("id");

        View view = inflater.inflate(R.layout.contact_item_detail_layout,container,false);
        nameTextView=view.findViewById(R.id.name_contact_fragment_detail);
        serviceTextView=view.findViewById(R.id.service_contact_fragment_detail);
        imageView=view.findViewById(R.id.image_contact_fragment_dialog);

        uploadImage(id);

        nameTextView.setText(nameContact);
        serviceTextView.setText(serviceContact);
        return view;
    }


    public void uploadImage(String id){
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReference().child("images/"+id);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext())
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .thumbnail(Glide.with(getContext()).load(R.drawable.man))
                        .error(R.drawable.man)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Glide.with(getContext())
                        .load(R.drawable.man)
                        .fitCenter()
                        .into(imageView);
            }
        });

    }
}
