package vn.phamthang.navigationbar_custom.Helper;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.phamthang.navigationbar_custom.Model.Food;

public class FetchListFoodFromFireBase {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("ListFood");

    public interface OnFoodDataFetchedListener {
        void onDataFetched(List<Food> foodList);
        void onError(String errorMessage);
    }

    public void fetchFoods(OnFoodDataFetchedListener listener) {
        final List<Food> list = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    if (food != null) {
                        list.add(food);
                    }
                }
                if (listener != null) {
                    listener.onDataFetched(list);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener != null) {
                    listener.onError(error.getMessage());
                }
            }
        });
    }
}
