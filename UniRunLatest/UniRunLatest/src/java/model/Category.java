package model;

public class Category {
    private int categoryId;
    private String categoryName;
    private double distanceKm;
    private double fee;

    public Category() { }

    public Category(int categoryId, String categoryName, double distanceKm, double fee) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.distanceKm = distanceKm;
        this.fee = fee;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
}
