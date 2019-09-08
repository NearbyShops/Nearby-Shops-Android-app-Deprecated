package org.nearbyshops.enduserappnew.ViewHolderCommon.Models;


import org.nearbyshops.enduserappnew.R;



public class EmptyScreenData {


    private String message;
    private String buttonText;
    private String urlForButtonClick;
    private int imageResource;


    public static EmptyScreenData getCreateMarketData()
    {
        EmptyScreenData data = new EmptyScreenData();
        data.setMessage("Create your own Market ... help local Economy ... !");
        data.setButtonText("Create Market");
        data.setUrlForButtonClick("https://nearbyshops.org");

        return data;
    }



    public static EmptyScreenData createMarketNoMarketsAvailable()
    {
        EmptyScreenData data = new EmptyScreenData();
        data.setMessage("No markets available in your area. \n\nYou can create your own local market and Help Local Vendors and Local Economy");
        data.setButtonText("Create Market");
        data.setUrlForButtonClick("https://nearbyshops.org");

        data.setImageResource(R.drawable.ic_local_florist_black_24dp);

        return data;
    }


    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getUrlForButtonClick() {
        return urlForButtonClick;
    }

    public void setUrlForButtonClick(String urlForButtonClick) {
        this.urlForButtonClick = urlForButtonClick;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
