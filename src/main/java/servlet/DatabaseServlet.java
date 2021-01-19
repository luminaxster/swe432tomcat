package servlet;
// Written by David Gonzalez, April 2020
// Modified by Jeff Offutt
// Built to deploy in github with Heroku
// IF STUCK, READ THE TUTORIAL:
//https://github.com/luminaxster/swe432tomcat#add-database-persistence-to-your-heroku-app

/*
requires Postgresql and Jsoup in your pom.xml
<dependencies>
...

<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>42.2.1</version>
</dependency>
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.13.1</version>
</dependency>


...
*/
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URISyntaxException;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "DBPersistence", urlPatterns = {"/database"})
public class DatabaseServlet extends HttpServlet{
  static enum Data {AGE, NAME};

  static String Domain  = "";
  static String Path    = "";
  static String Servlet = "database";

  // Button labels
  static String OperationAdd = "Add";

  private static Connection connection = null;

  private class EntriesManager{
      private Connection getConnection()
        throws URISyntaxException, SQLException {
          String dbUrl = System.getenv("JDBC_DATABASE_URL");
          return DriverManager.getConnection(dbUrl);
      }

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
        }catch(URISyntaxException uriSyntaxException){
          uriSyntaxException.printStackTrace();
        }
        catch (Exception exception) {
          exception.printStackTrace();
        }finally {
          if (statement != null) {
            try{
              statement.close();
            }catch(SQLException sqlException){
              sqlException.printStackTrace();
            }
          }
        }

        return false;
      }
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
          uriSyntaxException.printStackTrace();
        }
        catch (Exception exception) {
          exception.printStackTrace();
        }finally {
          if (statement != null) {
            try{
              statement.close();
            }catch(SQLException sqlException){
              sqlException.printStackTrace();
            }
          }
        }

        return "<table><tr><th>Name</th><th>Age</th></tr>"
          + htmlOut.toString()+"</table>";
      }
    }

  /** *****************************************************
   *  Overrides HttpServlet's doPost().
   *  Converts the values in the form, performs the operation
   *  indicated by the submit button, and sends the results
   *  back to the client.
  ********************************************************* */
  @Override
  public void doPost (HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
  {
     String name = Jsoup.clean(
      request.getParameter(Data.NAME.name()), Whitelist.basic());
     String rawAge = request.getParameter(Data.AGE.name());
     Integer age  = null;

     String error = "";
     if(name == null){
       error= "<li>Name is required</li>";
       name = "";
     }

     if(rawAge == null){
       error+= "<li>Age is required.<li>";
       rawAge = "";
     }else{
          try{
            age =new Integer(rawAge);
            if(age<1){
                error+= "<li>Age must be an integer greater than 0.</li>";
                rawAge = "";
            }else{
              if(age>150){
                  error+= "<li>Age must be an integer less than 150.</li>";
                  rawAge = "";
              }
            }
          }catch (Exception e) {
            error+= "<li>Age must be an integer greater than 0.</li>";
          }
     }

     response.setContentType("text/html");
     PrintWriter out = response.getWriter();

     if (error.length() == 0){
       EntriesManager entriesManager = new EntriesManager();

       boolean ok = entriesManager.save(name,age);
       String saveStatusHTML =
       "<p>"+(ok? "Entry added.":"Entry was not added.")+"</p>";
       printHead(out);
       printResponseBody(
        out, saveStatusHTML, entriesManager.getAllAsHTMLTable());
       printTail(out);
     }else{
       printHead(out);
       printBody(out, name, rawAge, error);
       printTail(out);
     }


  }  // End doPost

  /** *****************************************************
   *  Overrides HttpServlet's doGet().
   *  Prints an HTML page with a blank form.
  ********************************************************* */
  @Override
  public void doGet (HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException
  {
     response.setContentType("text/html");
     PrintWriter out = response.getWriter();
     printHead(out);
     printBody(out, "", "", "");
     printTail(out);
  } // End doGet

  /** *****************************************************
   *  Prints the <head> of the HTML page, no <body>.
  ********************************************************* */
  private void printHead (PrintWriter out){
     out.println("<html>");
     out.println("");
     out.println("<head>");
     out.println("<title>Database Persistence Example</title>");
     // Put the focus in the name field
     out.println ("<script>");
     out.println ("  function setFocus(){");
     out.println ("    document.persist2file.NAME.focus();");
     out.println ("  }");
     out.println ("</script>");
     out.println("</head>");
     out.println("");
  }
  /** *****************************************************
   *  Prints the <BODY> of the HTML page
  ********************************************************* */
  private void printBody (
   PrintWriter out, String name, String age, String error){
    out.println("<body onLoad=\"setFocus()\">");
    out.println("<p>");
    out.println(
      "A simple example that demonstrates how to persist data into a database");
    out.println("</p>");

    if(error != null && error.length() > 0){
      out.println(
      "<p style=\"color:red;\">Please correct the following and resubmit.</p>");
      out.println("<ol>");
      out.println(error);
      out.println("</ol>");
    }

    out.print  ("<form name=\"persist2file\" method=\"post\"");
    out.println(" action=\""+Domain+Path+Servlet+"\">");
    out.println("");
    out.println(" <table>");
    out.println("  <tr>");
    out.println("   <td>Name:");
    out.println("   <td><input type=\"text\" name=\""
      +Data.NAME.name()+"\" value=\""+name+"\" size=30 required>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("   <td>Age:");
    out.println("   <td><input type=\"text\" "
      +"oninput=\"this.value=this.value.replace(/[^0-9]/g,'');\" name=\""
      +Data.AGE.name()+"\" value=\""+age+"\" size=3 required>");
    out.println("  </tr>");
    out.println(" </table>");
    out.println(" <br>");
    out.println(" <br>");
    out.println(" <input type=\"submit\" value=\""
      + OperationAdd + "\" name=\"Operation\">");
    out.println(" <input type=\"reset\" value=\"Reset\" name=\"reset\">");
    out.println("</form>");
    out.println("");
    out.println("</body>");
  } // End PrintBody

  /** *****************************************************
   *  Prints the <BODY> of the HTML page
  ********************************************************* */
  private void printResponseBody (
   PrintWriter out, String status, String results){
    out.println("<body>");
    out.println("<p>");
    out.println("A simple example that shows entries from on a database");
    out.println("</p>");
    out.println("");
    out.println(status);
    out.println("");
    out.println(results);
    out.println("");
    out.println("</body>");
  } // End PrintBody


  /** *****************************************************
   *  Prints the bottom of the HTML page.
  ********************************************************* */
  private void printTail (PrintWriter out)
  {
     out.println("");
     out.println("</html>");
  } // End PrintTail

}  // End twoButtons
