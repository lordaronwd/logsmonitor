# logsmonitor
Logs Monitor application:

If you are not interested in building the application from the code, you can use provided `monitor-impl-1.0.jar` file.
In such case go directly to the step number 5.
In case you want to build only backend application, having in mind that it already has the embeded UI components
in the 'resources/' folder, go directly to the step number 3. 

Prerequisites: 
	1. maven
	2. node.js

To build the application from code:

	1. Go to the root folder of the LogsMonitor frontend application and run `npm run build`
	2. Copy the content of the `/build/` folder to the backend's application `/impl/src/main/resources/ folder`
	3. Go to the root of the LogsMonitor backend application and run `mvn clean package`

To start the application:
	   
	4. Go to the `/impl/target/` folder and copy the `monitor-impl-1.0.jar` file to arbitrary location
	5. Create the folder named 'config/' next to the coppied `monitor-impl-1.0.jar` file
	6. Copy the application.properties file to the 'config/' created in the previous step
	7. Change the property values inside the application.properties file to mach your preferences
	8. Go back to the folder containing `monitor-impl-1.0.jar` and run 'java -jar monitor-impl-1.0.jar'

To use the application:

	9. Go to the http://localhost:8080/logsmonitorapp/index.html in your browser.
