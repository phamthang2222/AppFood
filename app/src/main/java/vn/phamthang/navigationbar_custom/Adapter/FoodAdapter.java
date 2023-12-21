package vn.phamthang.navigationbar_custom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.phamthang.navigationbar_custom.Model.Food;
import vn.phamthang.navigationbar_custom.R;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> implements Filterable {

    Context mcontext;
    List<Food> mfoodList;
    List<Food> mfoodListOld;
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_layout,parent,false);
        return  new FoodViewHolder(view);
    }

    public List<Food> getMfoodList() {
        return mfoodList;
    }

    public void setMfoodList(List<Food> mfoodList) {
        this.mfoodList = mfoodList;
    }

    public FoodAdapter(Context mcontext, List<Food> mfoodList) {
        this.mcontext = mcontext;
        this.mfoodList = mfoodList;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food foods = mfoodList.get(position);
        if(foods == null){
            return;
        }else {
            holder.tvName.setText(foods.getName());
            holder.tvQuantity.setText("Số lượng: "+String.valueOf(foods.getQuantity()));
            holder.tvPrice.setText(String.format("%.2f vnđ",foods.getPrice()));
            Glide.with(mcontext).load(foods.getImageUrl())
                    .into(holder.imageView);

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
        ImageView imageView;
        TextView tvName, tvQuantity, tvPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvQuantity = itemView.findViewById(R.id.tvFoodQuantity);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
            imageView = itemView.findViewById(R.id.imgUserImg);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    mfoodList= mfoodListOld;
                }else {
                    List<Food> listFillter = new ArrayList<>();
                    for(Food values: listFillter){
                        if(values.getName().toLowerCase().contains(strSearch.toLowerCase())){ // tên food có chưa kí tự của tên tìm kiềm
                            // thì add vào listFillter
                            listFillter.add(values);
                        }
                    }
                    mfoodList = listFillter;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mfoodList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mfoodList = (List<Food>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
