/**
Surkuria Fisher
Server.java
Project 5
Program that allows users to connect 
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.InputMismatchException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server 
{		
	
	
	
	public static void main(String[] args) throws IOException
	{  
    final int SBAP_PORT = 8888;
    ServerSocket server = new ServerSocket(SBAP_PORT);
	
    Scanner in;
    PrintWriter out;
    ReentrantLock sLock = new ReentrantLock();
	 
	      System.out.println("Waiting for clients to connect...");
		   while (true)
	      {
			  
	    	  Socket s = server.accept();
	    	  
	    	  System.out.println("Client connected.");	
	    	  	
			 	 Proxy  p =  new Proxy(s, sLock);
			 	 Thread t = new Thread(p);
			 	 t.start();

						   
			 	 }
			}
		}

