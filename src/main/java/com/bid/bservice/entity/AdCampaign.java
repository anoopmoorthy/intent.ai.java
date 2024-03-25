package com.bid.bservice.entity;

import java.util.List;

import com.bid.bservice.entity.BidRequest.Device.Geo;
import com.bid.bservice.entity.BidRequest.Imp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdCampaign {
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private double budget;
    private String status;
    private Imp imp;
    private int[] placementIds;
    private String[] iab;
    private Targeting targeting;
    private List<Creative> creatives;
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Targeting {
        private Geo geo;
        private DeviceType deviceType;
        
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class DeviceType {
            private Integer devicetype;
            private String os;
        }
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Creative {
        private String id;
        private String type;
        private String url;
    }
}