package com.ps.utill;

import java.text.DecimalFormat;

import android.util.Log;

enum convert{
	convertInvalid,
    convertNone,
    convertWalking,
    convertBiking,
    convertDriving,
    convertPublic,
}

public class DistanceConverter {
	

	public String convertToString(double miles, convert convertTo){
		String string = "";
		
		double conversionValue = this.convertToTime(miles, convertTo);
		String suffix = this.getSuffixForConversion(convertTo, conversionValue);
		
		if(conversionValue < 1.0/60.0){
			conversionValue *= 3600;
		}else if (conversionValue < 1){
			conversionValue *= 60;
		}
		
		String time = new DecimalFormat("###0.00").format(conversionValue);

		string = time + " " + suffix;
		
		return string;
	}
	
	public double convertToTime(double miles, convert convertTo){
		double time = 0;

		time = (double)(miles / this.getDistanceTravelingBy(convertTo));
	
		return time;
	}
	
	public convert getConversion(int num){
		convert c = convert.convertNone;
		switch (num){
		case 2:
			c = convert.convertWalking;
			break;
		case 3:
			c = convert.convertBiking;
			break;
		case 4:
			c = convert.convertDriving;
			break;
		case 5:
			c = convert.convertPublic;
			break;
		case 0:
			c = convert.convertInvalid;
			break;
		}
		
		return c;
	}
	
	private double getDistanceTravelingBy(convert convertTo){
		double time = 0;
		switch(convertTo){
			case convertWalking:
				time = 3.1;
				break;
			case convertBiking:
				time = 12.0;
				break;
			case convertDriving:
				time = 55.0;
				break;
			case convertPublic:
				time = 35.0;
				break;
			case convertNone:
			case convertInvalid:
				time = 1.0;
				break;
		}
		return time;
	}
	
	private String getSuffixForConversion(convert convertTo, double hours){
		String suffix = "";
		
		if(hours < 1.0 / 60.0){
			if(hours / 3600.0 == 1){
				suffix = "second";
			}else{
				suffix = "seconds";
			}
		}else if(hours < 1.0){
			if((int)(hours / 60.0) == 1){
				suffix = "minute";
			}else{
				suffix = "minutes";
			}
		}else if((int)hours == 1){
			suffix = "hour";
		}else{
			suffix = "hours";
		}
		
		return suffix;
	}
	
}
