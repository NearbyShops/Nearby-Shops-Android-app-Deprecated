package org.nearbyshops.enduserappnew.SelectMarket.Interfaces;

import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;


public interface listItemMarketNotifications
{
    void listItemClick(ServiceConfigurationGlobal configurationGlobal,int position);
    void selectMarketSuccessful(ServiceConfigurationGlobal configurationGlobal, int position);
    void showMessage(String message);
}