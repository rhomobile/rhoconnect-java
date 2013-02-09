package com.msi.rhoconnect.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ClientResourceTest.class, PluginResourceTest.class,
		SourceResourceTest.class, StoreResourceTest.class,
		SystemResourceTest.class, UserResourceTest.class })
public class AllTests {

}
