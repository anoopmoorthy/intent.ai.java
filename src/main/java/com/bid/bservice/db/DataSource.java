package com.bid.bservice.db;

import java.text.DecimalFormat;
import java.util.Random;

import com.bid.bservice.entity.AdCampaign.Targeting;
import com.bid.bservice.entity.AdCampaign.Targeting.DeviceType;
import com.bid.bservice.entity.BidRequest.Device.Geo;
import com.bid.bservice.entity.BidRequest.Imp;
import com.bid.bservice.entity.BidRequest.Imp.Banner;
import com.bid.bservice.entity.BidRequest.Imp.Ext;
import com.bid.bservice.entity.BidRequest.Imp.Ext.Intent;

public class DataSource {

	private static Random random = new Random();
	
	public static Banner banner[] = { new Banner(320, 240), new Banner(320, 480), new Banner(300, 600),
			new Banner(300, 250) };

	public static String ua[] = {
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Safari/605.1.15",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36" };

	public static String[] campaignNames = { "Summer Sale 2024", "Back-to-School Deals", "Winter Clearance", "Holiday Specials",
			"New Year's Discounts", "Spring Collection Launch", "Tech Gadgets Showcase", "Fitness Challenge Promo",
			"Travel Destination Offers", "Food Festival Discounts" };

	public static String os[] = { "Windows", "Mac OS X" };

    public static int between(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    public static int[] placementIds() {
    	return new int[] { between(1, 2) };
    }
    public static Imp imp() {
    	return new Imp("", banner(),new Ext(intent()));
    }
    public static Targeting targeting() {
    	return new Targeting(geo(), DataSource.deviceType());
    }
    public static DeviceType deviceType() {
    	return new DeviceType(between(1, 2), os());
    }
    public static Geo geo() {
    	return new Geo(between(1, 99), between(1, 99), "US", "CA", "SF", "San Francisco", "94107");
    }
    public static Double budget() {
    	return Double.parseDouble(new DecimalFormat("#.##").format(0.50 + (0.80 - 0.50) * random.nextDouble()));
    }
    public static Intent intent() {
    	return new Intent(Integer.toString(between(1, 2)));
    }
    public static Banner banner() {
    	return banner[between(0, banner.length - 1)];
    }
    public static String campaignName() {
    	return campaignNames[between(0, campaignNames.length - 1)];
    }
    public static String id() {
    	return Integer.toString(between(1, 10));
    }
    public static String os() {
    	return os[between(0, os.length - 1)];
    }
	public static String iab() {

	        Integer ofCodes[][] = {new Integer[] {}, 
	        		new Integer[] {1, 7},
	        		new Integer[] {1, 20},
	        		new Integer[] {1, 12},
	        		new Integer[] {0, 11},
	        		new Integer[] {1, 15},
	        		new Integer[] {0, 9},
	        		new Integer[] {0, 15},
	        		new Integer[] {0, 18},
	        		new Integer[] {0, 31},
	        };
	        
	        int iab = between(1,9);
	        int code = between(ofCodes[iab][0],ofCodes[iab][1]);
	        
	        return "IAB" + iab + ((code > 0) ? ("-" + code) : "");
	    }
	
}
