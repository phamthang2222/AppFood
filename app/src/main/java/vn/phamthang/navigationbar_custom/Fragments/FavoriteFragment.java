package vn.phamthang.navigationbar_custom.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

import vn.phamthang.navigationbar_custom.Activity.DetailFoodActivity;
import vn.phamthang.navigationbar_custom.Adapter.FoodAdapter;
import vn.phamthang.navigationbar_custom.Helper.FetchListFoodFromFireBase;
import vn.phamthang.navigationbar_custom.Interface.iClickItemFood;
import vn.phamthang.navigationbar_custom.Model.Food;
import vn.phamthang.navigationbar_custom.R;


public class FavoriteFragment extends Fragment {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("ListFood");
    RecyclerView mRecyclerViewFav;
    FoodAdapter mFoodAdapter;
    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mRecyclerViewFav = view.findViewById(R.id.recycleViewListFavFoods);
        displayFood();
//        pushDataToFirebase(getFoodList());
        fetchListFoodFromRealtimeDataBase();
        return view;
    }
    private void displayFood() {
        mFoodAdapter = new FoodAdapter(getContext(), new ArrayList<>(), new iClickItemFood() {
            @Override
            public void onClickItemFood(Food food) {
                onClickGoToFoodDetail(food);
            }
        }); // Khởi tạo adapter với danh sách rỗng
        GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(), 1);
        mRecyclerViewFav.setLayoutManager(gridLayoutManager);
//        đổ dữ liệu vào adapter và set vào recyclerView
        mRecyclerViewFav.setAdapter(mFoodAdapter);
    }
    private void updateAdapterWithData(List<Food> foods) {
        mFoodAdapter.setMfoodList(foods);
        mFoodAdapter.notifyDataSetChanged();
    }
    private List<Food> fetchListFoodFromRealtimeDataBase(){
        List<Food> list = new ArrayList<>();
        FetchListFoodFromFireBase fetchListFoodFromFireBase = new FetchListFoodFromFireBase();
        fetchListFoodFromFireBase.fetchFoods(new FetchListFoodFromFireBase.OnFoodDataFetchedListener() {
            @Override
            public void onDataFetched(List<Food> foodList) {
                for(Food food : foodList){
                    if(food.isFav()){
                        list.add(food);
                    }
                }
                updateAdapterWithData(list);
            }
            @Override
            public void onError(String errorMessage) {
                Log.d("TAG","Lỗi lấy data");
            }
        });

        return list;
    }

    private void onClickGoToFoodDetail(Food food){
        Intent intent = new Intent(getContext(), DetailFoodActivity.class);
        intent.putExtra("FoodUrlImg",food.getImageUrl());
        intent.putExtra("FoodName",food.getName());
        intent.putExtra("FoodStatus",food.getStatus());
        intent.putExtra("FoodPrice",food.getPrice());
        intent.putExtra("FoodDes",food.getDescription());
        intent.putExtra("FoodImgDetail",food.getListImgDetail());
        intent.putStringArrayListExtra("FoodListDetailImg", food.getListImgDetail());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }
    private String getIdUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        return uid;
    }

}