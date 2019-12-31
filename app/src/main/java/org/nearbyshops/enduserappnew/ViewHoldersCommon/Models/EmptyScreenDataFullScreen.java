package org.nearbyshops.enduserappnew.ViewHoldersCommon.Models;


import org.nearbyshops.enduserappnew.R;

public class EmptyScreenDataFullScreen {


    private String title;
    private String message;
    private int drawableResource;
    private boolean showDesignedByFreepik;





    public static EmptyScreenDataFullScreen getOffline()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You are offline");
        data.setShowDesignedByFreepik(false);
        data.setMessage("No internet ... Please check your internet connection and refresh again !");
        data.setDrawableResource(R.drawable.ic_receipt_color);

        return data;
    }




    public static EmptyScreenDataFullScreen getErrorTemplate(int errorCode)
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("There is an Error !!");
        data.setShowDesignedByFreepik(false);
        data.setMessage("There is an Error ... when asking for help please tell the error code as " + errorCode );
        data.setDrawableResource(R.drawable.ic_close_black_24dp);

        return data;
    }




    public static EmptyScreenDataFullScreen noItemsAndCategories()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Items and Categories");
        data.setShowDesignedByFreepik(false);
        data.setMessage("No items in this category");
        data.setDrawableResource(R.drawable.ic_clear_black_24dp);

        return data;
    }






    public static EmptyScreenDataFullScreen emptyScreenOrders()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Orders to Show !");
        data.setShowDesignedByFreepik(false);
        data.setMessage("You have not received any orders ... When you receive orders they will appear here !");
        data.setDrawableResource(R.drawable.ic_local_shipping_black_24px);

        return data;
    }






    public static EmptyScreenDataFullScreen emptyScreenQuickStockEditor()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You have not added any items !");
        data.setShowDesignedByFreepik(false);
        data.setMessage("You have not added any items to your shop ... when you add items to your shop they will appear here !");
        data.setDrawableResource(R.drawable.ic_items_24px);

        return data;
    }













    public static EmptyScreenDataFullScreen emptyScreenPFSINventory()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("No Orders Here !");
        data.setShowDesignedByFreepik(false);
        data.setMessage("Swipe right or left to see more orders !");
        data.setDrawableResource(R.drawable.ic_items_24px);

        return data;
    }









    public static EmptyScreenDataFullScreen emptyScreenStaffList()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You have not added any Staff Members !");
        data.setShowDesignedByFreepik(false);
        data.setMessage("To add staff members to your shop press the Plus Button ( + ) given below !");
        data.setDrawableResource(R.drawable.ic_account_box_black_24px);

        return data;
    }





    public static EmptyScreenDataFullScreen emptyScreenDeliveryStaff()
    {
        EmptyScreenDataFullScreen data = new EmptyScreenDataFullScreen();
        data.setTitle("You have not added any Delivery Staff !");
        data.setShowDesignedByFreepik(false);
        data.setMessage("To add delivery staff to your shop press the Plus Button ( + ) given below !");
        data.setDrawableResource(R.drawable.ic_person_pin_circle_black_24px);

        return data;
    }













    // getter and setter
    public boolean isShowDesignedByFreepik() {
        return showDesignedByFreepik;
    }

    public void setShowDesignedByFreepik(boolean showDesignedByFreepik) {
        this.showDesignedByFreepik = showDesignedByFreepik;
    }

    public int getDrawableResource() {
        return drawableResource;
    }

    public void setDrawableResource(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
