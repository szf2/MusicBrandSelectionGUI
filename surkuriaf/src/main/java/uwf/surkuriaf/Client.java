package uwf.surkuriaf;

/**
Surkuria Fisher
Client.java
Project 5
Program that allows users to visually see the selection catalog
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Optional;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
   import java.io.InputStream;
   import java.io.IOException;
   import java.io.OutputStream;
   import java.io.PrintWriter;
	   import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.net.Socket;
   import java.util.Scanner;


public class Client extends Application{
	
	private TextInputDialog dialog;
	private final String defaultVal = "Default text";
	private static final String titleTxt = "Musical Instrument Lookup";
	final int SBAP_PORT = 8888;


   ChoiceBox<String> instrument;
   ChoiceBox<String> inBrand;
   ChoiceBox<String> wareLocation;
   TextField expenses;

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle(titleTxt);	
		
		// Window label
		instrument = new ChoiceBox<String>();
      Label instrumentLabel = new Label("Instrument Type: ");
      instrument.getItems().addAll("all","guitar","bass","keyboard","drums");
		instrument.getSelectionModel().select(0);
      HBox inHb = new HBox();
		inHb.setAlignment(Pos.CENTER);
		inHb.getChildren().addAll(instrumentLabel,instrument);

      instrument.setOnAction(e -> instrumentSelect(instrument));

		inBrand = new ChoiceBox<String>();
      Label bLabel = new Label("Instrument Brand: ");
      inBrand.getItems().addAll("all","gibson","fender","roland","alesis","ludwig","pearl","yamaha");
      inBrand.getSelectionModel().select(0);
     	HBox bHb = new HBox();
		bHb.setAlignment(Pos.CENTER);
		bHb.getChildren().addAll(bLabel,inBrand);
      
      
      
      expenses = new TextField();
		expenses.setFont(Font.font("Calibri", FontWeight.NORMAL, 12));
      Label cLabel = new Label("Maximum cost: ");
     	HBox cHb = new HBox();
		cHb.setAlignment(Pos.CENTER);
		cHb.getChildren().addAll(cLabel,expenses);      
 
 
 		wareLocation = new ChoiceBox<String>();
      Label wLabel = new Label("Warehouse Location: ");
      wareLocation.getItems().addAll("all","PNS","CLT","DFW");
		wareLocation.getSelectionModel().select(0);
      HBox wareHb = new HBox();
		wareHb.setAlignment(Pos.CENTER);
		wareHb.getChildren().addAll(wLabel,wareLocation);
      
		// Button
		Button textbtn = new Button("Submit Request");
		textbtn.setOnAction(new TextButtonListener());
		HBox buttonHb = new HBox(10);
		buttonHb.setAlignment(Pos.CENTER);
		buttonHb.getChildren().addAll(textbtn);
		
		// Vbox
		VBox vbox = new VBox(30);
		vbox.setPadding(new Insets(25, 25, 25, 25));
		vbox.getChildren().addAll(inHb, bHb, cHb, wareHb, buttonHb);
		
		// Scene
		Scene scene = new Scene(vbox, 500, 350); // w x h
		primaryStage.setScene(scene);
		primaryStage.show();
	}
   
   public void instrumentSelect(ChoiceBox<String> i)
   {
     System.out.println("instrument selection was choosen");
     branding();
   }
   
    public void branding()
   {
     System.out.println("brand selection was choosen");
     inBrand.getItems().clear();
     String st = instrument.getValue();
     if(st.equals("guitar"))
       inBrand.getItems().addAll("all","yamaha","gibson");
     else if(st.equals("keyboard"))
       inBrand.getItems().addAll("all","roland","alesis");
     else if(st.equals("bass"))
       inBrand.getItems().addAll("all","fender");
     else if(st.equals("drums"))
       inBrand.getItems().addAll("all","ludwig","pearl");
     inBrand.getSelectionModel().select(0);
   } 
   
   
	
	private void sendRequest() {
		
      String type = instrument.getValue();
      String brand = inBrand.getValue();
      String theCost = expenses.getText();
      if(theCost.length() == 0)
        theCost = "10000000000000";
      String ware = wareLocation.getValue();
      String request = type + " " + brand + " " + theCost + " " + ware+"\n";
		System.out.println(  request );
      try
      {

		   
       Socket   s = new Socket("localhost", SBAP_PORT);
         InputStream instream = s.getInputStream();
         OutputStream outstream = s.getOutputStream();
         Scanner in = new Scanner(instream);
         PrintWriter out = new PrintWriter(outstream); 

         out.print(request);
         out.flush();
         String response = "";
         int lines = 0;
         while(in.hasNext())
         {
           response += in.nextLine();
           response += "\n";
           lines++;
         }
         System.out.println(response);
			
         Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle(titleTxt);
         a.setResizable(true);
         a.getDialogPane().setPrefSize(500, 150 + 20*lines);
         a.setHeaderText("Results");
			a.setContentText(response);
      //   System.out.println("***** height = " + a.getHeight() + 
       //                     " width = " + a.getWidth());
			a.show();
      }
      catch(Exception e) {
    	  
      }	
   }
	
	private class TextButtonListener implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			sendRequest();
		}
	}
	
	public static void main(String [] args) throws IOException {

		
		Application.launch(args);
	}

}


