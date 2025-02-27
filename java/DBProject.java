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
// Student imports:
import java.util.Date;
import java.text.SimpleDateFormat;

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
      SimpleDateFormat formatter1=new SimpleDateFormat("MM/dd/yy");
      SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd"); 

      checkID = "SELECT customerID FROM Customer";
      try { 
         customerID = esql.executeQuery(checkID);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

      String fName;
      do {
         System.out.println("Input your First Name: ");
         try {
            fName = in.readLine();
            if (fName.length() <= 0 || fName.length() > 30) {
               throw new RuntimeException("First Names can't be null or longer than 30 characters");
            }
         break;
         }
         catch (Exception e){
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
         }
         catch (Exception e){
			System.out.println(e);
			continue;
		   }
	   }while(true);

      String Address;
      do {
		   System.out.println("Input your Address: ");
		   try {
			   Address = in.readLine();
            break;
         }
         catch (Exception e){
			   System.out.println(e);
			   continue;
		   }
	}while(true);

   Date DOB;
   String temp;

	do {
		System.out.println("Input your date of birth (MM/dd/YY): ");
		try {
			temp = in.readLine();
			DOB = formatter1.parse(temp);
			break;
      }
      catch (Exception e){
			System.out.println("Invaild input!");
			continue;
		}
	}while(true);

   long phNo;
   String tempNo;

   do {
      System.out.println("Input the customer's phone number");
      try {
         temp = in.readLine();
         phNo = Long.parseLong(temp);
         break;
      }
      catch (Exception e)
      {
         System.out.println("Invalid input!");
         continue;
      }
   }while(true);

   String GenderType;
	do {
		System.out.println("Input your Gender: Male, Female, Other ");
		try {
			GenderType = in.readLine();
      break;
      }
      catch (Exception e){
			System.out.println(e);
			continue;
		}
	}while(true);

	try {
		String query = "INSERT INTO CUSTOMER (customerID, fName, lName, Address, phNo, DOB, gender) VALUES (" + customerID + ", \'" + fName + "\',\'" + lName + "\',\'"  + Address + "\',\'" + phNo + "\',\'"  + formatter2.format(DOB) + "\',\'"  + GenderType + "\');";
		esql.executeUpdate(query);
	}catch (Exception e) {  
				System.err.println (e.getMessage());
}   
   };//end addCustomer

   public static void addRoom(DBProject esql){
      String tempID;
      int hotelID;
      int roomNo;
      String roomType;
      // Hotel ID
      do {
         System.out.println("Input the hotel ID:");
         try {
            tempID = in.readLine();
            if (tempID.length() <= 0) {
               throw new RuntimeException("Hotel ID cannot be empty!");
            }
            hotelID = Integer.parseInt(tempID);
            break;
         }
         catch (Exception e){
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);
      // Room number
      do {
         System.out.println("Input the room number:");
         try {
            tempID = in.readLine();
            if (tempID.length() <= 0) {
               throw new RuntimeException("Room number cannot be empty!");
            }
            roomNo = Integer.parseInt(tempID);
            break;
         }
         catch (Exception e){
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);
      // Room type
      do {
         System.out.println("Input the room type:");
         try {
            roomType = in.readLine();
            if (roomType.length() > 10) {
               throw new RuntimeException("Room type cannot be more than ten characters.");
            }
            if (roomType.length() <= 0) {
               throw new RuntimeException("Room type cannot be empty.");
            }
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);
      
      String query = "INSERT INTO Room (hotelID, roomNo, roomType) Values (" + hotelID + ", " + roomNo
         + ", \'" + roomType + "\');";

      try {
         esql.executeUpdate(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end addRoom

   public static void addMaintenanceCompany(DBProject esql){
      int cmpID = 0;
      String checkID;

	   checkID = "SELECT cmpID FROM MaintenanceCompany";
	   try { 
		   cmpID = esql.executeQuery(checkID);
	   }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

      String name;
         do {
            System.out.println("Input your Company Name: ");
            try {
               name = in.readLine();
               if (name.length() <= 0 || name.length() > 30) {
                  throw new RuntimeException("Company Names can't be null or longer than 30 characters");
                }
               break;
            }
            catch (Exception e){
               System.out.println(e);
               continue;
            }
         }while(true);

      String check;
      boolean isCertified;
         do {
            System.out.println("Is your Company Certified? (true/false): ");
            try {
               check = in.readLine();
               if (check.length() <= 0) {
                  throw new RuntimeException("Can't be Null/Empty");
               }
               isCertified = Boolean.parseBoolean(check);
               break;
            }
            catch (Exception e){
               System.out.println(e);
               continue;
            }
         }while(true);

      String Address;

         do {
            System.out.println("Input your Address: ");
            try {
               Address = in.readLine();
               break;
            }
            catch (Exception e){
               System.out.println(e);
               continue;
            }
         }while(true);


      try {
         String query = "INSERT INTO MaintenanceCompany (cmpID, name, address, isCertified) VALUES (" + cmpID + ", \'" + name + "\',\'" + Address + "\',\'" + isCertified + "\');" ;

         esql.executeUpdate(query);
      }
      catch (Exception e) {  
               System.err.println (e.getMessage());
      }
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){
      String temp;
      int rID = 0;
      int hotelID = 0;
      int roomNo = 0;
      int mCompany = 0;
      Date repairDate;
      String description;
      String repairType;
      SimpleDateFormat formatter1=new SimpleDateFormat("MM/dd/yy");  

      temp = "SELECT * FROM Repair";
      try { 
         rID = esql.executeQuery(temp);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

      do {
         System.out.println("Input the hotel ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Hotel ID cannot be empty.");
            }
            hotelID = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the room number.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Room number cannot be empty.");
            }
            roomNo = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the maintenance company's ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Maintenance company ID cannot be empty.");
            }
            mCompany = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the repair date. (MM/dd/yy)");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Repair date cannot be empty.");
            }
            repairDate = formatter1.parse(temp);
            break;
         }
         catch  (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the description. (Optional)");
         try {
            temp = in.readLine();
            description = temp;
            break;
         }
         catch (Exception e) {
            System.out.print("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Specify the repair type.");
         try{
            temp = in.readLine();
            if (temp.length() > 10) {
               throw new RuntimeException("Repair type must be less than 10 characters.");
            }
            repairType = temp;
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "INSERT INTO Repair (rID, hotelID, roomNo, mCompany, repairDate, description, repairType) Values (" 
         + rID + ", " + hotelID + ", " + roomNo + ", " + mCompany + ", \'" + formatter1.format(repairDate) + "\', \'" 
         + description + "\', \'" + repairType + "\');";

      try {
         esql.executeUpdate(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end addRepair

   public static void bookRoom(DBProject esql){
      // Given hotelID, roomNo and customer Name create a booking in the DB 
      int bID = 0;
      String checkbID;
      SimpleDateFormat formatter1=new SimpleDateFormat("MM/dd/yy");
      SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd");

      checkbID = "SELECT bID FROM Booking";
      try { 
         bID = esql.executeQuery(checkbID);
         bID = bID;
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

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

      do {
            System.out.println("Input your booking date: ");
            try {
               tempb = in.readLine();
               bookingDate = formatter1.parse(tempb);
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
               break;
            }catch (Exception e){
               System.out.println("Invaild input!");
               continue;
            }
      }while(true);


      float price;
      String checkP;

      do {
            System.out.println("Input your Price: $ ");
            try {
               checkP = in.readLine();
               price = Float.parseFloat(checkP);
               if (checkP.length() <= 0) {
                  throw new RuntimeException("Price can't be empty!");
               }
               if (price > 9999.99)
               {
                  throw new RuntimeException("Price is too high!");
               }
               if (price < 0)
               {
                  throw new RuntimeException("Invalid price!");
               }
               break;
            }catch (Exception e){
               System.out.println("Invaild input!");
               continue;
            }
      }while(true);


      try {
            String query = "INSERT INTO Booking (bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price) VALUES (" + bID + ", \'" + customer + "\',\'" + hotelID + "\',\'" + roomNo + "\',\'" + formatter2.format(bookingDate) + "\',\'" + noOfPeople + "\',\'" + price + "\');" ;

            esql.executeUpdate(query);
      }catch (Exception e) {
            System.err.println (e.getMessage());
      }
   }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){
      int staffID = 0;
      int hotelID = 0;
      int roomNo = 0;
      int asgID = 0;
      String temp;
      
      do {
         System.out.println("Input the Staff ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Staff ID cannot be empty.");
            }
            staffID = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the hotel ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Hotel ID cannot be empty.");
            }
            hotelID = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the room number.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Room number cannot be empty.");
            }
            roomNo = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      // Figure out what ID is next in the sequence by counting rows. Assuming data is sequential and continuous.
      temp = "SELECT * FROM Assigned";
      try {
         asgID = esql.executeQuery(temp);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
      // Assume asgID starts at 0
      String query = "INSERT INTO Assigned (asgID, staffID, hotelID, roomNo) Values (" + asgID + ", "
         + staffID + ", " + hotelID + ", " + roomNo + ");";

      try {
         esql.executeUpdate(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
      int reqID = 0;
      String checkreqID;

      checkreqID = "SELECT reqID FROM Request";
      try { 
         reqID = esql.executeQuery(checkreqID);
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
            }
            catch (Exception e){
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


      do {
            System.out.println("Input your Request date: ");
            try {
               tempreq = in.readLine();
               requestDate = new SimpleDateFormat("MM/dd/yy").parse(tempreq);
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
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){ //Assuming date doesn't matter.
     // Given a hotelID, get the count of rooms available 
      String temp;
      int hotelID = 0;
      int roomsAvailCount = 0;

      do {
         System.out.println("Input the hotel ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Hotel ID cannot be empty.");
            }
            hotelID = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "SELECT avail.* FROM ((SELECT r.roomNo, r.hotelID FROM Room r) EXCEPT (SELECT b.roomNo, b.hotelID FROM Booking b)) as avail WHERE avail.hotelID="
         + hotelID + ";";
      try{
         roomsAvailCount = esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

      System.out.println("There are " + roomsAvailCount + " available rooms in the hotel with ID " + hotelID);
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){
     // Given a hotelID, get the count of rooms booked
      String temp;
      int hotelID = 0;
      int roomsBookedCount = 0;

      do {
         System.out.println("Input the hotel ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Hotel ID cannot be empty.");
            }
            hotelID = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "SELECT * FROM Booking WHERE hotelID=" + hotelID + ";";
      try {
         roomsBookedCount = esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

      System.out.println("There are " + roomsBookedCount + " booked rooms in the hotel with ID " + hotelID);
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      String temp;
      int hotelID = 0;
      Date bookingDate;
      SimpleDateFormat formatter1=new SimpleDateFormat("MM/dd/yy");
      SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd");    

      do {
         System.out.println("Input the hotel ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Hotel ID cannot be empty.");
            }
            hotelID = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the date. (MM/DD/YY)");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Booking date cannot be empty.");
            }
            bookingDate = formatter1.parse(temp);
            break;
         }
         catch  (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "SELECT avail.* FROM ((SELECT r.roomNo, r.hotelID FROM Room r) EXCEPT (SELECT b.roomNo, b.hotelID FROM Booking b WHERE b.bookingdate >= \'" 
         + formatter2.format(bookingDate) + "\'::Date AND b.bookingDate <= \'" + formatter2.format(bookingDate) + "\'::Date + \'1 week\'::Interval))"
         + " as avail WHERE avail.hotelID=" + hotelID + ";";

      try {
         esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
	  // List Top K Rooms with the highest price for a given date range
      String temp;
      Date start;
      Date end;
      int k = 0;
      SimpleDateFormat formatter1=new SimpleDateFormat("MM/dd/yy");
      SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd");

      do {
         System.out.println("Input the start date. (MM/DD/YY)");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Repair date cannot be empty.");
            }
            start = formatter1.parse(temp);
            break;
         }
         catch  (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the end date. (MM/DD/YY)");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Repair date cannot be empty.");
            }
            if (start.after(formatter1.parse(temp))) {
               throw new RuntimeException("End date cannot be before start date.");
            }
            end = formatter1.parse(temp);
            break;
         }
         catch  (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the amount of results you want to see");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Amount of results cannot be empty.");
            }
            if (Integer.parseInt(temp) <= 0) {
               throw new RuntimeException("Amount of results cannot be 0 or less than 0");
            }
            k = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "SELECT price, roomNo, bookingDate FROM booking WHERE bookingDate >= \'" + formatter2.format(start) + 
      "\' AND bookingDate <= \'" + formatter2.format(end) + "\' ORDER BY price DESC LIMIT " + k + ";";

      try {
         esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }

   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer Name, List Top K highest booking price for a customer 
      String temp;
      String fName;
      String lName;
      int k = 0;

      do {
         System.out.println("Input the customer's first name.");
         try{
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("First name cannot be blank");
            }
            if (temp.length() > 30) {
               throw new RuntimeException("First name must be less than 10 characters.");
            }
            fName = temp;
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the customer's last name.");
         try{
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Last name cannot be blank");
            }
            if (temp.length() > 30) {
               throw new RuntimeException("Last name must be less than 10 characters.");
            }
            lName = temp;
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the amount of results you want to see");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Amount of results cannot be empty.");
            }
            if (Integer.parseInt(temp) <= 0) {
               throw new RuntimeException("Amount of results cannot be 0 or less than 0");
            }
            k = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "SELECT b.price FROM Booking b INNER JOIN Customer c ON c.customerID = b.customer WHERE c.fname = \'"
         + fName + "\' AND c.lname = \'" + lName +  "\' ORDER BY b.price DESC LIMIT " + k + ";";

         try {
            esql.executeQuery(query);
         }
         catch (Exception e) {
            System.err.println(e.getMessage());
         }
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      String temp;
      int hotelID;
      String fName;
      String lName;
      Date start;
      Date end;
      SimpleDateFormat formatter1=new SimpleDateFormat("MM/dd/yy");
      SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd");

      do {
         System.out.println("Input the hotel ID.");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Hotel ID cannot be empty.");
            }
            hotelID = Integer.parseInt(temp);
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the customer's first name.");
         try{
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("First name cannot be blank");
            }
            if (temp.length() > 30) {
               throw new RuntimeException("First name must be less than 10 characters.");
            }
            fName = temp;
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the customer's last name.");
         try{
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Last name cannot be blank");
            }
            if (temp.length() > 30) {
               throw new RuntimeException("Last name must be less than 10 characters.");
            }
            lName = temp;
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the start date. (MM/DD/YY)");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Repair date cannot be empty.");
            }
            start = formatter1.parse(temp);
            break;
         }
         catch  (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      do {
         System.out.println("Input the end date. (MM/DD/YY)");
         try {
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Repair date cannot be empty.");
            }
            if (start.after(formatter1.parse(temp))) {
               throw new RuntimeException("End date cannot be before start date.");
            }
            end = formatter1.parse(temp);
            break;
         }
         catch  (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "SELECT sum(b.price) FROM Booking b INNER JOIN Customer c ON c.customerID = b.customer WHERE b.hotelID = " + hotelID  
      + " AND c.fname = \'" + fName + "\' AND c.lname = \'" + lName +  "\' AND b.bookingDate >= \'" + formatter2.format(start) 
      + "\' AND b.bookingDate <= \'" + formatter2.format(end) + "\';";
      
      try {
         esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
     String temp; 
     String mcName;
      
     do {
         System.out.println("Input the maintenance company's name.");
         try{
            temp = in.readLine();
            if (temp.length() <= 0) {
               throw new RuntimeException("Company name cannot be blank");
            }
            if (temp.length() > 30) {
               throw new RuntimeException("Company name must be less than 10 characters.");
            }
            mcName = temp;
            break;
         }
         catch (Exception e) {
            System.out.println("Invalid input!");
            continue;
         }
      }while(true);

      String query = "SELECT R.rID, R.hotelID, R.repairType, R.repairDate, M.name FROM Repair R, MaintenanceCompany M WHERE M.name = \'"
         + mcName + "\' AND M.cmpID = R.mCompany;";

      try {
         esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      String checkK;
      int k;
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

     String query = "SELECT M.name, rep.count FROM maintenanceCompany M INNER JOIN (SELECT COUNT(*), mCompany FROM Repair GROUP BY mCompany) AS rep "
      + "ON rep.mCompany=M.cmpID ORDER BY count DESC LIMIT " + k + ";";
   
      try {
         esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
     int hotelID = 0;
     int roomNo = 0;
     String checkH;
             do {
                 System.out.println("Input the Hotel ID: ");
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
     
     String checkNo;
             do {
                 System.out.println("Input the Room Number: ");
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
      
      String query = "SELECT COUNT(*), EXTRACT(year FROM repairDate) FROM Repair WHERE hotelID="
      + hotelID + " and roomNo=" + roomNo + " GROUP BY EXTRACT(year FROM repairDate);";

      try {
         esql.executeQuery(query);
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }//end listRepairsMade

}//end DBProject
