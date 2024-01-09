package vn.phamthang.navigationbar_custom.Helper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import vn.phamthang.navigationbar_custom.Model.Food;

public class UpdateItemInRealtime {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("ListFood");

    public interface OnUpdateItemFavListener {
        void onUpdateItemFavSuccess(String childKey);
        void onUpdateItemFavError(String errorMessage);
    }

    public void updateIsFav(String childKey, OnUpdateItemFavListener listener) {
        // Kiểm tra childKey có tồn tại trong danh sách "ListFood" không
        myRef.child(childKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Cập nhật trường "fav" của mục cụ thể thành true
                    myRef.child(childKey).child("fav").setValue(true)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    listener.onUpdateItemFavSuccess(childKey);
                                } else {
                                    listener.onUpdateItemFavError("Cập nhật thất bại.");
                                }
                            });
                } else {
                    listener.onUpdateItemFavError("Mục không tồn tại trong Realtime Database.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onUpdateItemFavError(databaseError.getMessage());
            }
        });
    }
}
