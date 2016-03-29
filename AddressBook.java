package version1;

import version1.*;
import java.nio.file.*;
import java.util.stream.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.sql.*;

public class AddressBook{
private static AddressBook application;
private String fileName; 
private List<AddressBookEntry> listOfAddressRecords;
private AddressBookEntry abe;
private Connection connection;

public static void main(String[] args){
	System.out.println("Welcome to the AddressBook application");
	application = new AddressBook();
	application.runApp();
	application.writeToFile();
}

public void runApp(){	
	
	listOfAddressRecords = application.connectToDatabase("jdbc:mysql://localhost/addressbook");
	Scanner myScanner = new Scanner(System.in);
	
	String input = "";
	do{
		System.out.print("Enter S to Search or L to list all entries(E to exit:)");
		input = myScanner.nextLine();
		switch(input){
			case "S":
			case "s":
				openSearchDialogue();
				break;
			case "L":
			case "l":
				openListDialogue();
				break;
			default:
				break;
		}
	}while(!(input.equals("E")));
}

public void openSearchDialogue(){
	System.out.print("Search for name: ");
	Scanner s = new Scanner(System.in);
	String name = s.nextLine();
	String address;
	String email;
	String zipCode;
	String phoneNumber;
	
	Collections.sort(listOfAddressRecords);
	AddressBookEntry d = new AddressBookEntry(name);
	int index = (Collections.binarySearch(listOfAddressRecords,d));
	if(index >=0){
		System.out.println("Record found - Full details of "+name+":");
		d=listOfAddressRecords.get(index);
		System.out.println(d);
		String input = null;
		do{
		System.out.println("Do you want to (E)dit or (D)elete this record");
		 input = s.nextLine();
		
			switch(input){
				case "E":
					System.out.println("Enter address: ");
					d.setAddress(s.nextLine());
					System.out.print("Enter phone number: ");
					d.setPhoneNumber(s.nextLine());
					s.nextLine();
					System.out.print("Enter email addess: ");
					d.setEmail(s.nextLine());
					System.out.print("Enter zipCode: ");
					d.setZipCode(s.nextLine());
					System.out.println(name +" updated");
					System.out.println(input);
					break;
				case "D":
					listOfAddressRecords.remove(index);
					System.out.println("Item removed");
					break;
				}
		}while (!(input.equals("E")||input.equals("D")));
	}
	else{
		System.out.println("Record with name "+name+" not found. Do you want to create it Y/N");
		if(s.nextLine().equals("Y")){
			System.out.println("Enter address: ");
			address = s.nextLine();
			System.out.print("Enter phone number: ");
			phoneNumber = s.nextLine();
			s.nextLine();
			System.out.print("Enter email addess: ");
			email = s.nextLine();
			System.out.print("Enter zipCode: ");
			zipCode= s.nextLine();
			listOfAddressRecords.add(new AddressBookEntry(name,address,phoneNumber,email,zipCode));
			
		}
		
	}

}
	

public void openListDialogue(){
	String input= null;
	Scanner s = new Scanner(System.in);
	do{
	System.out.print("Do you want to list Address Book entries by (N)ame or by (Z)ip Code: ");
	input = s.nextLine();
	if(input.equals("N")){
		Collections.sort(listOfAddressRecords);
		System.out.println(listOfAddressRecords);
		}
	if(input.equals("Z")){
		Collections.sort(listOfAddressRecords,new AddressBookEntry("name"));
		System.out.println(listOfAddressRecords);
		}
	}while(input.equals("N")||input.equals("Z"));
}

public List<AddressBookEntry> connectToDatabase(String connectionURL){
PreparedStatement pstmt = null;
ArrayList<AddressBookEntry> recordList = new ArrayList<AddressBookEntry>();
                
	
	try{
		connection=DriverManager.getConnection(connectionURL,"root","Wisdom20");
		System.out.println("Should be connected to "+connectionURL +" now.");	
		pstmt = connection.prepareStatement("select * from addressbookentries");
		ResultSet rs = pstmt.executeQuery();	
		String name = null;
		String address = null;
		String phoneNumber = null;
		String email = null;
		String zipCode = null;
		
		while(rs.next()){
			name = rs.getString(1);
			address = rs.getString(2);
			phoneNumber = rs.getString(3);
			email = rs.getString(4);
			zipCode = rs.getString(5);
			recordList.add(new AddressBookEntry(name,address,phoneNumber,email,zipCode));
		}
	
	}
	catch(SQLException sqlException){
		System.out.println("Error connecting to database URL: "+connectionURL);
		sqlException.printStackTrace();
		}
	System.out.println(recordList);
	return recordList;
}


public void writeToFile()  {
	
}
	

}// end of class


