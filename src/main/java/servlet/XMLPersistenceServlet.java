package servlet;
// Written by David Gonzalez, April 2020
// Modified by Jeff Offutt
// Built to deploy in github with Heroku
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

@WebServlet(name = "XMLPersistence", urlPatterns = {"/xml"})
public class XMLPersistenceServlet extends HttpServlet{
  static enum Data {NAME, AGE, ENTRY, ENTRIES};

  static String RESOURCE_FILE = "entries.xml";

  static String Domain  = "";
  static String Path    = "/";
  static String Servlet = "xml";

  // Button labels
  static String OperationAdd = "Add";

  public class Entry {
    String name;
    Integer age;
  }

  List<Entry> entries;

  public class EntryManager {
    private String filePath = null;
    private XMLEventFactory eventFactory = null;
    private XMLEvent LINE_END = null;
    private XMLEvent LINE_TAB = null;
    private XMLEvent ENTRIES_START = null;
    private XMLEvent ENTRIES_END = null;
    private XMLEvent ENTRY_START = null;
    private XMLEvent ENTRY_END = null;


    public EntryManager(){
      eventFactory = XMLEventFactory.newInstance();
      LINE_END = eventFactory.createDTD("\n");
      LINE_TAB = eventFactory.createDTD("\t");

      ENTRIES_START = eventFactory.createStartElement(
        "","", Data.ENTRIES.name());
      ENTRIES_END =eventFactory.createEndElement(
        "", "", Data.ENTRIES.name());
      ENTRY_START = eventFactory.createStartElement(
        "","", Data.ENTRY.name());
      ENTRY_END =eventFactory.createEndElement(
        "", "", Data.ENTRY.name());
    }
    public void setFilePath(String filePath) {
      this.filePath = filePath;
    }

    public List<Entry> save(String name, Integer age)
      throws FileNotFoundException, XMLStreamException{
      List<Entry> entries = getAll();
      Entry newEntry = new Entry();
      newEntry.name = name;
      newEntry.age = age;
      entries.add(newEntry);

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      XMLEventWriter eventWriter = outputFactory
              .createXMLEventWriter(new FileOutputStream(filePath));

      eventWriter.add(eventFactory.createStartDocument());
      eventWriter.add(LINE_END);

      eventWriter.add(ENTRIES_START);
      eventWriter.add(LINE_END);

      for(Entry entry: entries){
        addEntry(eventWriter, entry.name, entry.age);
      }

      eventWriter.add(ENTRIES_END);
      eventWriter.add(LINE_END);

      eventWriter.add(eventFactory.createEndDocument());
      eventWriter.close();
      return entries;
    }

    private void addEntry(XMLEventWriter eventWriter, String name,
            Integer age) throws XMLStreamException {
        eventWriter.add(ENTRY_START);
        eventWriter.add(LINE_END);
        createNode(eventWriter, Data.NAME.name(), name);
        createNode(eventWriter, Data.AGE.name(), String.valueOf(age));
        eventWriter.add(ENTRY_END);
        eventWriter.add(LINE_END);

    }

    private void createNode(XMLEventWriter eventWriter, String name,
          String value) throws XMLStreamException {
      StartElement sElement = eventFactory.createStartElement("", "", name);
      eventWriter.add(LINE_TAB);
      eventWriter.add(sElement);

      Characters characters = eventFactory.createCharacters(value);
      eventWriter.add(characters);

      EndElement eElement = eventFactory.createEndElement("", "", name);
      eventWriter.add(eElement);
      eventWriter.add(LINE_END);

    }

    private List<Entry> getAll(){
      List entries = new ArrayList();

      try{

        File file = new File(filePath);
        if(!file.exists()){
          return entries;
        }

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream(file);
        XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

        Entry entry = null;
        while (eventReader.hasNext()) {
          // <ENTRIES> not needed for the example
          XMLEvent event = eventReader.nextEvent();

          if (event.isStartElement()) {
              StartElement startElement = event.asStartElement();
              if (startElement.getName().getLocalPart()
                .equals(Data.ENTRY.name())) {
                  entry = new Entry();
              }

              if (event.isStartElement()) {
                  if (event.asStartElement().getName().getLocalPart()
                          .equals(Data.NAME.name())) {
                      event = eventReader.nextEvent();
                      entry.name =event.asCharacters().getData();
                      continue;
                  }
              }
              if (event.asStartElement().getName().getLocalPart()
                      .equals(Data.AGE.name())) {
                  event = eventReader.nextEvent();
                  entry.age =Integer.parseInt(event.asCharacters().getData());
                  continue;
              }
          }

          if (event.isEndElement()) {
              EndElement endElement = event.asEndElement();
              if (endElement.getName().getLocalPart()
              .equals(Data.ENTRY.name())) {
                  entries.add(entry);
              }
          }

        }

      }catch (FileNotFoundException e) {
        e.printStackTrace();
      }catch (XMLStreamException e) {
        e.printStackTrace();
      }catch(IOException ioException){
        ioException.printStackTrace();
      }

      return entries;
    }

