package com.fitticket.buypage.pojos;

import java.util.ArrayList;

/**
 * Created by SanaKazi on 11/18/2016.
 */
public class GetProductDescriptionResponse {

    GetBuyPageProductDescriptionByIdResultClass GetBuyPageProductDescriptionByIdResult ;

    public GetBuyPageProductDescriptionByIdResultClass getGetBuyPageProductDescriptionByIdResult() {
        return GetBuyPageProductDescriptionByIdResult;
    }

    public void setGetBuyPageProductDescriptionByIdResult(GetBuyPageProductDescriptionByIdResultClass getBuyPageProductDescriptionByIdResult) {
        GetBuyPageProductDescriptionByIdResult = getBuyPageProductDescriptionByIdResult;
    }

    public class GetBuyPageProductDescriptionByIdResultClass {

        private String ProductName,ProductOverView,ShortDescription,StatusMsg;
        private int CategoryId,ProductId,StatusCode;
        private ArrayList<String> ProductImages;
        private ArrayList<ProductPricing> ProductPricing;

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getProductOverView() {
            return ProductOverView;
        }

        public void setProductOverView(String productOverView) {
            ProductOverView = productOverView;
        }

        public String getShortDescription() {
            return ShortDescription;
        }

        public void setShortDescription(String shortDescription) {
            ShortDescription = shortDescription;
        }

        public String getStatusMsg() {
            return StatusMsg;
        }

        public void setStatusMsg(String statusMsg) {
            StatusMsg = statusMsg;
        }

        public int getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(int categoryId) {
            CategoryId = categoryId;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public int getStatusCode() {
            return StatusCode;
        }

        public void setStatusCode(int statusCode) {
            StatusCode = statusCode;
        }

        public ArrayList<String> getProductImages() {
            return ProductImages;
        }

        public void setProductImages(ArrayList<String> productImages) {
            ProductImages = productImages;
        }

        public ArrayList<GetProductDescriptionResponse.ProductPricing> getProductPricing() {
            return ProductPricing;
        }

        public void setProductPricing(ArrayList<GetProductDescriptionResponse.ProductPricing> productPricing) {
            ProductPricing = productPricing;
        }
    }

    public class ProductPricing{
        private int Price,ProductPricingID;
        private String UnitName;


        public int getPrice() {
            return Price;
        }

        public void setPrice(int price) {
            Price = price;
        }

        public int getProductPricingID() {
            return ProductPricingID;
        }

        public void setProductPricingID(int productPricingID) {
            ProductPricingID = productPricingID;
        }

        public String getUnitName() {
            return UnitName;
        }

        public void setUnitName(String unitName) {
            UnitName = unitName;
        }
    }
}
