package servlet;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "MyServlet",
        urlPatterns = {"/hello"}
    )

public class HelloServlet extends HttpServlet
{
  @Override
   protected void doGet  (HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException
   {

        res.setContentType ("text/html");
        PrintWriter out = res.getWriter ();

        out.println ("<HTML>");
        out.println ("<HEAD>");
        out.println ("<TITLE>A simple servlet program</TITLE>");
        out.println ("</HEAD>");

        out.println ("<BODY>");
        out.println ("<CENTER>");

        out.println (" <B>Hello!</B><BR> <!--  English -->");
        out.println (" <B>Alo!</B><BR> <!--  Portuguese -->");
        out.println (" <B>Anyong haseyo!</B><BR> <!--  Korean -->");
        out.println (" <B>Apa Kabar!</B><BR> <!--  Indonesian -->");
        out.println (" <B>Ave!</B><BR> <!--  Latin -->");
        out.println (" <B>Bon jour!</B><BR> <!--  French -->");
        out.println (" <B>Ciao!</B><BR> <!--  Italian -->");
        out.println (" <B>Hajur!</B><BR> <!--  Nepali (India) -->");
        out.println (" <B>Hallo!</B><BR> <!--  German -->");
        out.println (" <B>Hej!</B><BR> <!--  Swedish -->");
        out.println (" <B>Hei!</B><BR> <!--  Norwegian -->");
        out.println (" <B>Hola!</B><BR> <!--  Spanish -->");
        out.println (" <B>Kaise ho!</B><BR> <!--  Hindi (depending on who you believe) -->");
        out.println (" <B>Kem Chho, Kem Che!</B><BR> <!--  Gujurati (India) -->");
        out.println (" <B>Ki Khobor!</B><BR> <!--  Bengali -->");
        out.println (" <B>Marhaba!</B><BR> <!--  Arabic -->");
        out.println (" <B>Moin Moin!</B><BR> <!--  German, Hamburg -->");
        out.println (" <B>Moni!</B><BR> <!--  Chichewa -->");
        out.println (" <B>Namaskaram!</B><BR> <!--  Telugu (India) -->");
        out.println (" <B>Namskar!</B><BR> <!--  Hindi -->");
        // Why do I keep getting different words in Hindi?
        // out.println (" <B>Namaste!</B><BR> <!--  Hindi -->");
        // out.println (" <B>Namastar!</B><BR> <!--  Hindi -->");
        out.println (" <B>Konnichiwa!</B><BR> <!--  Japanese -->");
        out.println (" <B>Manh gioi!</B><BR> <!--  Vietnamese -->");
        out.println (" <B>Marhabai!</B><BR> <!--  Arab -->");
        out.println (" <B>Ni hao!</B><BR> <!--  Chinese -->");
        out.println (" <B>Shalom!</B><BR> <!--  Hebrew -->");
        out.println (" <B>Sallam!</B><BR> <!--  Persian -->");
        out.println (" <B>Sat-Sri-Akal!</B><BR> <!--  Punjabi (India) -->");
        out.println (" <B>Strasvedte!</B><BR> <!--  Russian -->");
        out.println (" <B>Yah-shimu-siz!</B><BR> <!--  Uighur -->");
        out.println (" <B>Zhao  Shen!</B><BR> <!-- Cantonese -->");

        out.println ("<P>");
        out.println ("For a longer listing of \"hellos\" to the world, go see");
        out.println ("<A HREF=\"http://www.ipl.org/youth/hello/\">http://www.ipl.org/youth/hello/</A>");

        out.println ("</CENTER>");
        out.println ("</BODY>");

        out.println ("</HTML>");
        out.flush();

        out.close ();

    }
}
