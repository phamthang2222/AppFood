package vn.phamthang.navigationbar_custom.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.phamthang.navigationbar_custom.Activity.DetailFoodActivity;
import vn.phamthang.navigationbar_custom.Adapter.FoodAdapter;
import vn.phamthang.navigationbar_custom.Helper.FetchListFoodFromFireBase;
import vn.phamthang.navigationbar_custom.Interface.iClickItemFood;
import vn.phamthang.navigationbar_custom.Model.Food;
import vn.phamthang.navigationbar_custom.R;


public class HomeFragment extends Fragment{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("ListFood");
    RecyclerView mRecyclerView;
    FoodAdapter mFoodAdapter;

    public FoodAdapter getmFoodAdapter() {
        return mFoodAdapter;
    }

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.recycleViewListFoods);
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
        mRecyclerView.setLayoutManager(gridLayoutManager);
//        đổ dữ liệu vào adapter và set vào recyclerView
        mRecyclerView.setAdapter(mFoodAdapter);
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
                list.addAll(foodList);
                updateAdapterWithData(list);
            }
            @Override
            public void onError(String errorMessage) {
                Log.d("TAG","Lỗi lấy data");
            }
        });
        return list;
    }
    private List<Food> getFoodList(){
        List<Food> list = new ArrayList<>();
        ArrayList<String> Listimages = new ArrayList<>();
        Listimages.add("https://static-images.vnncdn.net/files/publish/2023/9/9/cha-com-1-339.jpg");
        Listimages.add("https://songkhoe.medplus.vn/wp-content/uploads/2020/03/C%C3%A1ch-l%C3%A0m-ch%E1%BA%A3-c%E1%BB%91m.png");
        Listimages.add("https://dacsanngon3mien.net/wp-content/uploads/2022/12/cha-com-lam-mon-gi-ngon-12.jpg");
        list.add(new Food("https://cdn.tgdd.vn/2021/04/CookRecipe/Avatar/cha-com-chay-thumbnail-2.jpg","Chả cốm","Còn hàng",
                "chả cốm hà nội ngon ơi là ngon mọi người ghé mua",22000,Listimages,false,true));
        return  list;
    }
    private void pushDataToFirebase(List<Food> foodList) {
        DatabaseReference listFoodRef = myRef.child("ListFood");
        // Đặt giá trị cho child "ListFood" bằng danh sách foodList
        listFoodRef.setValue(foodList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "thành công", Toast.LENGTH_SHORT).show();
                    }
                } )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void onClickGoToFoodDetail(Food food){
        Intent intent = new Intent(getContext(), DetailFoodActivity.class);
        intent.putExtra("FoodUrlImg",food.getImageUrl());
        intent.putExtra("FoodName",food.getName());
        intent.putExtra("FoodStatus",food.getStatus());
        intent.putExtra("FoodPrice",food.getPrice());
        intent.putExtra("FoodDes",food.getDescription());
        intent.putExtra("FoodImgDetail",food.getListImgDetail());
        intent.putExtra("IsFoodFav",food.isFav());
        intent.putExtra("IsFoodSale",food.isSale());
        intent.putExtra("childKey",myRef.getKey());
        intent.putStringArrayListExtra("FoodListDetailImg", food.getListImgDetail());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }
}