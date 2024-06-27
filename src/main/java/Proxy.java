/**
Surkuria Fisher
Proxy.java
Project 5
Program that allows users to select a selection of instruments
*/


import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.locks.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Proxy implements Runnable{
	 Scanner in;
	
	 private Socket s;
	 
	 	ReentrantLock l;
		private String info;
		Statement stat;
		Connection conn;
		ResultSet result;
		PrintWriter out;
		//ReentrantLock l = new ReentrantLock();
		 
	 public Proxy(Socket aSocket, ReentrantLock lock) {
		 s = aSocket;
		 l = lock;
		 
       openDB();
	 }	

		public void dropLoc(Statement stat)
		  {
	 	   try {  
				  stat.execute("DROP TABLE Locations"); 
		      }
			   catch (Exception e){ 
		    //    System.out.println("drop Locations failed"); 
		      }
          }

		public void dropInv(Statement stat)
		  {       
		   	try {  
				  stat.execute("DROP TABLE Inventory"); 
		      }
			   catch (Exception e){ 
		     //   System.out.println("drop Inventory failed"); 
		      } 
        }
		public void dropInst(Statement stat)
		  {
		 	   try {  
				  stat.execute("DROP TABLE Instruments"); 
		      }
			   catch (Exception e){ 
		     //   System.out.println("drop Instruments failed"); 
		      }           
		  }
		
		public void openDB() {
			try {
				
			  System.out.println("start opendb()");
			  SimpleDataSource.init("database.properties");
			      
			  conn = SimpleDataSource.getConnection();
			  stat = conn.createStatement();
           System.out.println("before droptables");
			  dropLoc(stat);
           			  dropInv(stat);
                    			  dropInst(stat);
        //   System.out.println("after droptables");
			  ResultSet r = createInstruments(stat);
         //  System.out.println("after create inst");
			  showResults(r);
                  //    System.out.println("after insts");
			     r =  createLocations(stat);
              			  showResults(r);
                  //    System.out.println("after locations");
			    r =  createInventory(stat);
             			  showResults(r);
                  //    System.out.println("after inventory");
			      
			}catch(Exception e) {
				System.out.println("Database will not open right now");
				e.getMessage();
			}
			
		}
		
		public void closeDB() {
			try {
				out.printf("Closing database"  + "\n"+ " ");
				conn.close();
				
			}catch(Exception e) {
				System.out.println("Problem closing");
				e.getMessage();
				}
		}

	public ResultSet createInstruments(Statement stat) throws Exception
	  {
	         stat.execute("CREATE TABLE Instruments (instName CHAR(12),instNumber INTEGER,cost DOUBLE,descrip CHAR(20))");
	         stat.execute("INSERT INTO Instruments VALUES ('guitar',1,100.0,'yamaha')");
	         stat.execute("INSERT INTO Instruments VALUES ('guitar',2,500.0,'gibson')");
	         stat.execute("INSERT INTO Instruments VALUES ('bass',3,250.0,'fender')");
	         stat.execute("INSERT INTO Instruments VALUES ('keyboard',4,600.0,'roland')");
	         stat.execute("INSERT INTO Instruments VALUES ('keyboard',5,500.0,'alesis')");
	         stat.execute("INSERT INTO Instruments VALUES ('drums',6,1500.0,'ludwig')");
	         stat.execute("INSERT INTO Instruments VALUES ('drums',7,400.0,'yamaha')");
	         ResultSet result = stat.executeQuery("SELECT * FROM Instruments");
	         return result;
	  }

	  public ResultSet createLocations(Statement stat) throws Exception
	  {
	         stat.execute("CREATE TABLE Locations (locName CHAR(12),locNumber INTEGER,address CHAR(50))");
	         stat.execute("INSERT INTO Locations VALUES ('PNS',1,'Pensacola Florida')");
	         stat.execute("INSERT INTO Locations VALUES ('CLT',2,'Charlotte North Carolina')");
	         stat.execute("INSERT INTO Locations VALUES ('DFW',3,'Dallas Fort Worth Texas')");
	         ResultSet result = stat.executeQuery("SELECT * FROM Locations");
	         return result;
	  }

	  public ResultSet createInventory(Statement stat) throws Exception
	  {
	         stat.execute("CREATE TABLE Inventory (iNumber INTEGER,lNumber INTEGER,quantity INTEGER)");
	         stat.execute("INSERT INTO Inventory VALUES (1,1,15)");
	         stat.execute("INSERT INTO Inventory VALUES (1,2,27)");
	         stat.execute("INSERT INTO Inventory VALUES (1,3,20)");
	         stat.execute("INSERT INTO Inventory VALUES (2,1,10)");
	         stat.execute("INSERT INTO Inventory VALUES (2,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (2,3,35)");
	         stat.execute("INSERT INTO Inventory VALUES (3,1,45)");
	         stat.execute("INSERT INTO Inventory VALUES (3,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (3,3,17)");
	         stat.execute("INSERT INTO Inventory VALUES (4,1,28)");
	         stat.execute("INSERT INTO Inventory VALUES (4,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (4,3,16)");
	         stat.execute("INSERT INTO Inventory VALUES (5,1,28)");
	         stat.execute("INSERT INTO Inventory VALUES (5,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (5,3,1)");
	         stat.execute("INSERT INTO Inventory VALUES (6,1,2)");
	         stat.execute("INSERT INTO Inventory VALUES (6,2,10)");
	         stat.execute("INSERT INTO Inventory VALUES (6,3,16)");
	         stat.execute("INSERT INTO Inventory VALUES (7,1,16)");
	         stat.execute("INSERT INTO Inventory VALUES (7,2,4)");
	         stat.execute("INSERT INTO Inventory VALUES (7,3,12)");     
	         ResultSet result = stat.executeQuery("SELECT * FROM Inventory");
	         return result;
	  }
		public void showResults(ResultSet result) {
			//	System.out.println( "showResults()");
			try {
				ResultSetMetaData rsm = result.getMetaData();
				int cols = rsm.getColumnCount();
				  while(result.next())
				  {
				    for(int i = 1; i <= cols; i++)
		           System.out.print(result.getString(i)+" ");
		         System.out.println("");      
				  }
			}catch(SQLException s ) {
				System.out.println("Could not get result");
			
		}
				
	}
		public String getInfo() {
			return info;
		} 
	 public void run() {
		 try {
			 
			 try {
				 in = new Scanner(s.getInputStream());		 		
				 out = new PrintWriter(s.getOutputStream());
				 doService();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 finally {
				 s.close();
			 }
			 }catch(IOException e) {
				 e.printStackTrace();
			 }
		 }	 
	public void doService() throws IOException, SQLException {
		if(!in.hasNext()) {
			return;
		}
		String output = in.nextLine();
		createQ(output);
	}	
	public void createQ(String output) throws IOException, SQLException {
			String order = "";	
	//	String[]	task = options.split(" ");
		String [] split = output.split(" ");
		String all = "";

		order = " SELECT Instruments.instName, Instruments.descrip, Instruments.cost, Inventory.quantity, Locations.address "
				+ "FROM Instruments " 
				+ "JOIN INVENTORY "
					+ "ON Instruments.instNumber = Inventory.iNumber "
				+ "JOIN Locations "
					+ "ON Locations.locNumber = Inventory.lNumber"
					+	 " WHERE Instruments.cost < " + split[2];
							
						if(!split[0].contains("all")) {
				order	+= " AND Instruments.instName = '" + split[0] + "' "; 
						}
						if(!split[1].contains("all")){
				order	+= " AND Instruments.descrip = '" + split[1] + "' ";
						}
						if(!split[3].contains("all")) {
				order	+= " AND Locations.locName = '" + split[3] + "' ";
						}
					System.out.println(order);
					l.lock();
		result = stat.executeQuery(order);	
					l.unlock();
			ResultSetMetaData rsm = result.getMetaData();
			int cols = rsm.getColumnCount();
			ArrayList<String> rList = new ArrayList<>();
			while(result.next()) {
				for(int i = 1; i<=cols; i++) {
					rList.add(result.getString(i) + "\t");
					System.out.println(result.getString(i) + " ");
				}
				rList.add("\n");
				System.out.println();
			}			
			String str = " ";
			for(int i = 0; i<rList.size(); i++) {
				str += rList.get(i);
			}
			out.println(str);
			out.flush();				
		}
}
