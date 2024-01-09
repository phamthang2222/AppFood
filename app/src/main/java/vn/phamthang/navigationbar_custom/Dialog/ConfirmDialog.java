package vn.phamthang.navigationbar_custom.Dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import vn.phamthang.navigationbar_custom.Activity.ProfileActivity;
import vn.phamthang.navigationbar_custom.Activity.MainActivity;

public class ConfirmDialog {
    private Context context;

    public ConfirmDialog(Context context) {
        this.context = context;
    }
    public void showConfirmationDialog(MainActivity context1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context1);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn muốn thoát ứng dụng?");

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context1.finish();
            }
        });

        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showConfirmationDialogAtProfileActivity(ProfileActivity context1) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context1);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn chưa cập nhật thông tin!");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                context1.finish();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
