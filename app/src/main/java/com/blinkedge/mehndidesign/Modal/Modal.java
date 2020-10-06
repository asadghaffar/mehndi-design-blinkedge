package com.blinkedge.mehndidesign.Modal;

import com.blinkedge.mehndidesign.API.API;

public class Modal {

    String catName;
    String catImage;
    String allImages;
    int catId;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return API.BASE_URL_FOR_IMAGES + catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getAllImages() {
        return allImages;
    }

    public void setAllImages(String allImages) {
        this.allImages = API.BASE_URL_FOR_IMAGES + allImages;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }


}