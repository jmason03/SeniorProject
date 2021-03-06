package simulator;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * @author Jordan
 *
 */
public class AirplaneSimulatorGUI 
{

	/**
	 * @param args
	 */
	
	//Initialize global variables, string array that contains dropdown box selections
	static String[] planeconfigs = {"3 seats/2 columns/204 capacity", "3 seats/2 columns/138 seats"};
	static String[] methods = {"Back To Front", "Front to Back", "Alternating WMA", "Random Boarding"};
	static String[] passengerOrderOptions = {"Ordered", "Random", "Random Zones", "Reverse", "Window, Middle, Aisle", "WMA Random", "Random with Companions"};
	static String[] debugOptions = {"No", "Yes"};
	static String[] animationOptions = {"No", "Yes"};
	
    //Create dropdown box for GUI
    static JComboBox<String> planelayout = new JComboBox<String>(planeconfigs);
    static JComboBox<String> boardingmethods = new JComboBox<String>(methods);
    static JComboBox<String> passengerorder = new JComboBox<String>(passengerOrderOptions);
    static JComboBox<String> debug = new JComboBox<String>(debugOptions);
    static JComboBox<String> animation = new JComboBox<String>(animationOptions);
     
	//Create user input text box for percent of plane filled (entered as integer and program converts to percent)
	static JTextField percentfilled = new JTextField("");
	
	//Create user input text box for percent of passengers with bags
	//A passenger can only have 1 or 0 bags
	static JTextField percentbags = new JTextField("");
	
	static JTextField percentcompanions = new JTextField("");
	
	public static void main(String[] args) throws InterruptedException 
	{	   
	    //Create input GUI, returns integer that reflects what button was clicked
		int result = createInputPanel();
		
		//If result = 0, user clicked "Begin Simulation"
	    if (result == JOptionPane.OK_OPTION) 
	    {
			//Initialize Variables
	    	Passenger[] passengerList;
			double avgPassengerTime = 0.0;
			double totalBoardTime = 0.0;
			
			//Get values selected by users in input GUI
			String planeCapacity = (String) planelayout.getSelectedItem();
			int capacity = getCapacityInput(planeCapacity);
			int boardingMethod = boardingmethods.getSelectedIndex();
			int passengerOrder = passengerorder.getSelectedIndex();
			int debugSelection = debug.getSelectedIndex();
			int animationSelection = animation.getSelectedIndex();
			String percentCapacityInput = percentfilled.getText();
			String percentBagInput = percentbags.getText();
			String percentCompanionsInput = percentcompanions.getText();
			int intCapacityInput = Integer.parseInt(percentCapacityInput);
			int intBagsInput = Integer.parseInt(percentBagInput);
			int intCompanionsInput = Integer.parseInt(percentCompanionsInput);
			
			//Find number of total passengers based on percentage input given by user
			double percentCapacity = (double) intCapacityInput / 100;
			int totalPassengers = (int) (capacity * percentCapacity);
			double percentBags = (double) intBagsInput / 100;
			double percentCompanions = (double) intCompanionsInput / 100;
			int totalColumns = getTotalColumns(capacity, planeCapacity);
			int totalRows = totalPassengers/totalColumns;
			int totalZones = 6;
			
			//Constants for time it takes a passenger to sit down
			int windowSitTime = 5;
			int middleSitTime = 4;
			int aisleSitTime = 2;
			int moveToRow = 2;
			int moveToBack = 2;
			
			//Add 90 sec to totalBoardTime to represent time for first passenger to walk down jetway
			int preBoardTime = 90;
			totalBoardTime = preBoardTime;
			
			//Create list of available seats for passengers to sit in
			SeatList tempSeatList = new SeatList();
			String[] seatList = tempSeatList.createSeatList(totalPassengers, totalRows, totalColumns);
			
			//Create empty airplane that holds all passengers
			Airplane tempPlane = new Airplane();
			Passenger[][] plane = tempPlane.createAirplane(totalRows, totalColumns);
			
			//Initialize passenger list, will be modified based on user passenger order selection
			PassengerList tempPasList = new PassengerList();
			
			if(passengerOrder == 0) //Ordered
			{
				passengerList = tempPasList.createPassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, percentBags);
			}
			
			else if(passengerOrder == 1) //Random
			{
				passengerList = tempPasList.createRandomPassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, percentBags);
			}
			
