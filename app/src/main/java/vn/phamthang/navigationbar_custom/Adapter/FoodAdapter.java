package vn.phamthang.navigationbar_custom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.phamthang.navigationbar_custom.Interface.iClickItemFood;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.phamthang.navigationbar_custom.Model.Food;
import vn.phamthang.navigationbar_custom.R;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>  {

    private Context mcontext;
    private List<Food> mfoodList;
    private iClickItemFood iClickItemFood;
    public List<Food> getMfoodList() {
        return mfoodList;
    }

    public void setMfoodList(List<Food> mfoodList) {
        this.mfoodList = mfoodList;
    }
    public FoodAdapter(Context mcontext, List<Food> mfoodList, iClickItemFood iClickItemFood) {
        this.mcontext = mcontext;
        this.mfoodList = mfoodList;
        this.iClickItemFood = iClickItemFood;
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_layout,parent,false);
        return  new FoodViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food foods = mfoodList.get(position);
        if(foods == null){
            return;
        }else {
            holder.tvName.setText(foods.getName());
            holder.tvStatus.setText(foods.getStatus());
            holder.tvPrice.setText(String.format("%,.2f vnđ",foods.getPrice()));
            Glide.with(mcontext).load(foods.getImageUrl())
                    .into(holder.imageView);
            if(foods.isFav() == true ){
                holder.btFav.setBackgroundResource(R.drawable.ic_favourite_pink);
            }else {
                holder.btFav.setBackgroundResource(R.drawable.ic_favourite_black);
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItemFood.onClickItemFood(foods);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        if(mfoodList != null){
            return mfoodList.size();
        }
        return 0;
    }
    public class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,btFav ;
        TextView tvName, tvStatus, tvPrice;
        CardView cardView;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvStatus = itemView.findViewById(R.id.tvFoodStatus);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
            imageView = itemView.findViewById(R.id.imgUserImg);
            btFav = itemView.findViewById(R.id.btFav);
            cardView =itemView.findViewById(R.id.cardViewFoodLayout);
        }

    }

}
