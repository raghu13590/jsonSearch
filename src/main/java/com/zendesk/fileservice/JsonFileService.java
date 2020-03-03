package com.zendesk.fileservice;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.zendesk.search.DataFile;

/***
 * class to interface with files and do heavy lifting work
 * 
 * @author raghu
 *
 */
public class JsonFileService {

	private static final int FIRST_ELEMENT = 0;
	private static final String EMPTY_BRACES = "{}";
	private static final String DOT = ".";
	private static final String DATA_PATH = "com/zendesk/data";
	private static final String JSONEXT = ".json";
	private static final int ONE = 1;
	private static JSONParser parser = new JSONParser();
	private static final Logger logger = Logger.getLogger(JsonFileService.class.getName());
	protected static Map<String, DataFile> fileMap = new HashMap<>();
	
	public static Map<String, DataFile> getFileMap() {
		return fileMap;
	}

	public static void setFileMap(Map<String, DataFile> fileMap) {
		JsonFileService.fileMap = fileMap;
	}

	/***
	 * scans {@link #DATA_PATH} folder for list of json files and adds to {@value #fileMap}
	 * 
	 * @throws IOException
	 */
	public static void getDataFiles() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(DATA_PATH);
		String path = url.getPath();
		File[] files = new File(path).listFiles();
		Integer index = ONE;
		for (File file : files) {
			String fileName = file.getName();
			if (!fileName.contains(DOT)) {
				continue;
			}
			String fileExtension = fileName.substring(fileName.lastIndexOf(DOT), fileName.length());
			if (JSONEXT.equalsIgnoreCase(fileExtension)) {
				//TODO remove file ext
				DataFile dataFile = new DataFile(fileName, path);
				fileMap.put(index.toString(), dataFile);
				index++;
			}
		}
	}

	/***
	 * iterates through file to search for matching result, matches empty and null
	 * values
	 * 
	 * @param key
	 * @param searchValue
	 * @param dataFile
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray findInFile(String key, String searchValue, DataFile dataFile)
			throws IOException, ParseException {
		JSONArray matchingResults = new JSONArray();
		Reader reader = new FileReader(dataFile.getPath());
		JSONArray jsonArray = (JSONArray) parser.parse(reader);
		Iterator<?> jsonIterator = jsonArray.iterator();
		while (jsonIterator.hasNext()) {
			JSONObject jsonElement = (JSONObject) jsonIterator.next();
			// log warning message if json element does not have a key
			if (!jsonElement .containsKey(key)) {
				Object id = jsonElement.get("_id");
				logger.warning("key :: " + key + " missing in file :: " + dataFile.getFileName() + " for id :: " + id
						+ " check file for data consistency");
			} else {
				Object jsonValue = jsonElement.get(key);
				if (jsonValue instanceof JSONArray) {
					JSONArray nestedJsonArr = (JSONArray) jsonValue;
					nestedJsonArr.forEach(iJson -> checkForMatch(searchValue, matchingResults, jsonElement, iJson));
				}
				checkForMatch(searchValue, matchingResults, jsonElement, jsonValue);
			}	
		}
		return matchingResults;
	}
	
	/***
	 * adds to matchingResults if jsonValue matches searchValue, matches null jsonValues to empty searchValue
	 * @param searchValue
	 * @param matchingResults
	 * @param jsonElement
	 * @param jsonValue
	 */
	@SuppressWarnings("unchecked")
	private static void checkForMatch(String searchValue, JSONArray matchingResults, JSONObject jsonElement,
			Object jsonValue) {
		if (!(jsonValue == null) && jsonValue.toString().equals(searchValue)
				|| ((jsonValue == null || EMPTY_BRACES.equals(jsonValue.toString())) && searchValue.isEmpty())) {
			matchingResults.add(jsonElement);
		}
	}

	/***
	 * returns top level keys in a json file
	 * @param fileIndex
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> listKeys(String fileIndex) throws IOException, ParseException {
		Set<String> keySet = new HashSet<>();
		DataFile dataFile = JsonFileService.fileMap.get(fileIndex);
		Reader reader = new FileReader(dataFile.getPath());
        JSONArray jsonArray = (JSONArray) parser.parse(reader);
        JSONObject jsonObject = (JSONObject) jsonArray.get(FIRST_ELEMENT);
        keySet = jsonObject.keySet();
		return keySet;
	}
}
