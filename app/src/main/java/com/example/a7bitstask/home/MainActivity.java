package com.example.a7bitstask.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a7bitstask.R;
import com.example.a7bitstask.otp.MobileNumberGetActivity;
import com.example.a7bitstask.user.UserDataSession;
import com.example.a7bitstask.user.UserEntityData;
import com.example.a7bitstask.user.activity.ProfilePageActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<ProductList> productLists = new ArrayList<>();
    ProductList refProductList = new ProductList();
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (id != null) {
            onUserDataFetching(id);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = database.getReference().child("fruits");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.v("tag", "Product Value" + snapshot);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        refProductList = dataSnapshot.getValue(ProductList.class);
                        productLists.add(refProductList);
                    }
                    Log.v("tag", "Product List" + productLists);
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                    ProductListAdapter productListAdapter = new ProductListAdapter(productLists, getApplicationContext());
                    recyclerView.setAdapter(productListAdapter);
                } else {
                    Log.v("tag", "Result" + "Some Thing went Wrong");
                    Toast.makeText(MainActivity.this, "Some Thing went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ic_customer_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, ProfilePageActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:

                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure? Logout")
                        .setConfirmText("Ok!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent logOutIntent = new Intent(MainActivity.this, MobileNumberGetActivity.class);
                                startActivity(logOutIntent);
                                MainActivity.this.finish();

                                UserDataSession userDataSession = new UserDataSession(getApplicationContext());
                                userDataSession.clear();

                                Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                finish();

                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
        ArrayList<ProductList> mProductList = new ArrayList<>();
        private Context context;

        public ProductListAdapter(ArrayList<ProductList> mProductList, Context context) {
            this.mProductList = mProductList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.ic_product_item_card_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mProductList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private CheckBox checkBox;
            private TextView productName, productPrice;
            private ImageView productImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.checkbox);
                productName = itemView.findViewById(R.id.productName);
                productPrice = itemView.findViewById(R.id.productPrice);
                productImage = itemView.findViewById(R.id.productImage);

            }
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ProductList list = mProductList.get(position);

            holder.productName.setText(list.getName());
            holder.productPrice.setText("₹ " + list.getPrice());

            String imageUrl = list.getImage();
            Picasso.get().load(imageUrl).into(holder.productImage);

            holder.itemView.setOnLongClickListener(v -> {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(true);
                return false;
            });
        }
    }


    public void onUserDataFetching(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("users").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserEntityData userEntityData = snapshot.getValue(UserEntityData.class);
                String name = userEntityData.getName();
                String email = userEntityData.getEmailID();
                String imageURI = userEntityData.getImage();
                String mobileNumber = userEntityData.getMobile();

                UserDataSession userDataSession = new UserDataSession(getApplicationContext());
                userDataSession.setUserValue(name, email, imageURI, mobileNumber);

                Log.v("tag", "User Data " + name + "\t" + email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}