package vn.phamthang.navigationbar_custom.Model;

import java.util.ArrayList;

public class FavFood {
    private String idUser;
    private String imageUrl;
    private String name;
    private String status;
    private String description;
    private double price;
    private ArrayList<String> listImgDetail;
    private boolean isFav;
    private boolean isSale;

    public FavFood(String idUser, String imageUrl, String name, String status, String description, double price, ArrayList<String> listImgDetail, boolean isFav, boolean isSale) {
        this.idUser = idUser;
        this.imageUrl = imageUrl;
        this.name = name;
        this.status = status;
        this.description = description;
        this.price = price;
        this.listImgDetail = listImgDetail;
        this.isFav = isFav;
        this.isSale = isSale;
    }
    public FavFood() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public boolean isSale() {
        return isSale;
    }

    public void setSale(boolean sale) {
        isSale = sale;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getListImgDetail() {
        return listImgDetail;
    }

    public void setListImgDetail(ArrayList<String> listImgDetail) {
        this.listImgDetail = listImgDetail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FavFood{" +
                "idUser='" + idUser + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", listImgDetail=" + listImgDetail +
                ", isFav=" + isFav +
                ", isSale=" + isSale +
                '}';
    }
}
