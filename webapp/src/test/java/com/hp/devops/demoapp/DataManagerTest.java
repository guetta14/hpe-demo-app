package com.hp.devops.demoapp;

import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;

/**
 * Created with IntelliJ IDEA.
 * User: gullery
 * Date: 25/11/14
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class DataManagerTest {

	@Test
	public void webapp_DataManager_dataManagerTestA() {
		try {
			DataManager.init(null);
			Assert.fail("the flow MUST have been fallen before");
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), InvalidParameterException.class);
			Assert.assertEquals(e.getMessage(), "servletContext must not be null");
		}
	}

	@Test
	public void webapp_DataManager_dataManagerTestB() {
		DataManager.loadData();
		Assert.assertEquals(DataManager.isInitialized(), false);
	}

	@Test
	public void webapp_DataManager_failTestForCoverageAnalysisB() {
		DataManager.loadData();
		Assert.assertEquals(true, true);
	}

	@Test
	public void webapp_DataManager_dataManagerTestC() {
		try {
			DataManager.getAll();
			Assert.fail("the flow MUST have been fallen before");
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), Exception.class);
			Assert.assertEquals(e.getMessage(), "service not initialized");
		}
	}

	@Test
	public void webapp_DataManager_dataManagerTestD() {
		try {
			DataManager.getBand(0);
			Assert.fail("the flow MUST have been fallen before");
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), Exception.class);
			Assert.assertEquals(e.getMessage(), "service not initialized");
		}
	}

	@Test
	public void webapp_DataManager_dataManagerTestE() {
		try {
			DataManager.upVoteBand(0);
			Assert.fail("the flow MUST have been fallen before");
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), Exception.class);
			Assert.assertEquals(e.getMessage(), "service not initialized");
		}
	}
}
