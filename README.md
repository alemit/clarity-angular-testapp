# clarity-angular-testapp
This is an exercise application integrating the Clarity UI project together with Angular 5, calling a simple Spring Boot REST app and using PostgreSQL DB

You can start the backend individually with maven:
Using:
`mvn clean install`
`java -jar target\clarity-angular-testapp-0.0.1-SNAPSHOT.jar`

Or by using the spring boot run command:
`mvn spring-boot:run`

You can run the JUnit test by executing:
`mvn test`

The frontend is located under \ui folder and can be started as follows:
First install Node modules with:
`npm install`

After that start the app with:
`npm start`
