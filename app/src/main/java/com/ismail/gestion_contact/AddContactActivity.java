package com.ismail.gestion_contact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddContactActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView changeImageTextView;
    private EditText emailEditText,nameEditText,serviceEditText,phoneEditText;
    private Button addContactButton;

    //Progress dialog
    ProgressDialog progressDialog;

    //Firebase
    FirebaseFirestore fireStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Contact");
        //firebase fire store
        fireStore=FirebaseFirestore.getInstance();

        //progress Dialog
        progressDialog=new ProgressDialog(this);


        imageView = findViewById(R.id.contact_image_add);
        changeImageTextView=findViewById(R.id.changeImage);
        emailEditText=findViewById(R.id.email_contact_edittext);
        nameEditText=findViewById(R.id.name_contact_edittext);
        phoneEditText=findViewById(R.id.phone_contact_edittext);
        serviceEditText=findViewById(R.id.service_contact_edittext);


        addContactButton=findViewById(R.id.add_contact_button);
        changeImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent,3);
            }
        });


        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String service=serviceEditText.getText().toString();

                uploadData(name,email,phone,service);
            }
        });

    }

    private void uploadData(String name, String email, String phone, String service) {
        progressDialog.setTitle("Adding data to firebase");
        progressDialog.show();
        String id= UUID.randomUUID().toString();
        Map<String,Object> doc = new HashMap<>();
        doc.put("id",id);
        doc.put("name",name);
        doc.put("email",email);
        doc.put("phone",phone);
        doc.put("service",service);

        fireStore.collection("Contacts").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(AddContactActivity.this, "Data is added to firebase success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddContactActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddContactActivity.this, "Data is failed to added to firebase success", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && data != null){
            Uri uri = data.getData();
            Log.d("ISMAIL",uri.toString());
            imageView.setImageURI(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
