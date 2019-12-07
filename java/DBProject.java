/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of DBProject
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public DBProject (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end DBProject

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            DBProject.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      DBProject esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the DBProject object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new DBProject (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking"); 
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range"); 
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

            switch (readChoice()){
				   case 1: addCustomer(esql); break;
				   case 2: addRoom(esql); break;
				   case 3: addMaintenanceCompany(esql); break;
				   case 4: addRepair(esql); break;
				   case 5: bookRoom(esql); break;
				   case 6: assignHouseCleaningToRoom(esql); break;
				   case 7: repairRequest(esql); break;
				   case 8: numberOfAvailableRooms(esql); break;
				   case 9: numberOfBookedRooms(esql); break;
				   case 10: listHotelRoomBookingsForAWeek(esql); break;
				   case 11: topKHighestRoomPriceForADateRange(esql); break;
				   case 12: topKHighestPriceBookingsForACustomer(esql); break;
				   case 13: totalCostForCustomer(esql); break;
				   case 14: listRepairsMade(esql); break;
				   case 15: topKMaintenanceCompany(esql); break;
				   case 16: numberOfRepairsForEachRoomPerYear(esql); break;
				   case 17: keepon = false; break;
				   default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
   
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   
   public static void addCustomer(DBProject esql){
	
 int customerID = 0;
String checkID;

	checkID = "SELECT customerID FROM Customer";
	try { 
		customerID = esql.executeQuery(checkID);
		//reqID = reqID + 1;
	}
	catch (Exception e) {
		System.err.println(e.getMessage());
}


	/* do {
		System.out.println("Input your Customer ID Number: ");
		try {
			checkID = in.readLine();
			customerID = Integer.parseInt(checkID);
			if (checkID.length() <= 0) {
				throw new RuntimeException("Custmor ID can't be empty!");
}
break;
		}catch (Exception e){
			System.out.println("Invaild input!");
			continue;
		}
	}while(true);

*/

  String fName;
	do {
		System.out.println("Input your First Name: ");
		try {
			fName = in.readLine();
			if (fName.length() <= 0 || fName.length() > 30) {
				throw new RuntimeException("First Names can't be null or longer than 30 characters");
}
break;
		}catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);


 String lName;

	do {
		System.out.println("Input your Last Name: ");
		try {
			lName = in.readLine();
			if (lName.length() <= 0 || lName.length() > 30) {
				throw new RuntimeException("Last Names can't be null or longer than 30 characters");
}
break;
		}catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);

 String Address;

	do {
		System.out.println("Input your Address: ");
		try {
			Address = in.readLine();
			if (Address.length() <= 0) {
				throw new RuntimeException("Your address will be placed as None");
}
break;
		}catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);

Date DOB;
String temp;
//SimpleDateFormatter dformat = SimpleDateFormatter.ofPattern("dd-MM-yyyy");

	do {
		System.out.println("Input your date of birth: ");
		try {
			temp = in.readLine();
			 DOB = new SimpleDateFormat("MM/DD/YY").parse(temp);
			break;

		}catch (Exception e){
			System.out.println("Invaild input!");
			continue;
		}
	}while(true);

 int phNo = 800;
String tempNo;
	//do {
	//	System.out.println("Input your phone number: with no / () ");
	//	try {
	//		tempNo = in.readLine();
	//		
	//		phNo = Integer.parseInt(tempNo);
	//		
	//		if (tempNo.length() <= 0) {
	//			throw new RuntimeException("Phone Number can't be empty!");
//}
//			break;
//		}catch (Exception e){
//			System.out.println("Invaild input!");
//			continue;
//		}
//	}while(true);

 String GenderType;

	do {
		System.out.println("Input your Gender: Male, Female, Other ");
		try {
			GenderType = in.readLine();
			if (GenderType.length() == 0) {
				throw new RuntimeException("Invaild");
}
break;
		}catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);

	try {
		String query = "INSERT INTO CUSTOMER (customerID, fName, lName, Address, phNo, DOB, gender) VALUES (" + customerID + ", \'" + fName + "\',\'" + lName + "\',\'"  + Address + "\',\'" + phNo + "\',\'"  + DOB + "\',\'"  + GenderType + "\');" ;

		esql.executeUpdate(query);
	}catch (Exception e) {  
				System.err.println (e.getMessage());
}

   };//end addCustomer

   public static void addRoom(DBProject esql){
	  // Given room details add the room in the DB
      // Your code goes here.

      // ...
   }//end addRoom

   public static void addMaintenanceCompany(DBProject esql){
      // Given maintenance Company details add the maintenance company in the DB
      // ...
int cmpID = 0;
String checkID;

	checkID = "SELECT cmpID FROM MaintenanceCompany";
	try { 
		cmpID = esql.executeQuery(checkID);
		//reqID = reqID + 1;
	}
	catch (Exception e) {
		System.err.println(e.getMessage());
}


/*	do {
		System.out.println("Input your Company ID Number: ");
		try {
			checkID = in.readLine();
			cmpID = Integer.parseInt(checkID);
			if (checkID.length() <= 0) {
				throw new RuntimeException("Company ID can't be empty!");
}
break;
		}catch (Exception e){
			System.out.println("Invaild input!");
			continue;
		}
	}while(true);
*/


  String name;
	do {
		System.out.println("Input your Company Name: ");
		try {
			name = in.readLine();
			if (name.length() <= 0 || name.length() > 30) {
				throw new RuntimeException("Company Names can't be null or longer than 30 characters");
}
break;
		}catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);


 String check;
boolean isCertified;

	do {
		System.out.println("Is your Company Certified?: ");
		try {
			check = in.readLine();
			isCertified = Boolean.parseBoolean(check);
			if (check.length() <= 0) {
				throw new RuntimeException("Can't be Null/Empty");
}
break;
		}catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);

 String Address;

	do {
		System.out.println("Input your Address: ");
		try {
			Address = in.readLine();
			if (Address.length() <= 0) {
				throw new RuntimeException("Your address will be placed as None");
}
break;
		}catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);



	try {
		String query = "INSERT INTO MaintenanceCompany (cmpID, name, address, isCertified) VALUES (" + cmpID + ", \'" + name + "\',\'" + Address + "\',\'" + isCertified + "\');" ;

		esql.executeUpdate(query);
	}catch (Exception e) {  
				System.err.println (e.getMessage());
}

      // ...
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){
	  // Given repair details add repair in the DB
      // Your code goes here.
      // ...
      // ...
   }//end addRepair

   public static void bookRoom(DBProject esql){
	  // Given hotelID, roomNo and customer Name create a booking in the DB 
      // Your code goes here.
      // ...
        int bID = 0;
        String checkbID;

	checkbID = "SELECT bID FROM Booking";
	try { 
		bID = esql.executeQuery(checkbID);
		bID = bID + 1;
	}
	catch (Exception e) {
		System.err.println(e.getMessage());
}

        /*do {
            System.out.println("Input your Booking ID Number: ");
            try {
                checkbID = in.readLine();
                bID = Integer.parseInt(checkbID);
                if (checkbID.length() <= 0) {
                    throw new RuntimeException("Booking ID can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invalid input!");
                continue;
            }
        }while(true);
*/
        int customer;
        String checkC;

        do {
            System.out.println("Input your Customer ID Number: ");
            try {
                checkC = in.readLine();
                customer = Integer.parseInt(checkC);
                if (checkC.length() <= 0) {
                    throw new RuntimeException("Customer ID can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invalid input!");
                continue;
            }
        }while(true);

        int hotelID;
        String checkhID;

        do {
            System.out.println("Input your Hotel ID Number: ");
            try {
                checkhID = in.readLine();
                hotelID = Integer.parseInt(checkhID);
                if (checkhID.length() <= 0) {
                    throw new RuntimeException("Hotel ID can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invalid input!");
                continue;
            }
        }while(true);

        int roomNo;
        String checkR;

        do {
            System.out.println("Input your Room Number: ");
            try {
                checkR = in.readLine();
                roomNo = Integer.parseInt(checkR);
                if (checkR.length() <= 0) {
                    throw new RuntimeException("Room Number can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invaild input!");
                continue;
            }
        }while(true);



        Date bookingDate;
        String tempb;
//SimpleDateFormatter dformat = SimpleDateFormatter.ofPattern("dd-MM-yyyy");

        do {
            System.out.println("Input your booking date: ");
            try {
                tempb = in.readLine();
                bookingDate = new SimpleDateFormat("MM/DD/YY").parse(tempb);
                break;

            }catch (Exception e){
                System.out.println("Invalid input!");
                continue;
            }
        }while(true);

        int noOfPeople;
        String checkN;

        do {
            System.out.println("Input the number of people: ");
            try {
                checkN = in.readLine();
                noOfPeople = Integer.parseInt(checkN);
                if (checkN.length() <= 0) {
                    throw new RuntimeException("Number can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invaild input!");
                continue;
            }
        }while(true);


        int price;
        String checkP;

        do {
            System.out.println("Input your Price: $ ");
            try {
                checkP = in.readLine();
                price = Integer.parseInt(checkP);
                if (checkP.length() <= 0) {
                    throw new RuntimeException("Price can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invaild input!");
                continue;
            }
        }while(true);


        try {
            String query = "INSERT INTO Booking (bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price) VALUES (" + bID + ", \'" + customer + "\',\'" + hotelID + "\',\'" + roomNo + "\',\'" + bookingDate + "\',\'" + noOfPeople + "\',\'" + price + "\');" ;

            esql.executeUpdate(query);
        }catch (Exception e) {
            System.err.println (e.getMessage());
        }
      // ...
   }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){
	  // Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      // Your code goes here.
      // ...
      // ...
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
      // Your code goes here.
      // ...
        int reqID = 0;
        String checkreqID;

	checkreqID = "SELECT reqID FROM Request";
	try { 
		reqID = esql.executeQuery(checkreqID);
		//reqID = reqID + 1;
	}
	catch (Exception e) {
		System.err.println(e.getMessage());
}


        int managerID;
        String checkm;

        do {
            System.out.println("Input your Manager ID Number: ");
            try {
                checkm = in.readLine();
                managerID = Integer.parseInt(checkm);
                if (checkm.length() <= 0) {
                    throw new RuntimeException("Manager ID can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invalid input!");
                continue;
            }
        }while(true);

        int repairID;
        String checkR;

        do {
            System.out.println("Input your Repair ID Number: ");
            try {
                checkR = in.readLine();
                repairID = Integer.parseInt(checkR);
                if (checkR.length() <= 0) {
                    throw new RuntimeException("Repair ID can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invalid input!");
                continue;
            }
        }while(true);



        Date requestDate;
        String tempreq;
//SimpleDateFormatter dformat = SimpleDateFormatter.ofPattern("dd-MM-yyyy");

        do {
            System.out.println("Input your Request date: ");
            try {
                tempreq = in.readLine();
                requestDate = new SimpleDateFormat("MM/DD/YY").parse(tempreq);
                break;

            }catch (Exception e){
                System.out.println("Invalid input!");
                continue;
            }
        }while(true);

        
        String description;

        do {
            System.out.println("Input Repair Description: ");
            try {
                description = in.readLine();
                
                if (description.length() <= 0) {
                    throw new RuntimeException("Description can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invaild input!");
                continue;
            }
        }while(true);




        try {
            String query = "INSERT INTO Request (reqID, managerID, repairID, requestDate, description) VALUES (" + reqID + ", \'" + managerID + "\',\'" + repairID + "\',\'" + requestDate + "\',\'" + description + "\');" ;

            esql.executeUpdate(query);
        }catch (Exception e) {
            System.err.println (e.getMessage());
        }
      // ...
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms available 
      // Your code goes here.
      // ...
      // ...
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms booked
      // Your code goes here.
      // ...
      // ...
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      // Your code goes here.
      // ...
      // ...
   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
	  // List Top K Rooms with the highest price for a given date range
      // Your code goes here.
      // ...
      // ...
   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer Name, List Top K highest booking price for a customer 
      // Your code goes here.
      // ...
      // ...
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      // Your code goes here.
      // ...
      // ...
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      // Your code goes here.
      // ...
      // ...
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      // Your code goes here.
      // ...
int k;
String checkK;
        do {
            System.out.println("Input the value of K: ");
            try {
                checkK = in.readLine();
                k = Integer.parseInt(checkK);
                if (checkK.length() <= 0) {
                    throw new RuntimeException("K can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invaild input!");
                continue;
            }
        }while(true);


       // try {
         //   String query = "COUNT * FROM Repairs Where hotelID = hotelID AND roomNo = roomNo VALUES (" + hotelID + ", \'" + roomNo + "\');" ;

           // esql.executeQuery(query);
        //}catch (Exception e) {
          //  System.err.println (e.getMessage());
//        } 
      // ...
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      // Your code goes here.
      // ...
int hotelID = 0;
String checkH;
        do {
            System.out.println("Input your Hotel ID: ");
            try {
                checkH = in.readLine();
                hotelID = Integer.parseInt(checkH);
                if (checkH.length() <= 0) {
                    throw new RuntimeException("Hotel ID can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invaild input!");
                continue;
            }
        }while(true);

int roomNo = 0;
String checkNo;
        do {
            System.out.println("Input your Room Number: ");
            try {
                checkNo = in.readLine();
                roomNo = Integer.parseInt(checkNo);
                if (checkNo.length() <= 0) {
                    throw new RuntimeException("Room Number can't be empty!");
                }
                break;
            }catch (Exception e){
                System.out.println("Invaild input!");
                continue;
            }
        }while(true);


       // try {
         //   String query = "COUNT * FROM Repairs Where hotelID = hotelID AND roomNo = roomNo VALUES (" + hotelID + ", \'" + roomNo + "\');" ;

           // esql.executeQuery(query);
        //}catch (Exception e) {
          //  System.err.println (e.getMessage());
//        } 
      // ...
   }//end listRepairsMade

}//end DBProject
