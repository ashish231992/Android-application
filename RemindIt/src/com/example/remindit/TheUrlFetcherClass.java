package com.example.remindit;

import android.content.Context;

public class TheUrlFetcherClass {

	Context ourContext;
	UrlGenerator ug;
	public TheUrlFetcherClass(Context c){
		this.ourContext=c;
	}
	public void theFetcher(){
		
		
		Thread th=new Thread(){
			
			public void run(){
				try{
					
					/*************** Url Generator call **********************/
				ug=new UrlGenerator(ourContext);
				ug.giveURL();
				sleep(2000);
				
				}catch(Exception e){
					e.printStackTrace();
					}finally{
						theFetcher();
					}
			}
		};
		th.start();
		
	}
}
