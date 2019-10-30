package com.desktopip.exploriztic.tootanium.models;

import android.widget.Toast;

public class NewProfile {

//    private final String username;

    private final String fullName;

    private final String email;

    private final String position;

    private final String imageUrl;

//    public String getUsername() {
//        return username;
//    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    private NewProfile(String fullName, String email, String position, String imageUrl) {
        this.fullName = fullName;
        this.email = email;
        this.position = position;
        this.imageUrl = imageUrl;
    }

    public static Build build(){
        return new Build();
    }

    public static class Build{
        private String username = null;
        private String fullname = null;
        private String email = null;
        private String position = null;
        private String imageUrl = null;

        public Build fullName(String newFullName){
            this.fullname = newFullName;
            return this;
        }

        public Build email(String newEmail){
            this.email = newEmail;
            return this;
        }

        public Build position(String newPosition){
            this.position = newPosition;
            return this;
        }

        public Build imageUrl(String newImageUrl){
            this.imageUrl = newImageUrl;
            return this;
        }

        public NewProfile create(){

            if(fullname == null){
                throw new IllegalStateException("User Fullname Empty");
            }

            if(email == null){
                throw new IllegalStateException("User Email Empty");
            }

            if(position == null){
                throw new IllegalStateException("User Position Empty");
            }

            if(imageUrl == null){
                throw new IllegalStateException("User Image URL Empty");
            }

            return new NewProfile(fullname,email,position,imageUrl);
        }
    }

}
