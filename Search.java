import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Search")
public class Search extends HttpServlet
{
   private static final long serialVersionUID = 1L;
   
   public Search()
   {
      super();
   }
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      String keyword = request.getParameter("keyword");
      search(keyword, response);
   }
   
   void search(String keyword, HttpServletResponse response) throws IOException
   {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " +
            "transitional//en\">\n";
      out.println(docType +
            "<html>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body bgcolor=\"#f0f0f0\">\n" +
            "<h1 align=\"center\">" + title + "</h1>\n");
      Connection connection = null;
      PreparedStatement preparedStatement = null;
      String sum = "SELECT SUM(Amount) FROM CheckBookTable";
      PreparedStatement sumStatement = null;
      
      try
      {
         DBConnector.getDBConnection();
         connection = DBConnector.connection;
         
         if (keyword.isEmpty())
         {
            String selectSQL = "SELECT * FROM CheckBookTable";
            preparedStatement = connection.prepareStatement(selectSQL);
            System.out.println(preparedStatement.toString());
         }
         
         else if (isNum(keyword))
         {
            String selectSQL = "SELECT * FROM CheckBookTable WHERE TransNum = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, keyword);
            System.out.println(preparedStatement.toString());
         }
         
         else if (keyword.equals("+"))
         {
            String selectSQL = "SELECT * FROM CheckBookTable WHERE Amount > 0";
            preparedStatement = connection.prepareStatement(selectSQL);
            System.out.println(preparedStatement.toString());
         }
         
         else if (keyword.equals("-"))
         {
        	 String selectSQL = "SELECT * FROM CheckBookTable WHERE Amount < 0";
             preparedStatement = connection.prepareStatement(selectSQL);
             System.out.println(preparedStatement.toString());
         }
         
         else if (keyword.indexOf('/') != -1)
         {
        	 String selectSQL = "SELECT * FROM CheckBookTable WHERE Date = ?";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, keyword);
             System.out.println(preparedStatement.toString());
         }
         
         else
         {
        	 String selectSQL = "SELECT * FROM CheckBookTable WHERE Location = ?";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, keyword);
             System.out.println(preparedStatement.toString());
         }
         
         ResultSet rs = preparedStatement.executeQuery();
         
         while (rs.next())
         {
            int transNum = rs.getInt("TransNum");
            int amount = rs.getInt("Amount");
            String location = rs.getString("Location").trim();
            String date = rs.getString("Date").trim();
            out.println("Transaction Number: " + transNum + ", ");
            out.println("Amount: " + amount + ", ");
            out.println("Location: " + location + ", ");
            out.println("Date: " + date + "<br>");
         }
         
         out.println("<br>");
         sumStatement = connection.prepareStatement(sum);
         ResultSet ss = sumStatement.executeQuery();
         
         while (ss.next())
         {
        	int total = ss.getInt(1);
        	out.println("Current Account Total: " + total + "<br><br>");
         }
         
         out.println("<a href=/CheckBook/search.html>Search CheckBook</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      }
      
      catch (SQLException se)
      {
         se.printStackTrace();
      }
      
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      finally
      {
    	  try
    	  {
    		  if (preparedStatement != null)
    		  {
    			  preparedStatement.close();
    		  }
    	  }
    	  
    	  catch (SQLException se2)
    	  {
    		  se2.printStackTrace();
    	  }
    	  
    	  try
    	  {
    		  if (connection != null)
    		  {
    			  connection.close();
    		  }
    	  }
    	  
    	  catch (SQLException se)
    	  {
    		  se.printStackTrace();
    	  }
      }
   }
   
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      doGet(request, response);
   }
   
   public static boolean isNum(String keyword)
   {
	   try
	   {
		   Double.parseDouble(keyword);
		   return true;
	   }
	   catch(NumberFormatException e)
	   {
		   return false;
	   }
   }
}
