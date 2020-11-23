package com.example.foodorderclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mFoodList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFoodList=(RecyclerView)findViewById(R.id.foodList);
        mFoodList.setHasFixedSize(true);
        mFoodList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Item");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(mDatabase, Food.class)
                        .build();

        FirebaseRecyclerAdapter<Food,FoodViewHolder> FBRA=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.setName(model.getName());
                holder.setPrice(model.getPrice());
                holder.setDesc(model.getDesc());
                holder.setImage(getApplicationContext(),model.getImage());
            }

            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.singlemenuitem, parent, false);
                return new FoodViewHolder(view);
            }
        };
        mFoodList.setAdapter(FBRA);
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setName(String name){
            TextView food_name=(TextView) mView.findViewById(R.id.foodName);
            food_name.setText(name);
        }

        public void setDesc(String desc){
            TextView food_desc=(TextView) mView.findViewById(R.id.foodDesc);
            food_desc.setText(desc);
        }

        public void setPrice(String price){
            TextView food_price=(TextView) mView.findViewById(R.id.foodPrize);
            food_price.setText(price);
        }

        public void setImage(Context ctx,String image){
            ImageView food_image=(ImageView) mView.findViewById(R.id.foodImage);
            Picasso.get().load(image).into(food_image);

        }
    }
}
