package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	public static Connection getConnection(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost/mahoamatkhau","root","");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	
}
