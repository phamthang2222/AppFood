package vn.phamthang.navigationbar_custom.Model;

public class Food {
    private String imageUrl;
    private String name;
    private int quantity;
    private double price;
    private boolean isFav;

    public Food(String imageUrl, String name, int quantity, double price,boolean isFav) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.isFav = isFav;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public Food() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
