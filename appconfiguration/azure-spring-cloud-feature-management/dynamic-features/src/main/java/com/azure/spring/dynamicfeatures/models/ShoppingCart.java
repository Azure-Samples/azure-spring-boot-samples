package com.azure.spring.dynamicfeatures.models;

public class ShoppingCart {
    private Integer size;

    private String color;

    public Integer getSize() {
        return size;
    }

    public ShoppingCart setSize(Integer size) {
        this.size = size;
        return this;
    }

    public String getColor() {
        return color;
    }

    public ShoppingCart setColor(String color) {
        this.color = color;
        return this;
    }

    @Override
    public String toString() {
        return "Shopping Cart: Size " + size + " Color " + color;
    }

}
