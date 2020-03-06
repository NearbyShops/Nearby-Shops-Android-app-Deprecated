package org.nearbyshops.enduserappnew.zHighlightSlider.Model;

import java.util.ArrayList;
import java.util.List;


public class Highlights {



    // Zero Commission | Lowest Price | Flexible Fares - No Package Restrictions | Multiple Destinations | Share Cab With Friends |

    //

    private List<Object> highlightList;

    private String listTitle;





    public static Highlights getHighlightsCabRental()
    {

        List<Object> list = new ArrayList<>();


        list.add(HighlightItem.getNegotiatePrices());
        list.add(HighlightItem.getBookCabsAtZeroCommission());
        list.add(HighlightItem.getBookingHelpline());
//        list.add(HighlightItem.getLowestFareGuarantee());
        list.add(HighlightItem.getFlexiFares());
        list.add(HighlightItem.getMultipleDestinations());
        list.add(HighlightItem.getShareCabWithFriends());


        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);

        highlights.setListTitle(null);


        return highlights;
    }








    public static Highlights getHighlightsLocalCabs()
    {

        List<Object> list = new ArrayList<>();

        list.add(HighlightItem.getNoSurgePricing());
        list.add(HighlightItem.getFlexiFares());
        list.add(HighlightItem.getMultipleDestinations());
        list.add(HighlightItem.getShareCabWithFriends());


        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);

        highlights.setListTitle("");


        return highlights;
    }







    public static Highlights getHighlightsOutstation()
    {

        List<Object> list = new ArrayList<>();


        list.add(HighlightItem.getNegotiatePrices());
        list.add(HighlightItem.getBookCabsAtZeroCommission());
        list.add(HighlightItem.getBookingHelpline());
//        list.add(HighlightItem.getLowestFareGuarantee());
        list.add(HighlightItem.getOneWayAndReturnCabsAvailable());
        list.add(HighlightItem.getFlexiFares());
        list.add(HighlightItem.getMultipleDestinations());
        list.add(HighlightItem.getShareCabWithFriends());


        Highlights highlights = new Highlights();
        highlights.setHighlightList(list);

        highlights.setListTitle("");


        return highlights;
    }






    // getter and setters


    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public List<Object> getHighlightList() {
        return highlightList;
    }

    public void setHighlightList(List<Object> highlightList) {
        this.highlightList = highlightList;
    }
}
