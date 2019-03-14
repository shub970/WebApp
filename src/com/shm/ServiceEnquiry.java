package com.shm;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shm.verify.VerifyCaptcha;


@SuppressWarnings("serial")
public class ServiceEnquiry extends HttpServlet
{	
	private Connection con = null;
	private String dbUsername = "root";
	private String dbPassword = "root@123";
	
	private void startUp() {
		 
            String dbDriver = "com.mysql.jdbc.Driver";
            String dbURL = "jdbc:mysql://localhost:3306/";
            // Database name to access
            String dbName = "users";
            try {
				Class.forName(dbDriver); 
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				this.con = DriverManager.getConnection(dbURL+dbName+"?autoReconnect=true&useSSL=false", dbUsername, dbPassword);
				System.out.println(this.con);
				Statement st = con.createStatement();
				st.execute("USE users;");
			} catch (ClassNotFoundException e) {	
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
            System.out.println("here");
	}
	
	private int getLastRecord() throws SQLException{
	       
        Statement st = this.con.createStatement();
        String query = "SELECT COUNT(name) FROM new_users;";
        ResultSet res = st.executeQuery(query);
        int last_record = -1;
        while (res.next()){
            last_record = res.getInt(1);
        }
        return last_record;
	}

	
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		this.startUp(); 
			
		int a = Integer.parseInt(req.getParameter("num1"));
		int b = Integer.parseInt(req.getParameter("num2"));
		String user_name = req.getParameter("user");
		String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
		System.out.println(gRecaptchaResponse);
		boolean verify = VerifyCaptcha.verifyuser(gRecaptchaResponse);
		System.out.println(verify);
		int result = a+b;
		
		String sql_query = "INSERT INTO new_users(name, number) VALUES('"+user_name+"', "+result+");";
		Statement st;
		int last_record = -1;
		String message = null;
		String warning = "";
		
		if (verify==false) 
		{
			warning = "Captcha cannot be verified.";
			message = "TRY Again!";
		}
		else {
			try {
				st = this.con.createStatement(); 
				last_record = this.getLastRecord();
				st.executeUpdate(sql_query);
				last_record++;
				message = ("This is Enquiry number "+last_record+". The result of the enquiry is "+result+".\nThank you!");
			} catch (SQLException e) {
				message = ("Server Down. Please Try Again!");
			}
		}	
		
		req.setAttribute("message", message);
		req.setAttribute("warning", warning);
		req.getRequestDispatcher("WEB-INF/response_page.jsp").forward(req, res);
		
	}
}
