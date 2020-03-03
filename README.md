# zendeskSearch
application to search in json files
user can search by first level key and value combination

### PrerequisitesPrerequisites
- [java 8](https://java.com/en/download/ "java 8") or above
- latest [gradle]( https://gradle.org/install/ "gradle") installed in local or and IDE with gradle plugin

### Running the Application
 #####  if you have an IDE with gradle plugin
 - clone the application into your local machine- 
 - import the project from the IDE as a gradle project
 - build the project with the gradle plugin
 - run test classes in src/test/java if your IDE has JUnit plugin
 - run the main class SearchApplication.java

 ##### from command line
 - 	clone the application
 - 	navigate to the folder
 -  make sure you have installed gradle already
 - 	type <gradle clean build> to build the app
 - 	type <./gradlew test> to run tests
 - 	to run the app from terminal
 - 	navigate to zendeskChallenge\zendeskSearch\src\main\java\com\zendesk\search
 - 	type <java SearchApplication>
	
##### instructions
- 	as the application starts, it shows list of files to search from with a number next to it
- 	type in the number corresponding to the file you would like to search in
- 	the app next displays list of keys that can be queried on
- 	type in the key you would like to search
- 	next type in the value that should match with the key
- 	if any match were to be found, the app would display them
- 	if no match was found a message saying so would be printed
- 	you can repeat the same exercise with a different file
- 	to exit the app type quit
