package com.kioferta;

public class Upload {
    private String Name;
    private String ImageUrl;

    public Upload(){

    }

    public  Upload (String name, String imageUrl){
        if(name.trim().equals("")){
            name = "Sem Nome";
        }
        Name = name;
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String mName) {
        this.Name = mName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.ImageUrl = mImageUrl;
    }
}
