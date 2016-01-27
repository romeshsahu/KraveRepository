package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import com.ps.models.UserDTO;

public class RecentlySearchedHistory {
	 
		private static RecentlySearchedHistory uniqInstance; 
		
	    public String RecentlySearchedCity;
	    public String isOnline;
	    public boolean isRightMenuActive;
	    public boolean isAllCitySearched;
	    public boolean isConvoHistoryLoaded;
	    public boolean isGetFriendsLoaded;
	    public boolean isFirstload;
	    public boolean isInRightMenu;
	    
	    public List<UserDTO> searchDudeList = new ArrayList<UserDTO>();
	    
		private RecentlySearchedHistory(){
			
			RecentlySearchedCity = "";
			isOnline = "no";
			isRightMenuActive = false;
			isAllCitySearched = false;
			isConvoHistoryLoaded = false;
			isGetFriendsLoaded = false;
			isFirstload = true;
			isInRightMenu = false;
			
			searchDudeList = new ArrayList<UserDTO>();
		}
		
		public static synchronized RecentlySearchedHistory getInstance(){
			if(uniqInstance == null){
			uniqInstance = new RecentlySearchedHistory();
			}
			
			return uniqInstance;
		
		}
		public static synchronized void setInstance(){
			
			uniqInstance = null;
			
		
		}
}