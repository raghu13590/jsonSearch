package com.zendesk.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;

import com.zendesk.fileservice.JsonFileService;

/***
 * class to interface with command line
 * @author raghu
 *
 */
public class SearchHelper {
	
	private static final String QUIT = "quit";
	private static final String ZERO = "0";
	
	/***
	 * displays main menu, with list of files to search from
	 * @param br
	 * @throws IOException
	 * @throws Exception
	 */
	static void showMainMenu(BufferedReader br) throws IOException, Exception {
		String userInput = "";
		while (!QUIT.equalsIgnoreCase(userInput) && userInput != null) {
			Map<String, DataFile> fileMap = JsonFileService.getFileMap();
			if (fileMap == null || fileMap.isEmpty()) {
				throw new Exception("data files not loaded");
			}
			fileMap.forEach((index,dataFile) -> System.out.println("enter " + index + " for " + dataFile.getFileName()));
			System.out.print("enter quit to exit\n");
			userInput = br.readLine();
			if (fileMap.keySet().contains(userInput)) {
				userInput = showSubMenu(br, userInput);
			} else if (!QUIT.equalsIgnoreCase(userInput)) {
				System.out.println("please enter a valid input\n");
			}
		}
	}
	
	/***
	 * displays sub menu with keys in selected file
	 * @param br
	 * @param userInput
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private static String showSubMenu(BufferedReader br, String userInput) throws IOException, Exception {
		DataFile dataFile = JsonFileService.getFileMap().get(userInput);
		System.out.print("here are of the search keys for " + dataFile.getFileName() + "\n");
		Set<String> keys = JsonFileService.listKeys(userInput);
		System.out.println(keys + "\n");
		System.out.println("enter a search key or 0 for main menu\n");
		do {
			userInput = br.readLine();
			if (ZERO.equals(userInput)) {
				continue;
			} else if (keys.contains(userInput)) {
				findSearchValue(br, userInput, dataFile);
				break;
			} else {
				System.out.println("please enter a valid search key or 0 for main menu\n");
			}
		} while (!ZERO.equals(userInput) && userInput != null);
		return userInput;
	}
	
	/***
	 * consumes search value and displays results
	 * @param br
	 * @param userInput
	 * @param dataFile
	 * @throws IOException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static void findSearchValue(BufferedReader br, String userInput, DataFile dataFile)
			throws IOException, Exception {
		System.out.println("enter search value\n");
		String searchValue = br.readLine();
		JSONArray results = JsonFileService.findInFile(userInput, searchValue, dataFile);
		if (!results.isEmpty()) {
			System.out.println("found " + results.size() + " matches");
			results.forEach(result -> System.out.println(result));
			System.out.println("\n");
		} else {
			System.out.println("no match found for " + userInput + " and " + searchValue + "\n");
		}
	}
	
}
