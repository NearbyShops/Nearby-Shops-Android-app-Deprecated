package org.nearbyshops.enduserapp.ModelServiceConfig.Endpoints;

import org.nearbyshops.enduserapp.ModelServiceConfig.ServiceConfigurationGlobal;

import java.util.ArrayList;

/**
 * Created by sumeet on 30/6/16.
 */
public class ServiceConfigurationEndPoint {

    int itemCount;
    int offset;
    int limit;
    int max_limit;
    ArrayList<ServiceConfigurationGlobal> results;

    public ArrayList<ServiceConfigurationGlobal> getResults() {
        return results;
    }

    public void setResults(ArrayList<ServiceConfigurationGlobal> results) {
        this.results = results;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(Integer max_limit) {
        this.max_limit = max_limit;
    }
}