  public String getAllAsHTMLTable(List<Entry> entries){
    StringBuilder htmlOut = new StringBuilder("<table>");
    htmlOut.append("<tr><th>Name</th><th>Age</th></tr>");
    if(entries == null || entries.size() == 0){
      htmlOut.append("<tr><td>No entries yet.</td></tr>");
    }else{
      for(Entry entry: entries){
         htmlOut.append("<tr><td>"+entry.name+"</td><td>"+entry.age+"</td></tr>");
      }
    }
    htmlOut.append("</table>");
    return htmlOut.toString();
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
     String name = request.getParameter(Data.NAME.name());
     String rawAge = request.getParameter(Data.AGE.name());
     Integer age = null;

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
            rawAge = "";
          }
     }

     response.setContentType("text/html");
     PrintWriter out = response.getWriter();

     if (error.length() == 0){
       EntryManager entryManager = new EntryManager();
       entryManager.setFilePath(RESOURCE_FILE);
       List<Entry> newEntries= null;
       try{
         newEntries=entryManager.save(name, age);
       }catch(FileNotFoundException e){
         e.printStackTrace();
          error+= "<li>Could not save entry.</li>";
       }
       catch(XMLStreamException e){
         e.printStackTrace();
          error+= "<li>Could not save entry.</li>";
       }


       printHead(out);
       if(newEntries ==  null){
         error+= "<li>Could not save entry.</li>";
         printBody(out, name, rawAge, error);
       }else{
         printResponseBody(out, entryManager.getAllAsHTMLTable(newEntries));
       }

       printTail(out);
     }else{
       printHead(out);
       printBody(out, name, rawAge, error);
       printTail(out);
     }


  }

  /** *****************************************************
   *  Overrides HttpServlet's doGet().
   *  Prints an HTML page with a blank form.
  ********************************************************* */
  @Override
  public void doGet (HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException{
     response.setContentType("text/html");
     PrintWriter out = response.getWriter();
     printHead(out);
     printBody(out, "", "", "");
     printTail(out);
  }

  /** *****************************************************
   *  Prints the <head> of the HTML page, no <body>.
  ********************************************************* */
  private void printHead (PrintWriter out){
     out.println("<html>");
     out.println("");
     out.println("<head>");
     out.println("<title>XML File Persistence Example</title>");
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
      "A simple example that demonstrates how to persist data to a XML file");
    out.println("</p>");

    if(error != null && error.length() > 0){
      out.println(
      "<p style=\"color:red;\">Please correct the following and resubmit.</p>"
        );
      out.println("<ol>");
      out.println(error);
      out.println("</ol>");
    }

    out.print  ("<form name=\"persist2file\" method=\"post\"");
    out.println(" action=\""+Domain+Path+Servlet+"\">");
    out.println("");
    out.println(" <table>");
    out.println("  <tr>");
    out.println("   <td>Name:</td>");
    out.println("   <td><input type=\"text\" name=\""+Data.NAME.name()
    +"\" value=\""+name+"\" size=30 required></td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("   <td>Age:</td>");
    out.println("   <td><input type=\"text\"  name=\""+Data.AGE.name()
    +"\" oninput=\"this.value=this.value.replace(/[^0-9]/g,'');\" value=\""
    +age+"\" size=3 required></td>");
    out.println("  </tr>");
    out.println(" </table>");
    out.println(" <br>");
    out.println(" <br>");
    out.println(" <input type=\"submit\" value=\"" + OperationAdd
    + "\" name=\"Operation\">");
    out.println(" <input type=\"reset\" value=\"Reset\" name=\"reset\">");
    out.println("</form>");
    out.println("");
    out.println("</body>");
  }

  /** *****************************************************
   *  Prints the <BODY> of the HTML page with persisted entries
  ********************************************************* */
  private void printResponseBody (PrintWriter out, String tableString){
    out.println("<body>");
    out.println("<p>");
    out.println("A simple example that shows entries persisted on a JSON file");
    out.println("</p>");
    out.println("");
    out.println(tableString);
    out.println("");
    out.println("</body>");
  }

  /** *****************************************************
   *  Prints the bottom of the HTML page.
  ********************************************************* */
  private void printTail (PrintWriter out){
     out.println("");
     out.println("</html>");
  }
}