			else if(passengerOrder == 2) //Random Zones
			{
				passengerList = tempPasList.createZoneRandomPassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, totalZones, percentBags);
			}
			
			else if(passengerOrder == 3) //Reverse
			{
				seatList = tempSeatList.createReverseSeatList(totalPassengers, totalRows, totalColumns);
				passengerList = tempPasList.createReversePassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, percentBags);
			}
			
			else if(passengerOrder == 4) //Window, Middle, Aisle Order
			{
				seatList = tempSeatList.createWMASeatList(totalPassengers, totalRows, totalColumns);
				passengerList = tempPasList.createWMAPassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, percentBags);
			}
			
			else if(passengerOrder == 5) //Window, Middle, Aisle Random Order
			{
				seatList = tempSeatList.createWMASeatList(totalPassengers, totalRows, totalColumns);
				passengerList = tempPasList.createWMARandomPassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, percentBags, totalRows, totalColumns);
			}
			
			else
			{
				passengerList = tempPasList.createRandomCompanionsPassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, totalColumns, percentBags, percentCompanions);
			}
			
			//Calculate time based on user selected boarding method
			//Return totalBoardTime for given boarding method
			CalculateTimeDisplaySim tempTime = new CalculateTimeDisplaySim();
			totalBoardTime = tempTime.calculateBoardTime(boardingMethod, passengerList, totalRows, totalColumns, totalBoardTime, moveToRow, moveToBack, windowSitTime, middleSitTime, aisleSitTime, debugSelection, plane, animationSelection);
			
			//Convert totalBoardTime from seconds to minutes and round to two decimal places
			totalBoardTime = totalBoardTime / 60;
			totalBoardTime = Double.parseDouble(new DecimalFormat("#.##").format(totalBoardTime));
			
			//Calculate average time per passenger
			avgPassengerTime = (double) ((totalBoardTime * 60) / totalPassengers);
			avgPassengerTime = Double.parseDouble(new DecimalFormat("#.##").format(avgPassengerTime));
			
			int report = createResultsPanel(totalBoardTime, totalPassengers, avgPassengerTime, intCapacityInput, percentBagInput);
			
		    //If user clicks Download Results button
		    if (report == 0)
			{
		    	saveResults(totalBoardTime, totalPassengers, capacity, intCapacityInput, plane, totalRows, totalColumns);    	
			}
	   }
	   
	}
	
	//Get passenger capacity for plane layout chosen by user
	private static int getCapacityInput(String planeCapacity)
	{
		//Split layout into array, delimited by / in selected string
		String[] layoutElements = planeCapacity.split("/");
		String digitString = "";
		int i = 0;
		//While current character is a digit, add it to a temp string
		while(Character.isDigit(layoutElements[2].charAt(i)))
		{
			digitString = digitString + layoutElements[2].charAt(i);
			i++;
		}
		
		//Convert string to integer to store plane capacity as int
		int capacity = Integer.parseInt(digitString);
		return capacity;
	}
	
	//Get total number of columns for user selected plane layout
	private static int getTotalColumns(int capacity, String planeCapacity)
	{
		String[] layoutElements = planeCapacity.split("/");
		String seatString = "";
		String columnString = "";
		int i = 0;
		int j = 0;
		while(Character.isDigit(layoutElements[0].charAt(i)))
		{
			seatString = seatString + layoutElements[0].charAt(i);
			i++;
		}
		
		int seatsPerRow = Integer.parseInt(seatString);
		
		while(Character.isDigit(layoutElements[1].charAt(j)))
		{
			columnString = columnString + layoutElements[1].charAt(j);
			j++;
		}
		
		int columnsPerRow = Integer.parseInt(columnString);
		
		return seatsPerRow * columnsPerRow;
	}
	
	//Create GUI Input Panel
	private static int createInputPanel()
	{ 
	    JPanel inputPanel = new JPanel(new GridLayout(0, 1));
	    
	    //Add input fields to JFrame window
	    inputPanel.add(new JLabel("Percent of Plane Filled"));
	    inputPanel.add(percentfilled);
	    
	    inputPanel.add(new JLabel("Percent of Passengers with Bags"));
	    inputPanel.add(percentbags);
	    
	    inputPanel.add(new JLabel("Percent of Passengers with Companions"));
	    inputPanel.add(percentcompanions);
	    
	    inputPanel.add(new JLabel("Plane Layout"));
	    inputPanel.add(planelayout);

	    inputPanel.add(new JLabel("Boarding Method:"));
	    inputPanel.add(boardingmethods);
	    
	    inputPanel.add(new JLabel("Passenger Order"));
	    inputPanel.add(passengerorder);
	    
	    inputPanel.add(new JLabel("Debug Mode"));
	    inputPanel.add(debug);
	    
	    inputPanel.add(new JLabel("Animate"));
	    inputPanel.add(animation);
	    
	    //If user clicks OK in Input Window, set result to 0
	    //If user clicks Cancel in Input Window, set result to 1
	    int result = JOptionPane.showConfirmDialog(null, inputPanel, "Airplane Boarding Simulator",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		return result;
	}
	
	private static int createResultsPanel(double totalBoardTime, int totalPassengers, double avgPassengerTime, int intInput, String percentBagInput)
	{
		//Create Results Window
		JPanel reportPanel = new JPanel(new GridLayout(0, 1));
	    
		//Add simulation results and simulation parameters to Results Window
		reportPanel.add(new JLabel("Boarding Time: " + totalBoardTime + " minutes"));
		reportPanel.add(new JLabel("Total Passengers: " + totalPassengers + " people"));
		reportPanel.add(new JLabel("Average Time Per Passenger: " + avgPassengerTime + " seconds"));
		
	    reportPanel.add(new JLabel("Percent of Passengers with Bags: " + percentBagInput + "%"));
		
		String percentOutput = "" + intInput;
	    reportPanel.add(new JLabel("Percent of Plane Filled: " + percentOutput + "%"));
	    
	    reportPanel.add(new JLabel("Plane Layout: " + String.valueOf(planelayout.getSelectedItem())));
	    reportPanel.add(new JLabel("Boarding Method: " + String.valueOf(boardingmethods.getSelectedItem())));
	    
	    //Add Download button to Results Window, if user clicks button set report = 0
	    String[] options = {"Download Results"};
	    int report = JOptionPane.showOptionDialog(null, reportPanel, "Simulator Results",
		        0, JOptionPane.PLAIN_MESSAGE, null, options, "Download Results");
		
		return report;
	}
	
	private static void saveResults(double totalBoardTime, int totalPassengers, int capacity, int intInput, Passenger[][] plane, int totalRows, int totalColumns)
	{
    	//Display Save As Dialog box
    	JFrame guiFrame = new JFrame();
    	JFileChooser fileDialog = new JFileChooser();
    	int saveChoice = fileDialog.showSaveDialog(guiFrame);
    	
    	//If user chooses file location and enters name of file to save
    	if (saveChoice == JFileChooser.APPROVE_OPTION)
    	{
	    	//Write simulation results and parameters to file at given file path
    		PrintWriter writer = null;
			try {
				writer = new PrintWriter(fileDialog.getSelectedFile() + ".txt", "UTF-8");
			} catch (FileNotFoundException e) {
				//Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				//Auto-generated catch block
				e.printStackTrace();
			}
			writer.println("Boarding Time: " + totalBoardTime + " minutes");
			writer.println("Total Passengers: " + totalPassengers);
			writer.println("Average Time Per Passenger: " + ((totalBoardTime * 60)/totalPassengers) + " seconds");
			writer.println("Plane Layout: 3 seats/2 rows/" + capacity + " capacity");
			writer.println("Boarding Method: " + String.valueOf(boardingmethods.getSelectedItem()));
			String percentOutput = "" + intInput;
			writer.println("Percent of Plane Filled: " + percentOutput + "%");
			writer.println("");
			writer.println("Airplane Layout:");
			writer.println("");
			printAirplaneToFile(plane, totalRows, totalColumns, writer);
			writer.close();
    	}
	}
	
	static void printPassengerDestinations(Passenger[] passengerList)
	{
		for(int i=0; i < passengerList.length - 1; i++)
		{
			System.out.println(passengerList[i].destination + " " + passengerList[i].passengerID + ", ");
		}
		
		System.out.println(passengerList[passengerList.length - 1].destination + " " + passengerList[passengerList.length - 1].passengerID);
	}
	
	static void printPassengerCompanions(Passenger[] passengerList)
	{
		for(int i = 0; i < passengerList.length; i++)
		{
			int companionID = passengerList[i].companionID;
			if(companionID != -1)
				System.out.println("Passenger: " + passengerList[i].passengerID + " has companion Passenger: " + passengerList[i].companionID);
			else
				System.out.println("Passenger: " + passengerList[i].passengerID + " has no companions.");
		}
	}
	
	static void printAirplaneToFile(Passenger[][] plane, int totalRows, int totalColumns, PrintWriter file)
	{
		file.printf("%1s  %12s  %9s  %9s  %9s  %9s  %9s%n", "Column", "A", "B", "C", "D", "E", "F");
		file.printf("%n");
		
		for(int currentRow = 1; currentRow <= totalRows; currentRow++)
		{
			file.printf("%1s  %2s", "Row: ", currentRow);
			for(int currentColumn = 0; currentColumn < totalColumns; currentColumn++)
			{
				Passenger currentPassenger = plane[currentRow - 1][currentColumn];
				file.printf("%11s", currentPassenger.passengerID);
			}
			file.printf("%n");
		}
		
		file.println("");
	}
	
	private static void printSeatList(String[] seatList)
	{
		for(int i = 0; i < seatList.length; i++)
		{
			System.out.println(seatList[i]);
		}
	}
}
