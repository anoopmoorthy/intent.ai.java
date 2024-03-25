package com.bid.bservice.entity;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class BidRequest {
    private String id;
    private List<Imp> imp;
    private Device device;
    private User user;
    private int at;
    private List<String> bcat;
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Imp {
        private String id;
        private Banner banner;
        private Ext ext;
        
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Ext {
            private Intent intent;
            
            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            @ToString
            public static class Intent {
                @JsonProperty("placementId")
                private String placementId;
            }
        }
        
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Banner {
            private Integer w;
            private Integer h;
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Banner banner = (Banner) obj;
                return w.equals(banner.w) && h.equals(banner.h);
            }
            @Override
            public int hashCode() {
                return Objects.hash(w, h);
            }
        }
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Device {
        private String ua;
        private Geo geo;
        private String ip;
        private Integer devicetype;
        private String os;
        
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString
        public static class Geo {
            private Integer lat;
            private Integer lon;
            private String country;
            private String region;
            private String metro;
            private String city;
            private String zip;
        }
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class User {
        private String id;
    }

}