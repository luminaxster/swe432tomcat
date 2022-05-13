*Author:* David Gonzalez. Last update: spring 2022

*Modified by:* Jeff Offutt, summer 2020

# Using Heroku to deploy servlets and JSPs

This tutorial explains how to deploy and develop a Heroku app that runs servlets and JSPs through GitHub. The tutorial covers creating GitHub and Heroku accounts, deploying servlets to a Heroku app, developing web apps locally, and using a database to persist data.

Check the currently deployed version: [https://swe432tomcat.herokuapp.com](https://swe432tomcat.herokuapp.com)

## Prelude
To develop web apps, it is important to mentally separate development from deployment. Development includes design, programming testing and debugging. Development is usually done locally on the developer's computer. Deploying is the process of publishing a web app to a server so users can access it, including compiling, installing executable in appropriate directories, checking connections to resources such as databases, and creating the URLs that clients will use to run the web app. In a large project, these issues can get quite complex and professional deployers take care of it. Our deployment will be simpler and student accessible. Heroku is a free hosting service for web apps than can be linked with GitHub to auto-deploy. Heroku also offers development tools so you can test and debug your app **locally**.

In this repo, the Heroku app uses **Tomcat internally**, this in different from using standalone Tomcat or J2EE Glassfish. This means, deployment and development are done via Heroku commands.

Please take a moment to explore each concept, technology, command, activity, and action used in this tutorial. We try to strike a balance between brevity and completeness, and welcome feedback and suggestions.

## Quick Reference
These command line statements are here for a quick reminder of details in the tutorial. **Preferably**, use them after you have read the whole tutorial.

### Redeploying the app by pushing changes to the GitHub remote repo
```ShellSession
git a . & git c -m "TODO: I really should explain these changes" & git push
```

### Rerunning the app locally after changes
```ShellSession
mvn package & heroku local
```

### Heroku Git deployment
Go to [Heroku](https://dashboard.heroku.com/apps) and create a new app (remember the name of the app). I chose `swe432tomcat2`.

Follow these commands in your terminal, replace `swe432tomcat2` with your app's name:

```ShellSession
git clone https://github.com/luminaxster/swe432tomcat.git
cd swe432tomcat/
heroku git:remote -a swe432tomcat2
git add .
git commit -am "first Heroku Git push"
git push heroku
```
You will see your deployment logs in your terminal. At the end, make sure you see these lines:
```ShellSession
remote:        [INFO] BUILD SUCCESS
...
remote: -----> Discovering process types
remote:        Procfile declares types -> web
...
remote: -----> Launching...
remote:        Released v5
remote:        https://swe432tomcat2.herokuapp.com/ deployed to Heroku
```
**Your most recent web app version in now deployed. You are all set. =)**

From now on, if you want to **redeploy** you app with the latest version of your code, rerun these commands:
```shell
git add . & git commit -am "Another Heroku Git push" & git push heroku
```

**Note:** If, somehow, your **[Procfile](https://github.com/luminaxster/swe432tomcat/blob/main/Procfile)** is missing, you will see a log line like this:
```ShellSession
-----> Discovering process types
       Procfile declares types -> (none)
```
Make sure the its location and content matches this repos's, and redeploy your app.
Similarly, if you see a `H10` in your logs oan an application error in your web app, you may have pushed a broken Procfile (Perhaps, you changed it to work locally in a Windows machine). For more details, go to this [section](https://github.com/luminaxster/swe432tomcat/blob/main/README.md#troubleshooting-application-errors).  

### Heroku Dashboard
In your [dashboard](https://dashboard.heroku.com/apps/), after selecting the app related to this project, you may want to check these elements:
 - **Open App:** This button will open a new browser window with the URL of your currently deployed app.
 - **Activity:** This tab contains all logs of each deployment attempt, check it if you get errors in your web app. Run the app locally to debug them.
 - **Settings:** this tab lets you check your build packs, make sure you are using `heroku/java` for this project.

## 1. Create GitHub and Heroku accounts

You can create free accounts on both platforms (you do not need to provide any payment info).

Create a GitHub account on [https://github.com/](https://github.com/).

Create a Heroku account on [https://signup.heroku.com/](https://signup.heroku.com/).


You may optionally use Heroku's [GitHub student package](https://www.heroku.com/github-students).

Your assignments' repo must be private at all times. To grade your assignments, we must have access to your GitHub repo, thus you must add both the TA and instructor as contributors. We will share our usernames on the discussion board.

## 2. Deployment: Create a Git repo (repository) and and link it to a Heroku app
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

**Note:** If you encounter an `Unathorized` prompt while integrating Github and Heroku, follow [this troubleshooting](https://github.com/luminaxster/swe432-heroku-react/blob/master/README.md#github-integration-unavailable).

### F. Updating your repo and redeploying

Your changes will be redeployed automatically when you push them to your repo.

## 3. Development: How to run web apps locally

Before deploying web app to GitHub & Heroku, developers program, debug, and test apps locally. This section explains how to use Apache Maven and Heroku's command line interface (CLI) to run web apps locally. We walk through installing Maven and Heroku, then running an app locally, updating an app, and continuous loop deployment.

### A. Installing Apache Maven

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

### B. Installing Heroku’s CLI
If you have not installed the Heroku CLI before, download it from the [Heroku Dev Center](https://devcenter.heroku.com/articles/heroku-cli).

#### Windows Users Only
By default, the example repo’s [Procfile](https://github.com/luminaxster/swe432tomcat/blob/master/Procfile) is set for Unix-like machines using “sh” as the shell command in Unix. For Windows, replace the following line in the Procfile:

```ShellSession
web: sh target/bin/webapp
```
with this line (**This is Windows Users Only, DO NOT PUSH THIS CHANGE TO YOUR REPO OR IT WIL BREAK YOUR DEPLOYMENT**):

```ShellSession
web: target\bin\webapp.bat
```

##### Troubleshooting Application Errors

If you are Windows user, do not push your Procfile to your remote repo. That would cause the following error:
```ShellSession
"targetbinwebapp not found" error and then an "app crashed" error with code H10 ...
```
Before pushing changes to your repo, make sure the content of the Procfile contains:
```ShellSession
web: sh target/bin/webapp
```
You may also see this as an `Application Error` in your Heroku dashboard or your deployed web app, which may still be related to the `H10` deployment confiration error. By default, Heroku deployment nodes, where your app will be running on, are Unix-like environments, thus your Procfile is set as is. 

Now, the goal is to determine if your application error is a compile-time error or a runtime error. Then choose how to address it. If you got to the "deployment done", that checks out a compile-time error to some extent, we have to confirm it before continuing. For example, if your Heroku app has a servlet generating a web app (HTML+CSS+JS), the application error may be a compile-time error in the browser. The application error caught by Heroku may be in your Java code, at runtime.

Let's proceed to the next step: Are the Heroku logs empty? More details [here](htps://help.heroku.com/UMAUQ4UF/why-am-i-seeing-application-error). In general, check [Heroku Error Codes](https://devcenter.heroku.com/articles/error-codes)(H12, R14, R15, H10, H14 etc.).


If still, the logs are not verbose enough, check if the application runs **locally** under the same behavior. If the app fails, the terminal may show more error details in the stack trace to pinpoint the defective code location. 

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

### E. Adding resources to your web app
 To access files in your generated HTML, first add them to the `src/main/webapp/` folder, and then access them in your html like `<script src="aScript.js"/></script>` or `<link rel="stylesheet" type="text/css" href="aStyle.css">`.

  For example, the resource `src/main/webapp/js/index.js` can be accessed in your generated HTML by adding the line `out.println("<script src=\"js/index.js\"/></script>");` to your servlet before closing the `<head>` element, a line like `out.println("</head>");`.

### F. Don’t forget to deploy your app to Heroku’s server!

Just because your app works locally, does not mean we can run and grade it. The two most common errors that novices make are:

 1. Forgetting to push the changes to your GitHub repo. When you are ready to share, be sure to push your app to your GitHub repo. It will automatically be deployed through Heroku.
 2. Forgetting to deploy-test the app. Sometimes apps that work correctly on your local machine will NOT work on the server. Common issues are path names, such as the difference between `/` and `\` on Windows and Unix systems, capitalization (Windows treats upper and lower case letters the same, but Unix does not), and external resources such as files and databases. **Be sure to run your app on the server to ensure it still works!**.
## 6. Persistence: How to use a database with Heroku
**You only need this section to persist data into a database and can skip it otherwise**.

Accessing a database may be different on your local machine and on Heroku—this description is for Heroku only. Also note that this is not a general tutorial on using databases from Java programs, but just the specific incantations your program needs to use a Postgres database on Heroku.

Set up to use Postgres on Heroku through the Heroku dashboard. Choose your **Tomcat servlet app** on your dashboard, go to the **Resources** tab, click on **find add-ons**, type **Postgres**, and Heroku-Postrgres should appear. Select it with the **Hobby dev** (free) tier.

You can also do this from a terminal command-line window:
```ShellSession
heroku addons:create heroku-postgresql:hobby-dev --app <your_heroku_app_name>
```
Note that `<your_heroku_app_name>` is the name of your Heroku web application.

This section is quite long and is not necessary to run servlets or JSPs. This section discusses installation, configuring, using the command line interface (CLI) and Java to use the database.


### A. Install PostgreSQL
Get Postgres from the [Postgres download page](https://postgresapp.com/downloads.html).

*Windows*: The wizard will install the DB, services, and basic tools needed to manage and query the database.

*MacOS*: Select and install Postgres.app with PostgreSQL 12. Then execute this command in your terminal:
```ShellSession
sudo mkdir -p /etc/paths.d &&
echo /Applications/Postgres.app/Contents/Versions/latest/bin | sudo tee /etc/paths.d/postgresapp
```
Reopen the terminal and try:
```ShellSession
which psql
```

It should return a file system path that looks like this:`/Applications/Postgres.app/Contents/Versions/latest/bin/psql`.

### B. Configure the connection to your remote DB add-ons in Heroku
Java programs access databases using a library package called Java DataBase Connectivity (JDBC). This tutorial does not teach JDBC, but you will need to use it. For your Java applications to access the DB via JDBC, set up the connection as follows:

If you use a *Unix-Like* system, run the command:
```ShellSession
export JDBC_DATABASE_URL=`heroku run echo \\$JDBC_DATABASE_URL -a <your_heroku_app_name>
```
If you use *Windows*, run the commands separately. The first command will return a URL, which you use in the second command to define the environment variable:
```ShellSession
heroku run echo \$JDBC_DATABASE_URL -a <your_heroku_app_name>
setx JDBC_DATABASE_URL "<URL>"
```
Windows users can also use the URL to create a `JDBC_DATABASE_URL` property in the system’s environment variables with that URL.

**Remember:** `<your_heroku_app_name>` is the name of your heroku app.

Check that the environment variable was set:

*Unix*:
```ShellSession
echo $JDBC_DATABASE_URL
```
*Windows*:
```ShellSession
echo %JDBC_DATABASE_URL
```

The echo command should return a string like `jdbc:postgresql://...`.

**Note:** This configuration will be lost when you close the terminal window. Since the credentials are regularly removed, there is no benefit to making it permanent.

### C. Connect to the database via a command line interface (CLI)
Enter the DB with the following command:
```ShellSession
heroku pg:psql <your_postgresql_add_on_name> --app <your_heroku_app_name>
```
**Remember:** `<your_heroku_app_name>` is the name of your heroku app, and `<your_postgresql_add_on_name>` is your Postgres add-on name.

You can get your *precise* command from the Heroku web page. Login to your account, go to the add-ons dashboard, and select the Postgres add-on created some steps before. Then go to **settings > admistration > view credentials > Heroku cli**.

Once that command works, you can use the database CLI. In your terminal window, the input should look like this:
```ShellSession
<your_heroku_app_name>::DATABASE=>
```
Now run DB management and query commands like:
```SQL
CREATE TABLE test(id SERIAL PRIMARY KEY, value VARCHAR (50) NOT NULL);
INSERT INTO test (value) VALUES ('a value');
SELECT name FROM test;
```
### D. Connecting to the database within your app: The Database Servlet
Our example project has an example Java class, [DatabaseServlet.java](https://github.com/luminaxster/swe432tomcat/blob/master/src/main/java/servlet/DatabaseServlet.java), which uses JDBC. This is but one of many possible ways to use databases. The following seven subsections explain how the database servlet was implemented.

#### I. Manage and query your database
[DatabaseServlet.java](https://github.com/luminaxster/swe432tomcat/blob/master/src/main/java/servlet/DatabaseServlet.java) needs the database table to be created before it can run. Do this from your CLI terminal window:

```SQL
CREATE TABLE entries(
  id serial PRIMARY KEY,
  name VARCHAR (50) NOT NULL,
  age INT  CHECK (age > 0  AND age <150) NOT NULL
);
```

As a check, add a row to the table and then query the data from that row:
```SQL
INSERT INTO entries (name, age) VALUES ('Logan', 149);
SELECT name, age FROM entries;
```

#### II. Add Postgres to your web app
You need to add Postgres to your dependencies in your `pom.xml` file:
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
#### III. Sanity Check
To make sure you have the database set up correctly, run the web app locally, from a terminal in your app's root folder:
```ShellSession
mvn package
heroku local
```
The terminal should show a line like:
```ShellSession
INFO: Starting ProtocolHandler ["http-nio-5000"]
```
Your Tomcat server should be up and running a `localhost:5000`, and the database servlet should be at `localhost:5000/database`.

Note: To stop the server from running, press `Ctrl+C` or close the terminal.

#### IV. Connecting to the database in the servlet
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
These statement add Postgres as a dependency and configure the environment variable. The statement `String dbUrl = System.getenv("JDBC_DATABASE_URL");` uses the environment variable `JDBC_DATABASE_URL`. You can check its value with the command `echo $JDBC_DATABASE_URL`. The statement `return DriverManager.getConnection(dbUrl);`retrieves the Postgres database driver from the previous URL, then connects to the database with the credentials provided in the URL.
**Troubleshooting:** You may get an error like `java.sql.SQLException: The url cannot be null`. This means “JDBC_DATABASE_URL” is not saved in your profile as described in step *6.B* above.

#### V. Persisting data into the database
The following code is taken from method *save()* in [DatabaseServlet.java](https://github.com/luminaxster/swe432tomcat/blob/master/src/main/java/servlet/DatabaseServlet.java):
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
After getting a `connection`, the method prepares a statement to insert a row into the table `entries`. The order of values is determined by the tuple `(name, age)`. The statements `statement.setString(1, name)` and `statement.setInt(2, age)` map the value 1 to variable `name` and value 2 to variable `age`. Finally, `statement.executeUpdate();` inserts the row.

**Important:** Prepared statements prevent some types of SQL injection attacks that other API methods allow.

#### VI. Querying data from the database
The following code from method *getAllAsHTMLTable()* retrieves all names and ages from the entries table, then displays them in an HTML table.
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
After getting a connection to the database, executing the query returns an iterable `ResultSet`, which is stored in the variable `entries`. The `while` loop prints all name and age pairs in `entries`.

**Important:** Using only `executeQuery()` to make querys prevents some types of SQL injection attacks that other API methods allow. For instance, using `executeUpdate()` to make queries can allow attackers to delete database tables by appending a delete command to the update statement: `; delete from entries`.

#### VII. Important: Avoid cross-side scripting (XSS) attacks

![Funny comic strip](https://imgs.xkcd.com/comics/exploits_of_a_mom.png "Special thanks to Nathan Harvey for sharing this (spring 2021 class)")

A common way to attack web apps is to inject malicious code into data that originates as user inputs. This is call cross-site scripting (XSS.) Since it is common to generate HTML content from user data, an attacker may add executable code that will trigger in the page. For example, adding `<script>function xss(){location.href='https://www.google.com'} </script><button onclick="xss()">click me</button>` as a name can add a malicious button that takes users to `google.com` when clicked.The database fails because the table column does not accept more than 50 characters. However, `<script>function x(){location.href='s'}()</script>` fits. A good way to avoid this (illustrated in our example) is to use [Jsoup](https://jsoup.org/) to sanitize data when accepting user inputs, and when sending them back to your front-end. The database example uses Jsoup.clean() when getting row’s names from the database, and when capturing strings from the users, as in: `String name = Jsoup.clean(request.getParameter(Data.NAME.name()), Whitelist.basic());`.

**Always** use **sanitized** user inputs to assemble statements, and use **prepared statements** when concatenating **user inputs** in your queries or updates.

#### VIII. Persisting data into a database from a servlet
The following code from the *doPost()* method adds new data into the database.

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

The servlet uses database persistence via the **EntriesManager** instance to save (`save(name, age)`) a new entry, then renders all the entries in the database into an HTML table with *getAllAsHTMLTable()*.

## 5. Resources
For more details about how to create a Tomcat setup from scratch, go to the Dev Center guide on how to [Create a Java Web Application using Embedded Tomcat](https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat).

- [Git Tutorial](https://kbroman.org/github_tutorial/pages/init.html)

- [Heroku Postgres](https://devcenter.heroku.com/articles/heroku-postgresql)

- [Heroku Dataclips](https://devcenter.heroku.com/articles/dataclips)

- [Java XML writing and reading](https://www.vogella.com/tutorials/JavaXML/article.html)
