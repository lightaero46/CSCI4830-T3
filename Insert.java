/**
 * @file Insert.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Insert")
public class Insert extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   public Insert()
   {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response)
		   throws ServletException, IOException
   {
      String amount = request.getParameter("amount");
      String location = request.getParameter("location");
      String date = request.getParameter("date");
      Connection connection = null;
      String insertSql = " INSERT INTO CheckBookTable (TransNum, Amount, Location, Date) values (default, ?, ?, ?)";
      String sum = "SELECT SUM(Amount) FROM CheckBookTable";
      PreparedStatement sumStatement = null;
      int total = 0;
      
      try
      {
         DBConnector.getDBConnection();
         connection = DBConnector.connection;
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         int modAmount = Integer.parseInt(amount);
         preparedStmt.setInt(1, modAmount);
         preparedStmt.setString(2, location);
         preparedStmt.setString(3, date);
         preparedStmt.execute();
         sumStatement = connection.prepareStatement(sum);
         ResultSet ss = sumStatement.executeQuery();
         
         while (ss.next())
         {
        	total = ss.getInt(1);
         }
         connection.close();
      }
      
      catch (Exception e)
      {
         e.printStackTrace();
      }

      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Insert Data to DB table";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType +
            "<html>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body bgcolor=\"#f0f0f0\">\n" +
            "<h2 align=\"center\">" + title + "</h2>\n" +
            "<ul>\n" +
            "  <li><b>Amount</b>: " + amount + "\n" +
            "  <li><b>Location</b>: " + location + "\n" +
            "  <li><b>Date</b>: " + date + "\n" +
            "</ul>\n");
      out.println("<br>New Account Total: " + total + "<br><br>");
      out.println("<a href=/CheckBook/insert.html>Insert Data</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
		   throws ServletException, IOException
   {
      doGet(request, response);
   }

}
