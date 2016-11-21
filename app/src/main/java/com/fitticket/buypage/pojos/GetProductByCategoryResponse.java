package com.fitticket.buypage.pojos;

import java.util.ArrayList;

/**
 * Created by SanaKazi on 11/18/2016.
 */
public class GetProductByCategoryResponse {
    GetBuyPageProductByCategoryResultClass GetBuyPageProductByCategoryResult;

    public GetBuyPageProductByCategoryResultClass getGetBuyPageProductByCategoryResult() {
        return GetBuyPageProductByCategoryResult;
    }

    public void setGetBuyPageProductByCategoryResult(GetBuyPageProductByCategoryResultClass getBuyPageProductByCategoryResult) {
        GetBuyPageProductByCategoryResult = getBuyPageProductByCategoryResult;
    }

    public class GetBuyPageProductByCategoryResultClass
    {
    ArrayList<Products> products;

        public ArrayList<Products> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<Products> products) {
            this.products = products;
        }
    }

    public class Products{

        int categoryId,id,status,unitId;
        String productImage,productName,shortDescription,unitName,unitPrice;

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUnitId() {
            return unitId;
        }

        public void setUnitId(int unitId) {
            this.unitId = unitId;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
