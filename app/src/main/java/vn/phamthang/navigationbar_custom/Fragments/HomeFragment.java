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
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
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
//    private List<Food> getFoodList(){
//        List<Food> list = new ArrayList<>();
//
//        list.add(new Food("https://daynauan.info.vn/wp-content/uploads/2019/05/suon-non-kho-nuoc-dua.jpg",
//                "Sườn xào ",30,265_000));
//        list.add(new Food("https://cdn-i.vtcnews.vn/resize/th/upload/2023/09/18/27706f19b4ba94f6f2a9a9a18dc02033-23522159.jpg",
//                "Thịt gà",50,210_000));
//        list.add(new Food("https://daotaobeptruong.vn/wp-content/uploads/2019/10/chuoi-boc-nep-mon-an-vat-ban-online.jpg",
//                "Xôi",4,50_000));
//        list.add(new Food("https://saodieu.vn/media/Bai%20Viet%20-%20T62016/Saodieu%20-%2010%20mon%20an%203.jpg",
//                "Nem rán",10,110_000));
//        list.add(new Food("https://i2.ex-cdn.com/nongnghiepso.nongnghiep.vn/files/guest/21/2021/12/30/21--zdhcosz-104331.jpg",
//                "Thịt gà đen",1,330_000));
//        list.add(new Food("https://i-giadinh.vnecdn.net/2021/10/11/Longnonxaoduachuaancungcom-163-2034-2534-1633939319.jpg",
//                "Lòng xào dưa",5,70_000));
//        return  list;
//    }
//    private void pushDataToFirebase(List<Food> foodList) {
//        DatabaseReference listFoodRef = myRef.child("ListFood");
//        // Đặt giá trị cho child "ListFood" bằng danh sách foodList
//        listFoodRef.setValue(foodList)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        // Nếu lưu dữ liệu thành công
//                    }
//                } )
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//    }
//    private List<Food> fetchListFoodFromRealtimeDataBase(){
//        final List<Food> list = new ArrayList<>();
//        DatabaseReference listFoodRef = myRef.child("ListFood");
//        listFoodRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                // Lặp qua từng phần tử trong dataSnapshot
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    // Chuyển đổi dataSnapshot thành đối tượng Food
//                    Food food = postSnapshot.getValue(Food.class);
//                    if (food != null) {
//                        // Thêm food vào danh sách
//                        list.add(food);
//                    }
//                }
//                updateAdapterWithData(list);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        return list;
//    }
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

            }
        });
        return list;
    }
    private void onClickGoToFoodDetail(Food food){
        Intent intent = new Intent(getContext(), DetailFoodActivity.class);
        intent.putExtra("FoodName",food.getName());
        intent.putExtra("FoodQuantity",food.getQuantity());
        intent.putExtra("FoodPrice",food.getPrice());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

}