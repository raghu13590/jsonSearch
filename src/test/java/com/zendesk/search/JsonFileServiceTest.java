package com.zendesk.search;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.zendesk.fileservice.JsonFileService;

/***
 * test class for methods in JsonFileService
 * @author raghu
 *
 */
public class JsonFileServiceTest {
	
	@Before
	public void emptyFileMap() {
		Map<String, DataFile> fileMap = new HashMap<>();
		JsonFileService.setFileMap(fileMap);
	}
		
	/***
	 * tests if app has 3 required data/json files
	 * @throws IOException
	 */
	@Test
	public void testGetDataFiles() throws IOException {
		JsonFileService.getDataFiles();
		assertEquals(3,JsonFileService.getFileMap().size());
	}
	
	/***
	 * tests for single result match
	 * @throws Exception
	 */
	@Test
	public void testFindInFileForSingleResult() throws Exception {
		JsonFileService.getDataFiles();
		DataFile dataFile = JsonFileService.getFileMap().get("1");
		JSONArray result = JsonFileService.findInFile("_id", "101", dataFile);
		assertEquals(result.size(), 1);
		JSONObject resultJson = (JSONObject) result.get(0);
		assertEquals("101", resultJson.get("_id").toString());
	}
	
	/***
	 * test for multiple results match
	 * @throws Exception
	 */
	@Test
	public void testFindInFileForMultipleResults() throws Exception {
		JsonFileService.getDataFiles();
		DataFile dataFile = JsonFileService.getFileMap().get("2");
		JSONArray result = JsonFileService.findInFile("has_incidents", "true", dataFile);
		assertEquals(result.size(), 99);
	}
	
	/***
	 * test for no/empty result
	 * @throws Exception
	 */
	@Test
	public void testFindInFileNoResults() throws Exception {
		JsonFileService.getDataFiles();
		DataFile dataFile = JsonFileService.getFileMap().get("3");
		JSONArray result = JsonFileService.findInFile("name", "abcd", dataFile);
		assertEquals(result.size(), 0);
	}
	
	/***
	 * test to list all first level keys in a json schema for a file
	 * @throws Exception
	 */
	@Test
	public void testListKeys() throws Exception {
		JsonFileService.getDataFiles();
		Set<String> result = JsonFileService.listKeys("1");
		assertEquals(9, result.size());
	}
	
	/***
	 * error test trying to list keys in a missing file
	 * @throws Exception
	 */
	@Test(expected = FileNotFoundException.class)
	public void testListKeysFileNotFound() throws Exception {
		DataFile dataFile = new DataFile("test.json", "test");
		JsonFileService.getFileMap().put("4", dataFile);
		JsonFileService.listKeys("4");
	}
	
	/***
	 * error test for trying to search in missing file
	 * @throws Exception
	 */
	@Test(expected = IOException.class)
	public void testFindInFileNoFile() throws Exception {
		DataFile dataFile = new DataFile("test.json", "test");
		JsonFileService.findInFile("test.json", "test", dataFile);
	}
}
