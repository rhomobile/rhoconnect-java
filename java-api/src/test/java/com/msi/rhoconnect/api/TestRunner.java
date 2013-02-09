package com.msi.rhoconnect.api;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

	public static void main(String[] args) {
//		Result result = JUnitCore.runClasses(
//				ClientResourceTest.class, PluginResourceTest.class,
//				SourceResourceTest.class, StoreResourceTest.class,
//				SystemResourceTest.class, UserResourceTest.class				
//				);		
//		System.out.println("...");
//	    for (Failure failure : result.getFailures()) {
//	      System.out.println(failure.toString());
//	    }
		
		JUnitCore junit = new JUnitCore();
	    junit.addListener(new TextListener(System.out));		

	    junit.run(PluginResourceTest.class);
	    junit.run(SourceResourceTest.class);
	    junit.run(StoreResourceTest.class);
	    junit.run(UserResourceTest.class);	    
	    junit.run(ClientResourceTest.class);
	    junit.run(SystemResourceTest.class);
	}
}
