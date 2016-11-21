package com.fitticket.buypage.pojos;

import java.util.ArrayList;


public class BuyPageCategoryJsonResponse {
    GetBuyPageCategoryResultClass GetBuyPageCategoryResult;

    public GetBuyPageCategoryResultClass getGetBuyPageCategoryResult() {
        return GetBuyPageCategoryResult;
    }

    public void setGetBuyPageCategoryResult(GetBuyPageCategoryResultClass GetBuyPageCategoryResult) {
        this.GetBuyPageCategoryResult = GetBuyPageCategoryResult;
    }



    public class GetBuyPageCategoryResultClass
    {
        ArrayList<BuyPageCategoryJson> categories;

        public ArrayList<BuyPageCategoryJson> getCategories() {
            return categories;
        }

        public void setCategories(ArrayList<BuyPageCategoryJson> categories) {
            this.categories = categories;
        }
    }

    public class BuyPageCategoryJson{

        private String id,categoryName, status,shortDescription,backgroundImage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}

