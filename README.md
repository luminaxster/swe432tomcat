*Author:* David Gonzalez, spring 2020

*Modified by:* Jeff Offutt, summer 2020

# Using Heroku to deploy servlets and JSPs

This tutorial explains how to deploy and develop a Heroku app that runs servlets and JSPs through GitHub. The tutorial covers creating GitHub and Heroku accounts, deploying serlvets to a Heroku app, developing web apps locally, and using a database to persist data.

Check the currently deployed version: [https://swe432tomcat.herokuapp.com](https://swe432tomcat.herokuapp.com)

## 1. Prelude
To develop web apps, it is important to mentally separate development from deployment. Development includes design, programming testing and debugging. Development is usually done locally on the developer's computer. Deploying is the process of publishing a web app to a server so users can access it, including compiling, installing executable in appropriate directories, checking connections to resources such as databases, and creating the URLs that clients will use to run the web app. In a large project, these issues can get quite complex and professional deployers take care of it. Our deployment will be simpler and student accessible. Heroku is a free hosting service for web apps than can be linked with GitHub to auto-deploy. Heroku also offers development tools so you can test and debug your app locally.

Please take a moment to explore each concept, technology, command, activity, and action used in this tutorial. We try to strike a balance between brevity and completeness, and welcome feedback and suggestions.

## 2. Quick Reference
These command line statements are here for a quick reminder of details in the tutorial.

### Redeploying the app by pushing changes to the remote repo
```ShellSession
git a . & git c -m "TODO: I really should explain these changes" & git push
```

### Rerunning the app locally after changes
```ShellSession
mvn package & heroku local
```

## 3. Create GitHub and Heroku accounts

You can create free accounts on both platforms (you do not need to provide any payment info).

Create a GitHub account on [https://github.com/](https://github.com/).

Create a Heroku account on [https://signup.heroku.com/](https://signup.heroku.com/).


You may optionally use Heroku's [GitHub student package](https://www.heroku.com/github-students).

Your assignments' repo must be private at all times. To grade your assignments, we must have access to your GitHub repo, thus you must add both the TA and instructor as contributors. We will share our usernames on the discussion board.

## 4. Deployment: Create a Git repo (repository) and and link it to a Heroku app
You can install Git on your local machine from [Git's download site](https://git-scm.com/downloads). Now, follow steps A—F to bring this repo into your Github account:

### A. Get this repo locally in your machine:
This code contains all necessary boilerplate for supporting servlets and JSPs in a Heroku app through a terminal (command-line) window:
```ShellSession
git clone https://github.com/luminaxster/swe432tomcat.git
```

### B. Create an empty repo in your Github:
1. Go to Github, login into your account, select the "repositories" tab, click on ”New.” Once in the "Create a new repository" page: give a name to your repo, make it private and let other options default, and click on "create repository".

2. This will take you to your new repository's page. Copy the url to access your repo in the quick setup section. It should look like this:

```HTTP
https://github.com/<your_username>/<newly_created_repo_name>.git
```
We will use this URL in subsequent steps.

### C. Redirect the local repo to your own repo and save the changes:
Remember to replace the url from step 2 (`https://github.com/<your_username>/<newly_created_repo_name>.git`) with your own repo’s url.

1. Setting up your remote repo in Git

Only use the following commands once, in a terminal command-line window:
```ShellSession
cd swe432tomcat
git init
git remote set-url origin "https://github.com/<your_username>/<newly_created_repo_name>.git"
```
2. Push your local changes into your remote repo

Reuse the following commands every time you need to send your changes to GitHub, in a terminal command-line window:

```ShellSession
git add .
git commit -m "Initial commit: cloned repo"
git push
```
Be clear and explicit with your commit messages. The idea is to document why you made the changes so that everyone who wants to contribute to your project understand, including you after a month not looking at the file.

### D. Create a Heroku app
Go to your [Heroku dashboard](https://dashboard.heroku.com/apps). Click on **New > Create New App**, provide a name, and click on **create app**.

### E. Link repo and deploy

Once in your Heroku app web page, select the "deploy" tab:

 a. set the deploy method to "Github"
 
 b. authorize Heroku to access your GitHub repositories
 
 c. select the recently created one
 
 d. click on "connect"
 
 e. activate automatic deploys
 
 f. click on deploy the `master` branch (only this time so you can see the changes immediately)
 
 g. Once your deploy is processed, click on "View"
 
 Once in your Heroku app web page, select the **deploy** tab:
 1. Set the deploy method to **Github**
 2. Authorize Heroku to access your GitHub repositories
 3. Select the recently created repo
 4. Click on **connect**
 5. Activate automatic deploys
 6. Click on **deploy the master branch** (only this time so you can see the changes immediately)
 7. Once your deployment is processed, click on **View**
 
### F. Updating your repo and redeploying

Your changes will be redeployed automatically when you push them to your repo.

## 5. Development: How to run web apps locally

Before deploying web app to GitHub & Heroku, developers program, debug, and test apps locally. This section explains how to use Apache Maven and Heroku's command line interface (CLI) to run web apps locally. We walk through installing Maven and Heroku, then running an app locally, updating an app, and continuous loop deployment.

#### A. Installing Apache Maven

If you have not installed Apache Maven before, get the binaries from the [Apache Maven Project](https://maven.apache.org/download.cgi), and follow the instructions from Maven’ [installation page](https://maven.apache.org/install.html).

Note: If you are using a Unix-like system (MacOS, Linux, ect.), open a command-line terminal window and add the path to Maven permanently in your bash profile:

```ShellSession
vi ~/.bash_profile
```
Add the following line to allow you to run Maven the terminal:

```ShellSession
export PATH=/opt/apache-maven-3.6.3/bin:$PATH
```
If you have a later version of Maven or installed it in a different place, change the path accordingly.

If your machine runs Windows, add Maven’s path to the PATH property in the system’s environment variables.

**Note:** You will need to open a new terminal window to reflect the path change.

#### B. Installing Heroku’s CLI
If you have not installed the Heroku CLI before, download it from the [Heroku Dev Center](https://devcenter.heroku.com/articles/heroku-cli).

By default, the example repo’s [Procfile](https://github.com/luminaxster/swe432tomcat/blob/master/Procfile) is set for Unix-like machines using “sh” as the shell command in Unix. For Windows, replace the following line in the Procfile:

```ShellSession
web: sh target/bin/webapp
```
with this line:

```ShellSession
web: target\bin\webapp.bat
```
Note: If you are Windows user, do not push your Procfile to your remote repo. That would cause the following error: 
```ShellSession
"targetbinwebapp not found" error and then an "app crashed" error with code H10 ...
```

### C. Build and run your app
To run an app contained in your repo, look up for the file `POM.xml` in your repo’s root folder. Maven uses this configuration file to build your app so Heroku can run it. Run the following two commands to build and run your app:

```ShellSession
mvn package
heroku local
```

If the commands succeed, you should be able to access your app at: `http://localhost:5000`.

### D. Adding a new servlet

In your machine, place your servlet file in the `src/main/java/servlet` folder and add the servlet annotation so your Apache Tomcat knows how to map it:
```Java
import javax.servlet.annotation.WebServlet;
...
@WebServlet( name = "servletName", urlPatterns = {"/servicePathName"} )
```
The line above handles **servlet mapping**, which makes its servlet instance available at yourServerUrl/**servicePathName**.

Now you can observe, debug or test your app locally by building your app (in terminal: `mvn package`) and running it in your local server (in terminal: `heroku local`). After that, `localhost:5000/servicePathName` (as in urlPatterns property from the annotation above) must be working.

**Note:** If your servlet mapping setup failed or is missing, the URL `localhost:5000/servicePathName` or `yourWebsite/servicePathName` won't be accessible and show a `404: Not found error`. Make sure the `@WebServlet` annotation is in the desired servlet java file and the `localhost:5000/servicePathName` matches `@WebServlet.. urlPatterns = {"/servicePathName"}`.


Place your servlet file in the folder `src/main/java/servlet` folder and add the servlet annotation so your Apache Tomcat knows how to map it:
```Java
import javax.servlet.annotation.WebServlet;
...
@WebServlet( name = "servletName", urlPatterns = {"/servicePathName"} )
```
The line above handles **servlet mapping**, which makes its servlet instance available at **yourServerUrl/servicePathName**. That is, the `@WebServlet` annotation “maps” the name of the servlet, `servletName`, to the path `yourServerUrl/servicePathName`, which is appended after the port number 5000. Now you can run your app locally.

Note: If your servlet mapping setup failed or is missing, you will not be able to access the URL `localhost:5000/servicePathName` or `yourWebsite/servicePathName`. Instead, the server will return a `404: Not found error`. Make sure the `@WebServlet` annotation is in the servlet Java file and the `localhost:5000/servicePathName` matches `@WebServlet.. urlPatterns = {"/servicePathName"}`.

## E. Don’t forget to deploy your app to Heroku’s server!

Just because your app works locally, does not mean we can run and grade it. The two most common errors that novices make are:

 1. Forgetting to push the changes to your GitHub repo. When you are ready to share, be sure to push your app to your GitHub repo. It will automatically be deployed through Heroku.
 2. Forgetting to deploy-test the app. Sometimes apps that work correctly on your local machine will NOT work on the server. Common issues are path names, such as the difference between `/` and `\` on Windows and Unix systems, capitalization (Windows treats upper and lower case letters the same, but Unix does not), and external resources such as files and databases. **Be sure to run your app on the server to ensure it still works!**.
# Add database persistence to your Heroku app
Go to your Heroku dashboard, choose your Tomcat servlet app, go to the Resources tab, click on find add-ons, type  postgres, Heroku-Postrgres will show up, select it with Hobby dev (free) tier.

**Or**, in a terminal:
```ShellSession
heroku addons:create heroku-postgresql:hobby-dev --app <your_heroku_app_name>
```
**Remember:** `<your_heroku_app_name>` is the name of your heroku app.

## Install PostgreSQL
You can get Postgres [here](https://postgresapp.com/downloads.html) and choose your platform binaries.

*Windows:* Using the wizard will install the DB, services and basic tools we need to manage and query the database.


*Mac:* Select Postgres.app with PostgreSQL 12. Then execute this command in your terminal:
```ShellSession
sudo mkdir -p /etc/paths.d &&
echo /Applications/Postgres.app/Contents/Versions/latest/bin | sudo tee /etc/paths.d/postgresapp
```
Reopen the terminal and try:
```ShellSession
which psql
```

It should return a file system path like `/Applications/Postgres.app/Contents/Versions/latest/bin/psql`.

## Configure the connection to your remote DB add-ons in Heroku
In order for your Java applications to access the DB via JDBC, you need to setup the connection. In your terminal, execute:
```ShellSession
export JDBC_DATABASE_URL=`heroku run echo \\$JDBC_DATABASE_URL -a <your_heroku_app_name>
```
**Remember:** `<your_heroku_app_name>` is the name of your heroku app.

Double check the environment variable was set:
```
echo $JDBC_DATABASE_URL
```
It should return a string like `jdbc:postgresql://...`.

**Note:** This configuration will be lost once you close the terminal, do no try to make it permanent, the crendentials are renovated often.

### Connect to you database via CLI
In your terminal, enter to your DB with the following command:
```ShellSession
heroku pg:psql <your_postgresql_add_on_name> --app <your_heroku_app_name>
```
**Remember:** `<your_heroku_app_name>` is the name of your heroku app, and `<your_postgresql_add_on_name>` is your postgres add-on name.

You can get your precise command from your Postgres add-on dashboard, go to settings > admistration > view credentials > **Heroku cli**

Once that command executes correctly, you should be now using the database CLI, in your shell, the input should look like this:
```ShellSession
<your_heroku_app_name>::DATABASE=>
```
Now you run DB management and query commands like:
```SQL
CREATE TABLE test(id SERIAL PRIMARY KEY, value VARCHAR (50) NOT NULL);
INSERT INTO test (value) VALUES ('a value');
SELECT name FROM test;
```
## Connecting to the database within your app: The Database Servlet
This project has an [example](https://github.com/luminaxster/swe432tomcat/blob/master/src/main/java/servlet/DatabaseServlet.java) using Java DataBase Connection(JDBC), there are plenty of ways to use Java or related frameworks APIs to connect to databases elsewhere.

Follow the next sections to follow how the Database Servlet was implemented. 
### 1. Manage and query your database
In your database CLI (the same accessed in [a previous section](blah)), create the folowing table, it is required by the [DB servlet](https://github.com/luminaxster/swe432tomcat/blob/master/src/main/java/servlet/DatabaseServlet.java) to work:

```SQL
CREATE TABLE entries( 
  id serial PRIMARY KEY,
  name VARCHAR (50) NOT NULL,
  age INT  CHECK (age > 0  AND age <150) NOT NULL
);
```

Try adding a row to the table:
```SQL
INSERT INTO entries (name, age) VALUES ('Logan', 149);
```
Or querying persisted data in that table:
```SQL
SELECT name, age FROM entries;
```

### 2. Add Postgres to your app
You need to add Postgres to your dependencies in your `pom.xml`:
```XML
</dependencies>
    ...
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.2.1</version>
    </dependency>
    ...
</dependencies>
```
### 3. Sanity Check
Try to run the app locally, at a terminal in your app's root folder:
```ShellSession
mvn package
heroku local
```
The terminal should show a line like:
```ShellSession
INFO: Starting ProtocolHandler ["http-nio-5000"]
```
Your Tomcat server should be up and running at `localhost:5000`, and the database servlet should be at `localhost:5000/database`.

**Note:** To stop the server from running, press `Ctrl+C`.  If you close the terminal, the server will stop as well. 

### 4. Connecting to database in the servlet
```Java
...
private class EntriesManager{
      private Connection getConnection()
        throws URISyntaxException, SQLException {
          String dbUrl = System.getenv("JDBC_DATABASE_URL");
          return DriverManager.getConnection(dbUrl);
      }
      ...
```
This lines are where adding Postgres as a dependency, and configuring the environment variable come together, missing any of these steps will cause a runtime error. In `String dbUrl = System.getenv("JDBC_DATABASE_URL");`, the environment variable is used `JDBC_DATABASE_URL`, make sure it is setup with `echo $JDBC_DATABASE_URL`. Then in `return DriverManager.getConnection(dbUrl);`, the Postgres database driver is detected based on the previous URL, the driver manager will look for it in your Postgres installed dependency and the use it to connect to the database with the credentiasl specified in the URL. 

**Troubleshooting:** You may get an error like `java.sql.SQLException: The url cannot be null`. Since `"JDBC_DATABASE_URL"` is not saved to your profile, make sure is set up before running `heroku local`, this is explained in this [previous section](https://github.com/luminaxster/swe432tomcat/blob/master/README.md#configure-the-connection-to-your-remote-db-add-ons-in-heroku).

### 5. Saving data into the database
```Java
...
public boolean save(String name, int age){
        PreparedStatement statement = null;
        try {
          connection = connection == null ? getConnection() : connection;
          statement = connection.prepareStatement(
          "INSERT INTO entries (name, age) values (?, ?)"
          );
          statement.setString(1, name);
          statement.setInt(2, age);
          statement.executeUpdate();
          return true;
          ...
```
After getting a new or existing `connection`, preparing a statement to insert a row in table `entries`, the order of values set follow the sequence determined by the tuple `(name, age)` in the statement: `statement.setString(1, name)` is concatenated where the first question mark is in `(?, ?)` and ` statement.setInt(2, age)` to the second.

Finally,  `statement.executeUpdate();` will attempt to insert the row, if no errors are thrown, it does not mean it succeded, the return value of the method is a count of the affected rows, in this context `1` should be successfully inserted one row. 

**Important:** Prepared statements prevent some types of SQL injection attacks. Using other API methods to do so will let your server vulnerable. more details in the next sections.

### 6. Querying data from the database
```Java
public String getAllAsHTMLTable(){
        Statement statement = null;
        ResultSet entries = null;
        StringBuilder htmlOut = new StringBuilder();
        try {
          connection = connection == null ? getConnection() : connection;
          statement = connection.createStatement();
          entries = statement.executeQuery(
          "SELECT "+Data.NAME.name()+", "+Data.AGE.name()+" FROM entries");

          while (entries.next()) {
              htmlOut.append("<tr><td>");
              htmlOut.append(Jsoup.clean(entries.getString(1), Whitelist.basic())); //name
              htmlOut.append("</td><td>");
              htmlOut.append(entries.getInt(2)); //age
              htmlOut.append("</td></tr>");
          }
          if(htmlOut.length() == 0){
            htmlOut.append("<tr><td> no entries</td></tr>");
          }
        }catch(URISyntaxException uriSyntaxException){
        ...
```
After gettign a new or exisitng connection the database, executing the query returns a ResultSet, which can be iterated. In this case were building a HTML table with rows obtained from the query result.

**Important:** Using only `executeQuery()` to make querys prevents some types of SQL injection attacks. Using other API methods to do so will let your server vulnerable. For instance, using `executeUpdate()` to make queries will allow attackers to delete your database tables by ending the current statement and appending a delete update `; delete from entries`.

### ImportantER: Avoid XSS attacks
A common way to attack (web) apps is to inject malicious code in data captured from user inputs -- Cross-Site Scripting. Since it is common to generate HTML content from user data, an attacker may add executable code that will trigger in the page. For example, adding `<script>function xss(){location.href='https://www.google.com'} </script><button onclick="xss()">click me</button>` in the name in any of the Persistence examples will succeded on adding a malicious button that takes you to `google.com` once clicked. The database fails because of the table column not accepting more than 50 characters. However, `<script>function x(){location.href='s'}()</script>` fits.
Consider using [Jsoup](https://jsoup.org/) to sanitize data when capturing user inputs in your services, and also when sending them back to your front-end. The Database example uses `Jsoup.clean()` when getting row's names from the database, and capturing string from the users `String name = Jsoup.clean(request.getParameter(Data.NAME.name()), Whitelist.basic());`.

**Always** use **sanitized** user inputs to assemble statements, and use **prepared statements** when concatenating **user inputs** in your queries or updates.

### 7. Using both it the servlet

```Java
... doPost(...
 EntriesManager entriesManager = new EntriesManager();

       boolean ok = entriesManager.save(name,age);
       String saveStatusHTML =
       "<p>"+(ok? "Entry added.":"Entry was not added.")+"</p>";
       PrintHead(out);
       PrintEntriesBody(
        out, saveStatusHTML, entriesManager.getAllAsHTMLTable());
       PrintTail(out);
 ```
 
Finally, the servlet can use database persistence via the EntryManager instance to save (`save(...)`) a new entry and rendering all the entries in the database in a HTML table (`getAllAsHTMLTable()`).

# Grading: sharing your repo with the TA
Your assignment's repo must be private at all times and for me to grade your code, please add me as a contributor. My username is luminaxster.

## Follow the original guide
For more details about how to create a Tomcat setup from scratch, go to the Dev Center guide on how to [Create a Java Web Application using Embedded Tomcat](https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat).

## Resources: 

https://kbroman.org/github_tutorial/pages/init.html 

https://devcenter.heroku.com/articles/heroku-postgresql

https://devcenter.heroku.com/articles/heroku-postgresql#local-setup

https://devcenter.heroku.com/articles/dataclips

https://www.vogella.com/tutorials/JavaXML/article.html



