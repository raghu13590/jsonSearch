package com.zendesk.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.zendesk.fileservice.JsonFileService;

/***
 * command line application to search data in files in com.zendesk.data folder
 * @author raghu
 *
 */
public class SearchApplication {

	private static final Logger logger = Logger.getLogger(SearchApplication.class.getName());
	
	public static void main(String[] args) throws Exception {
		logger.info("application started...");
		JsonFileService.getDataFiles();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		SearchHelper.showMainMenu(br);
		logger.info("application shutting down...");
	}
}
