package vn.phamthang.navigationbar_custom.Fragments;

import android.app.SearchManager;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import vn.phamthang.navigationbar_custom.Adapter.FoodAdapter;
import vn.phamthang.navigationbar_custom.Model.Food;
import vn.phamthang.navigationbar_custom.R;


public class HomeFragment extends Fragment {
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
        return view;
    }

    private void displayFood() {
        mFoodAdapter =new FoodAdapter(getContext(), getFoodList());
        GridLayoutManager gridLayoutManager =new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
//        đổ dữ liệu vào adapter và set vào recyclerView
        mFoodAdapter.setMfoodList(getFoodList());
        mRecyclerView.setAdapter(mFoodAdapter);
    }
    private List<Food> getFoodList(){
        List<Food> list = new ArrayList<>();

        list.add(new Food("https://daynauan.info.vn/wp-content/uploads/2019/05/suon-non-kho-nuoc-dua.jpg",
                "Sườn xào ",30,265_000));
        list.add(new Food("https://cdn-i.vtcnews.vn/resize/th/upload/2023/09/18/27706f19b4ba94f6f2a9a9a18dc02033-23522159.jpg",
                "Thịt gà",50,210_000));
        list.add(new Food("https://daotaobeptruong.vn/wp-content/uploads/2019/10/chuoi-boc-nep-mon-an-vat-ban-online.jpg",
                "Xôi",4,50_000));
        list.add(new Food("https://saodieu.vn/media/Bai%20Viet%20-%20T62016/Saodieu%20-%2010%20mon%20an%203.jpg",
                "Nem rán",10,110_000));
        list.add(new Food("https://i2.ex-cdn.com/nongnghiepso.nongnghiep.vn/files/guest/21/2021/12/30/21--zdhcosz-104331.jpg",
                "Thịt gà đen",1,330_000));
        list.add(new Food("https://i-giadinh.vnecdn.net/2021/10/11/Longnonxaoduachuaancungcom-163-2034-2534-1633939319.jpg",
                "Lòng xào dưa",5,70_000));
        return  list;
    }

}