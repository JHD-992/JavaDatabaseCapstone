
import java.sql.*;
import java.time.LocalDate;
import java.util.*;


public class Invoice {

	public static void main(String[] args) {

		/**Initiate a variable to track the number of SQL queries that are executed*/
		int rowsAltered;
		/**Initiate a  scanner to read in any requested user input*/
		Scanner userInput = new Scanner(System.in);
		
		/**Print out a menu for the user to  select their choice of operation from.
		 * Allows the user to choose from adding customers, updating customer info,
		 * adding customer info, completing orders and finding an invoice
		 */
		System.out.println("Please select operation: "
				+ "\n1. Add new customer"
				+ "\n2. Update customer information"
				+ "\n3. Add restaurant"
				+ "\n4. Complete order"
				+ "\n5. Find Invoice"
				+ "\n0. Exit");
		/**Initialize and set a variable to take in the input that the user is generating*/
		String menuChoice = userInput.nextLine();
		
		/**Creation of a while loop to be able to process any user generated input that does not match a condition that will terminate the loop.
		 * As long as the user does not enter 0 and enters one of the other valid list items, the code will execute based on the number input.
		 */
		while (!menuChoice.equals("0")) {
			/**Switch statement to encase and process the user choice as entered after they were given the displayed operation menu*/
			switch (menuChoice) {
			/**If the user elects to add a new client to the database, collect the information in specified format, connect to the database server
			 * and add the information where appropriate */
				case "1":
					/**Generate output that shows the user what the format is expected to be when new client data is to be captured */
					System.out.println("Please enter client information."
							+ "\nEg. Abe, Ducat, ducatabe@example.com, 123 456 7890, 1 Art Road, Kenilworth, Cape Town");
					/**Initiate a variable that is used to store the new clients information as is input by the user*/
					String newCust = userInput.nextLine();
					/**Initiate and populate an array based on the earlier received user input so as to prepare it for adding to the database */
					String[] custInfo = newCust.split(", ");
					
					/** a try block used to attempt to connect to the database server where it is possible to do so */
					try {
						/**Establish connection to the database*/
						Connection connection = DriverManager.getConnection(
								"jdbc:sqlserver://localhost;database=QuickFoodMS", "JHDreyer", "Greywind");
						
						/**Create a direct line to the database for running our queries*/
						Statement statement = connection.createStatement();
						
						/** variable is set to a query so as to add the new customer info to the database 
						 * while allowing for the auto-incrementation of an id field within the database itself*/
						rowsAltered = statement.executeUpdate("INSERT INTO customer_info(cust_name, cust_surname, cust_email, cust_contact_num, cust_street, cust_suburb, cust_city) "
								+ "VALUES ('" + custInfo[0] + "', '" + custInfo[1]  + "', '" + custInfo[2] + "', '" + custInfo[3] + "', '" + custInfo[4]  + "', '" + custInfo[5] + "', '" + custInfo[6] + "')");
						
						/**Print out confirming the amount of customers added, should in current iteration always be 1 if successful */
						System.out.println("\n" + rowsAltered + " customer succesfully added"); 
					}
					/**Catch statement in case the attempt to connect to the SQL server is unsuccessful */
					catch (SQLException e) {
						e.printStackTrace();
					}
					/**Print out a menu for the user to  select their choice of operation from.
					 * Allows the user to choose from adding customers, updating customer info,
					 * adding customer info, completing orders and finding an invoice
					 */
					System.out.println("\nPlease select operation: "
							+ "\n1. Add new customer"
							+ "\n2. Update customer information"
							+ "\n3. Add restaurant"
							+ "\n4. Complete order"
							+ "\n5. Find Invoice"
							+ "\n0. Exit");
					menuChoice = userInput.nextLine();
				break;
				
				/**If the user elects to update client information, they will be shown a list of users in the database.
				 * The user will then be able to select the client they wish to update. Finally they will be able to select which information
				 * to update until the user specifies that they have completed updates*/
				case "2":
					/**Initiate a variable to store the results of any queries executed to the SQL server */
					ResultSet result;
					
					/** a try block used to attempt to connect to the database server where it is possible to do so */
					try {
						/**Establish connection to the database*/
						Connection connection = DriverManager.getConnection(
								"jdbc:sqlserver://localhost;database=QuickFoodMS", "JHDreyer", "Greywind");
						
						/**Create a direct line to the database for running our queries*/
						Statement statement = connection.createStatement();
						
						/**Run a query to retrieve all information from the customer_info table */
						result = statement.executeQuery("SELECT * FROM customer_info");
						
						/**Print out the information contained in the customer_info table to the user so as to assist them in
						 * choosing the correct record to update */ 
						System.out.println("\nID, NAME, SURNAME, EMAIL");
						while(result.next()) {
							System.out.println(result.getInt("cust_id") + ", " + result.getString("cust_name") + ", " + result.getString("cust_surname") + ", " + result.getString("cust_email"));
						}
						
						/**Print out a message asking the user to select the id of the user that they wish to edit  */
						System.out.println("\nWhich user would you like to update?"
								+ "\nPlease enter their ID");
						String custId = userInput.nextLine();
						
						/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
						result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
						
						/** while the result from the above query still has results out it in a menu form to allow the user to select
						 * which information they would like to update */
						while(result.next()) {
							System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
							+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
							+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
						}
						menuChoice = userInput.nextLine();
						
						/**Initialize a variable that will be used to store that information to be used for the update */
						String infoUpdate;
						
						/**While the user does not specify that they wish to finish updates, loop through the options for updating information*/
						while (!menuChoice.equals("0")) {
							/** switch block for processing the user choice specifying what the user wants to edit */
							switch (menuChoice) {
								/**if the user wishes to update a client name read in the new information, update the field
								 * and ask the user if they wish to update anything else */
								case "1":
									/**Print out asking for the updated name and store it in a variable*/
									System.out.println("Please enter the updated name");
									infoUpdate = userInput.nextLine();
								
									/** variable is set to a query so as to update the customer name with the provided information*/
									rowsAltered = statement.executeUpdate("UPDATE customer_info SET cust_name = '" + infoUpdate +"' WHERE cust_id= " + custId +"");
									
									/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
									result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
									
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
								
								case "2":
									/**Print out asking for the updated surname and store it in a variable*/
									System.out.println("Please enter the updated surname");
									infoUpdate = userInput.nextLine();
								
									/** variable is set to a query so as to update the customer surname with the provided information*/
									rowsAltered = statement.executeUpdate("UPDATE customer_info SET cust_surname = '" + infoUpdate +"' WHERE cust_id= " + custId +"");
									
									/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
									result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
									
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
								
								case "3":
									/**Print out asking for the updated email and store it in a variable*/
									System.out.println("Please enter the updated email");
									infoUpdate = userInput.nextLine();
								
									/** variable is set to a query so as to update the customer email with the provided information*/
									rowsAltered = statement.executeUpdate("UPDATE customer_info SET cust_email = '" + infoUpdate +"' WHERE cust_id= " + custId +"");
									
									/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
									result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
									
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
								
								case "4":
									/**Print out asking for the updated contact number and store it in a variable*/
									System.out.println("Please enter the updated contact number");
									infoUpdate = userInput.nextLine();
								
									/** variable is set to a query so as to update the customer contact number with the provided information*/
									rowsAltered = statement.executeUpdate("UPDATE customer_info SET cust_contact_num = '" + infoUpdate +"' WHERE cust_id= " + custId +"");
									
									/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
									result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
									
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
								
								case "5":
									/**Print out asking for the updated street address and store it in a variable*/
									System.out.println("Please enter the updated street address");
									infoUpdate = userInput.nextLine();
								
									/** variable is set to a query so as to update the customer street address with the provided information*/
									rowsAltered = statement.executeUpdate("UPDATE customer_info SET cust_street = '" + infoUpdate +"' WHERE cust_id= " + custId +"");
									
									/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
									result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
									
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
								
								case "6":
									/**Print out asking for the updated suburb and store it in a variable*/
									System.out.println("Please enter the updated suburb");
									infoUpdate = userInput.nextLine();
								
									/** variable is set to a query so as to update the customer suburb with the provided information*/
									rowsAltered = statement.executeUpdate("UPDATE customer_info SET cust_suburb = '" + infoUpdate +"' WHERE cust_id= " + custId +"");
									
									/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
									result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
									
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
								
								case "7":
									/**Print out asking for the updated city and store it in a variable*/
									System.out.println("Please enter the updated city");
									infoUpdate = userInput.nextLine();
								
									/** variable is set to a query so as to update the customer city with the provided information*/
									rowsAltered = statement.executeUpdate("UPDATE customer_info SET cust_city = '" + infoUpdate +"' WHERE cust_id= " + custId +"");
									
									/**Run a query to retrieve all information from the customer_info table where the user id matches the one input by user*/
									result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_id =" + custId + "");
									
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
								
								default:
									System.out.println("Invalid selection. Please choose again\n");
									/** while the result from the above query still has results out it in a menu form to allow the user to select
									 * which information they would like to update */
									while(result.next()) {
										System.out.println("1. Name:  " + result.getString("cust_name") + "\n2. Surname: " + result.getString("cust_surname") 
										+ "\n3. Email: " + result.getString("cust_email") + "\n4. Contact Number: " + result.getString("cust_contact_num") + "\n5. Street: " + result.getString("cust_street") + "\n6. Suburb: " + result.getString("cust_suburb")
										+ "\n7. City: " + result.getString("cust_city") + "\n0. Finish Updates");
									}
									menuChoice = userInput.nextLine();
								break;
							}
						}
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
					/**Print out a menu for the user to  select their choice of operation from.
					 * Allows the user to choose from adding customers, updating customer info,
					 * adding customer info, completing orders and finding an invoice
					 */
					System.out.println("\nPlease select operation: "
							+ "\n1. Add new customer"
							+ "\n2. Update customer information"
							+ "\n3. Add restaurant"
							+ "\n4. Complete order"
							+ "\n5. Find Invoice"
							+ "\n0. Exit");
					menuChoice = userInput.nextLine();
				break;
				
				case "3":
					/**Provide a print out specifying to the user the format in which restaurant information should be entered*/
					System.out.println("Please enter client information."
							+ "\nEg. Morgans Steakhouse, 879 546 2113, Johannesburg");
					/**store user input into a variable */
					String newRest = userInput.nextLine();
					/**break the user generated input into its component parts so that it may be processed correctly */
					String[] restInfo = newRest.split(", ");
					
					/**try block to attempt to connect to the sql database in order to facilitate any additions */
					try {
						/**Establish connection to the database*/
						Connection connection = DriverManager.getConnection(
								"jdbc:sqlserver://localhost;database=QuickFoodMS", "JHDreyer", "Greywind");
						
						/**Create a direct line to the database for running our queries*/
						Statement statement = connection.createStatement();
						
						/**use a variable that will assist in the processing of a sql query to add the appropriate information to the restaurant info table */
						rowsAltered = statement.executeUpdate("INSERT INTO restaurant_info(restaurant_name, restaurant_contact_num, restaurant_city) "
								+ "VALUES ('" + restInfo[0] + "', '" + restInfo[1] + "', '" + restInfo[2] +  "')");
						
						/**print out to the console that records have been added */
						System.out.println("\n" + rowsAltered + " restaurant succesfully added"); 
					}
					/** catch the exception that may be thrown if the sql database is unreachable */
					catch (SQLException e) {
						e.printStackTrace();
					}
					/**Print out a menu for the user to  select their choice of operation from.
					 * Allows the user to choose from adding customers, updating customer info,
					 * adding customer info, completing orders and finding an invoice
					 */
					System.out.println("\nPlease select operation: "
							+ "\n1. Add new customer"
							+ "\n2. Update customer information"
							+ "\n3. Add restaurant"
							+ "\n4. Complete order"
							+ "\n5. Find Invoice"
							+ "\n0. Exit");
					menuChoice = userInput.nextLine();
				break;
				
				/**if the user selects to complete an order, use the client name to find drivers, read in the restaurant name,
				 * read in the menu selections and determine the total cost that may be due */
				case "4":
					/**Print out requesting that the user enter a customer name to complete an order for and split it into name
					* to facilitate later look up*/
					System.out.println("Please enter customer name");
					String orderCust = userInput.nextLine();
					String[] currentCust = orderCust.split(" ");
					/**initiate variables that will contain client information later in this section */
					String custNum = "";
					String custEmail = "";
					String custCity = "";
					/**read in and store the name of the restaurant the order will be placed at */
					System.out.println("Please input Restaurant name");
					String restName = userInput.nextLine();
					/**read in and store the amount of items in the order. This does not link to how many of each item there may be
					 * eg there may be 1 type of pizza but 3 individual pizzas*/
					System.out.println("How many menu items are in the order?");
					int orderQty = userInput.nextInt();
					/** read in the current date from the machine for a completion date*/
					LocalDate date = LocalDate.now();
					/** set driver loads to 100 initially to facilitate determining who has the least orders currently assigned*/
					int driverLoad = 100;
					/** initialize a variable that will be used to determine the ID of the driver that will be assigned to an order. */
					int driverID = 0;
					/** initialize a variable that will be used to store the ID of the restaurant where order will be placed*/
					int restID = 0;
					/** initialize a variable that will be used to store the ID of items selected*/
					int itemID;
					/** initialize a variable that will be used to store the cumulative cost of the order*/
					float totalCost = 0;
					/** initialize a variable to store how many of each item is being ordered*/
					int itemQty;
					
					/** a try block used to attempt to connect to the database server where it is possible to do so */
					try {
						/**Establish connection to the database*/
						Connection connection = DriverManager.getConnection(
						"jdbc:sqlserver://localhost;database=QuickFoodMS", "JHDreyer", "Greywind");
						
						/**Create a direct line to the database for running our queries*/
						Statement statement = connection.createStatement();

						/** run a query pulling customer information where name and surnames match the user input*/
						result = statement.executeQuery("SELECT * FROM customer_info WHERE cust_name = '" + currentCust[0] + "' AND cust_surname = '" + currentCust[1] + "'");
						
						/** a while loop that runs as long as there is a result and sets the city, contact number and customer
						 * email to earlier declared variables*/
						while(result.next()) {
							custCity = result.getString("cust_city");
							custNum = result.getString("cust_contact_num");
							custEmail  = result.getString("cust_email");
							}
						
						/** run a query to fetch all drivers that have the same city as the client*/ 
						result = statement.executeQuery("SELECT * FROM driver_info WHERE driver_city = '" + custCity + "'");
						
						/** while there are still results run the following loop to determine which driver has the least loads
						 * currently assigned to them for delivery*/
						while(result.next()) {
							if (result.getInt("driver_load") < driverLoad) {
									driverID = result.getInt("driver_id");
									driverLoad = result.getInt("driver_load");
								}
							}
						/** run a query to fetch all information from restaurant_info where restaurant name matches earlier input
						 * and store the restaurant id*/ 
						result = statement.executeQuery("SELECT * FROM restaurant_info WHERE restaurant_name = '" + restName + "'");
						while(result.next()) {
							restID = result.getInt("restaurant_id");
							}
						/** output all the items assigned to the restaurant in the items_info table to allow user to see "menu"*/
						System.out.println("\nItem ID, ITEM NAME, ITEM COST");
						result = statement.executeQuery("SELECT * FROM items_info WHERE restaurant_ID = " + restID + "");
						while(result.next()) {
							System.out.println(result.getInt("item_id") + ", " + result.getString("item_name") + ", R" + result.getFloat("item_cost"));
							}
						
						/**loop to read in user "orders" and determine total cost to be added to the invoice database */
						for (int i = 0; i < orderQty; i++) {
							System.out.println("Please enter the item id");
							itemID = userInput.nextInt();
							result = statement.executeQuery("SELECT * FROM items_info WHERE item_id = " + itemID + "");
							System.out.println("Please enter quantity of item");
							itemQty = userInput.nextInt();
							
							while(result.next()) {
								totalCost += result.getInt("item_cost") * itemQty;
								}
						}
						/** variable is used to execute a query to add the new order information to the invoice_info table*/
						rowsAltered = statement.executeUpdate("INSERT INTO invoice_info (cust_name, cust_surname, cust_contact_num, cust_email, total_cost, status, comp_date)"
									+ "VALUES ('" + currentCust[0] + "', '" + currentCust[1] + "', '" + custNum + "', '" + custEmail + "', '" + totalCost + "', 'finalised', '" + date + "')");					
						}
						/** catch the exception that may be thrown if the sql database is unreachable */
						catch (SQLException e) {
							e.printStackTrace();
						}
					/**Print out a menu for the user to  select their choice of operation from.
					 * Allows the user to choose from adding customers, updating customer info,
					 * adding customer info, completing orders and finding an invoice
					 */
					System.out.println("\nPlease select operation: "
							+ "\n1. Add new customer"
							+ "\n2. Update customer information"
							+ "\n3. Add restaurant"
							+ "\n4. Complete order"
							+ "\n5. Find Invoice"
							+ "\n0. Exit");
					menuChoice = userInput.nextLine();
				break;
				
				/** if the user chooses to look up an invoice, allow them to select to search based on name or order/invoice nr*/
				case "5":
				
					/**print out a message giving the user options to select from */
					System.out.println("Do you want to search by:"
							+ "\n1. Order/Invoice number"
							+ "\n2. Client name");
					menuChoice = userInput.nextLine();
					
					/** switch block to process the user choice pending what they want to search by*/
					switch (menuChoice) {
						case "1":
							/** a try block used to attempt to connect to the database server where it is possible to do so */
							try {
								/**Establish connection to the database*/
								Connection connection = DriverManager.getConnection(
								"jdbc:sqlserver://localhost;database=QuickFoodMS", "JHDreyer", "Greywind");
								
								/**Create a direct line to the database for running our queries*/
								Statement statement = connection.createStatement();
								
								/** request the user to input order/invoice number*/
								System.out.println("Please enter order/invoice number");
								int invNr = userInput.nextInt();
								/** set variable to search for all invoice information based on the provided invoice/order number*/
								result = statement.executeQuery("SELECT * FROM invoice_info WHERE invoice_nr = " + invNr + "");
								
								/** print out all the information retrieved based on order/invoice nr*/
								while(result.next()) {
									System.out.println(result.getInt("invoice_nr") + ", " + result.getString("cust_name") + ", "
											+ result.getString("cust_surname") + ", " + result.getString("cust_contact_num") + ", "
											+ result.getString("cust_email") + ", " + result.getString("total_cost") + ", "
											+ result.getString("status") + ", " + result.getString("comp_date"));
								}
							}
							/** catch the exception that may be thrown if the sql database is unreachable */
							catch (SQLException e) {
								e.printStackTrace();
							}
						break;
						
						case "2":
							/** a try block used to attempt to connect to the database server where it is possible to do so */
							try {
								/**Establish connection to the database*/
								Connection connection = DriverManager.getConnection(
								"jdbc:sqlserver://localhost;database=QuickFoodMS", "JHDreyer", "Greywind");
								
								/**Create a direct line to the database for running our queries*/
								Statement statement = connection.createStatement();
								
								/**print out asking the user to enter a client name, break it into two parts and search based there on */
								System.out.println("Please enter client name");
								String invName = userInput.nextLine();
								String[] invNameInfo = invName.split(" ");
								result = statement.executeQuery("SELECT * FROM invoice_info WHERE cust_name = '" + invNameInfo[0] + "', '" + invNameInfo[1] + "'");
								
								/** print out all the information retrieved based on client name nr*/
								while(result.next()) {
									System.out.println(result.getInt("invoice_nr") + ", " + result.getString("cust_name") + ", "
											+ result.getString("cust_surname") + ", " + result.getString("cust_contact_num") + ", "
											+ result.getString("cust_email") + ", " + result.getString("total_cost") + ", "
											+ result.getString("status") + ", " + result.getString("comp_date"));
								}
							}
							/** catch the exception that may be thrown if the sql database is unreachable */
							catch (SQLException e) {
								e.printStackTrace();
							}
							
						break;
					}
					/**Print out a menu for the user to  select their choice of operation from.
					 * Allows the user to choose from adding customers, updating customer info,
					 * adding customer info, completing orders and finding an invoice
					 */
					System.out.println("\nPlease select operation: "
							+ "\n1. Add new customer"
							+ "\n2. Update customer information"
							+ "\n3. Add restaurant"
							+ "\n4. Complete order"
							+ "\n5. Find Invoice"
							+ "\n0. Exit");
					menuChoice = userInput.nextLine();
				break;
			}
		}
		userInput.close();
	}
}