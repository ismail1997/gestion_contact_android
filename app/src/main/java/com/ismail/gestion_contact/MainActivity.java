package com.ismail.gestion_contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismail.gestion_contact.adapters.MyRecyclerViewAdapter;
import com.ismail.gestion_contact.dialogFragments.DialogFragment;
import com.ismail.gestion_contact.listners.ContactRecyclerViewListener;
import com.ismail.gestion_contact.models.Contact;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ContactRecyclerViewListener {

    ArrayList<Contact> models = new ArrayList<>();
    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    ProgressDialog pd;

    private FloatingActionButton floatingActionButton;
    private MyRecyclerViewAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("List Contacts");

        db=FirebaseFirestore.getInstance();
        pd=new ProgressDialog(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //remplirData();
        showData();


        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddContactActivity.class);
                startActivity(intent);
            }
        });

    }




    private void remplirData(){
        Contact contact = new Contact("Fatim Ismail","Departement informatique","s","","","");
        models.add(contact);
        contact = new Contact("Bouaddi ismail ","","Departement informatique","m","","");
        models.add(contact);
        contact = new Contact( "John Batista","Departement informatique","","","","");
        models.add(contact);
        contact = new Contact("Bouaddi ismail ","Departement informatique","m","","","");
        models.add(contact);
        contact = new Contact( "John Batista","Departement informatique","","","","");
        models.add(contact);
        contact = new Contact("Bouaddi ismail ","Departement informatique","m","","","");
        models.add(contact);
        contact = new Contact( "John Batista","Departement informatique","","","","");
        models.add(contact);
        contact = new Contact("Bouaddi ismail ","Departement informatique","m","","","");
        models.add(contact);
        contact = new Contact( "John Batista","Departement informatique","","","","");
        models.add(contact);

        Log.d("love",""+models);
    }


    private void showData(){
        pd.setTitle("Loading Title ....");
        pd.show();

        db.collection("Contacts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pd.dismiss();
                        for(DocumentSnapshot doc : task.getResult()){
                            Contact contact = new Contact(
                                    doc.getString("name"),
                                    doc.getString("service"), "",
                                    doc.getString("email"),
                                    doc.getString("phone"),doc.getId()
                            );
                            models.add(contact);
                        }
                        adapter=  new MyRecyclerViewAdapter(models, getApplicationContext(), new ContactRecyclerViewListener() {
                            @Override
                            public void onItemClick(int position) {
                                DialogFragment dialogFragment=new DialogFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("name",models.get(position).getName());
                                bundle.putString("service",models.get(position).getService());

                                dialogFragment.setArguments(bundle);
                                dialogFragment.show(getSupportFragmentManager(),"My  Fragment");
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        Log.d("ISMAIL BOUADDI",""+models);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Error Retrieving Data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        DialogFragment dialogFragment=new DialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name",models.get(position).getName());
        bundle.putString("service",models.get(position).getService());

        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"My  Fragment");
        /*Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
        intent.putExtra("name",models.get(position).getName());
        intent.putExtra("lastMessage",models.get(position).getEmail());
        startActivity(intent);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                models.clear();
                showData();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchData(String query) {
        pd.setTitle("Searching ....");
        pd.show();

        db.collection("Contacts").whereEqualTo("name",query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pd.dismiss();
                        models.clear();
                        for(DocumentSnapshot doc : task.getResult()){
                            Contact contact = new Contact(
                                    doc.getString("name"),
                                    doc.getString("service"), "",
                                    doc.getString("email"),
                                    doc.getString("phone"),doc.getId()
                            );
                            models.add(contact);
                        }
                        adapter=  new MyRecyclerViewAdapter(models, getApplicationContext(), new ContactRecyclerViewListener() {
                            @Override
                            public void onItemClick(int position) {
                                DialogFragment dialogFragment=new DialogFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("name",models.get(position).getName());
                                bundle.putString("service",models.get(position).getService());

                                dialogFragment.setArguments(bundle);
                                dialogFragment.show(getSupportFragmentManager(),"My  Fragment");
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "error in search", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:{
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Settings is clicked", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 101:{

                Intent intent = new Intent(this,EditActivity.class);

                intent.putExtra("name",models.get(item.getGroupId()).getName());
                intent.putExtra("email",models.get(item.getGroupId()).getEmail());
                intent.putExtra("service",models.get(item.getGroupId()).getService());
                intent.putExtra("phone",models.get(item.getGroupId()).getPhone());
                intent.putExtra("id",models.get(item.getGroupId()).getId());
                startActivity(intent);

                Log.d("List",models.get(item.getGroupId())+"");
                break;
            }
            case 102:{
                deleteContact(item.getGroupId());
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void deleteContact(int index){
        pd.setTitle("Deleting Contact ....");
        pd.show();

        db.collection("Contacts").document(models.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                        models.remove(index);
                        adapter.notifyItemRemoved(index);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}