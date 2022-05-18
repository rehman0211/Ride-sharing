package org.marehman.rideSharing.utils;

import java.util.List;

public class FilterRequest {
    private String customerId;
    private String driverId;
    private List<Filter> filters;
    private String condition="AND";

    public FilterRequest() {
    }

    public FilterRequest(String customerId, String driverId, List<Filter> filters) {
        this.customerId = customerId;
        this.driverId = driverId;
        this.filters = filters;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }



    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
