package com.zendesk.search;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zendesk.fileservice.JsonFileService;

/***
 * test class to test methods in SearchHelper
 * @author raghu
 *
 */
public class SearchHelperTest {
	
	private final ByteArrayOutputStream sysout = new ByteArrayOutputStream();
	private final ByteArrayOutputStream sysErr = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(sysout));
	    System.setErr(new PrintStream(sysErr));
	}

	@After
	public void restoreStreams() {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test
	public void showMainMenuTest() throws IOException, Exception {
		JsonFileService.getDataFiles();
		String inputStr = "quit";
		InputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		SearchHelper.showMainMenu(br);
		String result = sysout.toString().trim();
		assertTrue(result.contains("enter 1 for organizations"));
		assertTrue(result.contains("enter 2 for tickets"));
		assertTrue(result.contains("enter 3 for users"));
		assertTrue(result.contains("enter quit to exit"));
	}
	
	@Test
	public void showSubMenuTest() throws IOException, Exception {
		JsonFileService.getDataFiles();
		String inputStr = "1\nname\nNutralab";
		InputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		SearchHelper.showMainMenu(br);
		String result = sysout.toString().trim();
		assertTrue(result.contains("here are of the search keys for organizations"));
		assertTrue(result.contains("enter a search key or 0 for main menu"));
		assertTrue(result.contains("[shared_tickets, name, created_at, external_id, details, _id, url, domain_names, tags]"));
		assertTrue(result.contains("enter search value"));
		assertTrue(result.contains("{\"shared_tickets\":false,\"name\":\"Nutralab\",\"created_at\":\"2016-04-07T08:21:44 -10:00\",\"external_id\":\"7cd6b8d4-2999-4ff2-8cfd-44d05b449226\",\"details\":\"Non profit\",\"_id\":102,\"url\":\"http:\\/\\/initech.zendesk.com\\/api\\/v2\\/organizations\\/102.json\",\"domain_names\":[\"trollery.com\",\"datagen.com\",\"bluegrain.com\",\"dadabase.com\"],\"tags\":[\"Cherry\",\"Collier\",\"Fuentes\",\"Trevino\"]}"));
	}
	
}
