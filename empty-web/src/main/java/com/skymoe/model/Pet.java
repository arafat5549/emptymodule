package com.skymoe.model;

/**
 * Created by Administrator on 2017/7/25.
 */
public class Pet {
    private Integer id;
    private String name;
    private Integer masterId;
    private Integer categoryId;
    private String status;

    private Category category;
    private User master;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", masterId=" + masterId +
                ", categoryId=" + categoryId +
                ", status='" + status + '\'' +
                ", category=" + category +
                ", master=" + master +
                '}';
    }
}
