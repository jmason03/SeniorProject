package simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class CalculateTimeDisplaySim 
{
	public CalculateTimeDisplaySim()
	{
		
	}
	
	double calculateBoardTime(int boardingMethod, Passenger[] passengerList, int totalRows, int totalColumns, double totalBoardTime, int moveToRow, int moveToBack, int windowSitTime, int middleSitTime, int aisleSitTime, int debugSelection, Passenger[][] plane, int animationSelection) throws InterruptedException
	{
		if(boardingMethod == 0)
		{
			//Look at each passenger in passengerList to add their board time to totalBoardTime
			for (int i = 0; i < passengerList.length; i++)
			{
				//Initialize variables used inside for loop
				String curPassengerColumn = "";
				
				int curPassengerRow = getPassengerRow(passengerList, i);
				
				//Get number of digits in row, this is used to know where at in the destination string the seat column
				//letter is located
				int digitsinRow = String.valueOf(curPassengerRow).length();
				
				//Parse current passenger's column letter from their seat destination
				curPassengerColumn = "" + passengerList[i].destination.charAt(digitsinRow);
								
				//Add time for next person in line to move to their row after the person ahead of them
				//has sat down
				totalBoardTime = totalBoardTime + moveToRow;
				
				totalBoardTime = addBackToFrontTime(passengerList, curPassengerColumn, curPassengerRow, moveToBack, totalBoardTime, windowSitTime, middleSitTime, aisleSitTime, i);
			}
			
		}
		
		else if(boardingMethod == 1)
		{
			for (int i = 0; i < passengerList.length; i++)
			{
				//Initialize variables used inside for loop
				String curPassengerColumn = "";
				
				int curPassengerRow = getPassengerRow(passengerList, i);
				
				//Get number of digits in row, this is used to know where at in the destination string the seat column
				//letter is located
				int digitsinRow = String.valueOf(curPassengerRow).length();
				
				//Parse current passenger's column letter from their seat destination
				curPassengerColumn = "" + passengerList[i].destination.charAt(digitsinRow);
				
				totalBoardTime = addFrontToBackTime(passengerList, curPassengerColumn, curPassengerRow, moveToBack, moveToRow, totalBoardTime, windowSitTime, middleSitTime, aisleSitTime, i);
			}
		}
		
		else if(boardingMethod == 2)
		{
			totalBoardTime = addAlternatingWMATime(totalRows, moveToBack, totalBoardTime, windowSitTime, middleSitTime, aisleSitTime);
		}
		
		//Assert that boardingMethod = 3
		else
		{
			totalBoardTime = addRandomBoardingTime(passengerList, totalBoardTime, totalRows, totalColumns, debugSelection, plane, animationSelection);
		}
		return totalBoardTime;
	}
	
	private double addBackToFrontTime(Passenger[] passengerList, String curPassengerColumn, int curPassengerRow, int moveToBack, double totalBoardTime, int windowSitTime, int middleSitTime, int aisleSitTime, int i)
	{
		//Add 4 sec * total number of rows to account for time first passenger needs to
		//walk to the back of the plane
		if(i == (passengerList.length - 1))
		{
			totalBoardTime = totalBoardTime + (curPassengerRow * moveToBack);
		}
		
		//Add time for passenger to sit in their seat, based on seat's column
		if(curPassengerColumn.contains("A") || curPassengerColumn.contains("F"))
		{
			totalBoardTime = totalBoardTime + windowSitTime;
		}
		
		if(curPassengerColumn.contains("B") || curPassengerColumn.contains("E"))
		{
			totalBoardTime = totalBoardTime + middleSitTime;
		}
		
		if(curPassengerColumn.contains("C") || curPassengerColumn.contains("D"))
		{
			totalBoardTime = totalBoardTime + aisleSitTime;
		}
		
		return totalBoardTime;
	}
	
	private double addFrontToBackTime(Passenger[] passengerList, String curPassengerColumn, int curPassengerRow, int moveToBack, int moveToRow, double totalBoardTime, int windowSitTime, int middleSitTime, int aisleSitTime, int i)
	{
		//Add time for passenger to sit in their seat, based on seat's column
		if(curPassengerColumn.contains("A") || curPassengerColumn.contains("F"))
		{
			totalBoardTime = totalBoardTime + windowSitTime + moveToRow;
		}
		
		else if(curPassengerColumn.contains("B") || curPassengerColumn.contains("E"))
		{
			totalBoardTime = totalBoardTime + middleSitTime + moveToRow;
		}
		
		else if(curPassengerColumn.contains("C"))
		{
			totalBoardTime = totalBoardTime + aisleSitTime + moveToRow;
		}
		
		//Assert that curPassengerColumn must equal D
		//Add windowSitTime + time for person behind curPassenger to move to next row
		else 
		{
			totalBoardTime = totalBoardTime + aisleSitTime + (2 * moveToRow);
		}
		
		return totalBoardTime;
	}
	
	private double addAlternatingWMATime(int totalRows, int moveToBack, double totalBoardTime, int windowSitTime, int middleSitTime, int aisleSitTime)
	{
		int walkToBack = moveToBack * totalRows;

		totalBoardTime = totalBoardTime + (walkToBack * 6) + (2 * windowSitTime) + (2 * middleSitTime) + (2 * aisleSitTime);

		return totalBoardTime;
	}
	
	double addRandomBoardingTime(Passenger[] passengerList, double totalBoardTime, final int totalRows, final int totalColumns, int debugSelection, Passenger[][] plane, int animationSelection) throws InterruptedException
	{
		//Initialize variables
		JTable dataTable = null;
		
		if(animationSelection == 1)
		{
			dataTable = initializeAnimationWindow(totalColumns, totalRows);
			JPanel animationPanel = new JPanel();
		    String[] options = {"Download Results"};
		    int report = JOptionPane.showOptionDialog(null, animationPanel, "Simulator Results",
			        0, JOptionPane.PLAIN_MESSAGE, null, options, "Download Results");
		}
	
		Passenger[] activePassengers = new Passenger[1];
		activePassengers[0] = passengerList[0];
		int clockTicks = 0;
		int passengerPositionAdded = 0;
		int extraStandUpTime = 0;
		
		//While there are still active passengers
		while(activePassengers.length > 0)
		{
			//Initialize rowBlocked to be greater than totalRows
			//Indicates that no row is currently blocked
			int rowBlocked = totalRows + 1;
			
			//For each passenger in the current active passenger list
			for(int i = 0; i < activePassengers.length; i++)
			{
				//If activePassengers[i].cDT = 0
				//If passenger has reached their target row
				if(activePassengers[i].targetRow == activePassengers[i].currentRow && activePassengers[i].cDT == 0)
				{
					//Set current passenger cDT to their sit time + 1 and indicate that current row
					//is blocked
					activePassengers[i].cDT = activePassengers[i].sitTime + 1;
					rowBlocked = activePassengers[i].targetRow;
					
					extraStandUpTime = checkPassengerSeats(activePassengers[i], plane);
					extraStandUpTime = extraStandUpTime + (activePassengers[i].bags * 4);
					activePassengers[i].cDT = activePassengers[i].cDT + extraStandUpTime;
				}
				
				//If rowBlocked is not the current passenger's next row, assert that the 
				//row is free for a new passenger to be added
				//to the active passenger list and move up to row 1
				if(rowBlocked != activePassengers[i].currentRow + 1 && activePassengers[i].cDT < 1)
				{
					activePassengers[i].currentRow = activePassengers[i].currentRow + 1;
				}
				
				//If passenger cDT is greater than one, decrement cDT
				if(activePassengers[i].cDT > 1)
				{
					activePassengers[i].cDT = activePassengers[i].cDT - 1;
					rowBlocked = activePassengers[i].targetRow;
				}
				
				//If the passenger cDT is 1, that person is assumed to have sat down
				//and is removed from active passenger list
				else if(activePassengers[i].cDT == 1)
				{
					passengerList[activePassengers[i].listPosition].seated = true;
					int columnNumber = getAlphabetPosition(activePassengers[i].targetColumn);
					plane[activePassengers[i].targetRow - 1][columnNumber - 1] = activePassengers[i];
					activePassengers = deletePassengerAtPosition(activePassengers, i);
					i--;
				}
				
				else
				{
					rowBlocked = activePassengers[i].currentRow;
				}
				
			}
			
			//If rowBlocked is greater than 1, assert that row 1 is free and a new passenger
			//can be added to the active passenger list
			if(rowBlocked > 1 && passengerPositionAdded < passengerList.length - 1 && activePassengers.length < totalRows + 1)
			{
				Passenger[] tempPassengers = addPassenger(passengerList, activePassengers);
				
				//Increment passengerPositionAdded to reference the next passenger in passengerList
				//that has not yet been added to the active Passengers list
				passengerPositionAdded++;
				
				tempPassengers[tempPassengers.length - 1] = passengerList[passengerPositionAdded];
				
				activePassengers = tempPassengers;
				
			}
			
			//Increment clockTicks
			clockTicks++;
			
			//Debug Mode is enabled
			if(debugSelection == 1)
			{
				printDebugChart(activePassengers);
			}
			
			//Animation is enabled
			if(animationSelection == 1)
			{
				printAirplaneToWindow(plane, activePassengers, totalRows, totalColumns, dataTable);
				Thread.sleep(100);
			}
		}
		
		return clockTicks + totalBoardTime;
	}
	
	
	private int getPassengerRow(Passenger[] passengerList, int i)
	{
		//Initialize Variables
		int j = 0;
		String stringPassengerRow = "";
		
		//Parse current passenger's row number from their seat destination
		while(Character.isDigit(passengerList[i].destination.charAt(j)))
		{
			stringPassengerRow = stringPassengerRow + passengerList[i].destination.charAt(j);
			j++;
		}
		
		int curPassengerRow = Integer.parseInt(stringPassengerRow);
		
		return curPassengerRow;
	}
	
	private int passengerStandUpTime(int targetRow, String targetColumn, Passenger[][] plane)
	{
		//Initialize variables
		int standUpTime = 0;
		int columnNumber = getAlphabetPosition(targetColumn);
		
		//If there is a passenger sitting at the given position, add necessary time to current passenger cDT
		//position != -1
		if(plane[targetRow - 1][columnNumber - 1] != null)
		{
			standUpTime = (plane[targetRow - 1][columnNumber - 1].sitTime * 2);
		}
		
		return standUpTime;
	}
	
	int checkPassengerSeats(Passenger currentPassenger, Passenger[][] plane)
	{
		int extraTime = 0;
		
		//Examine any seat that is in the way of current passenger
		//Check if a passenger is already sitting there
		if(currentPassenger.targetColumn.contains("A"))
		{
			extraTime = passengerStandUpTime(currentPassenger.targetRow, "B", plane);
			extraTime = passengerStandUpTime(currentPassenger.targetRow, "C", plane) + extraTime;
		}	
		
		else if(currentPassenger.targetColumn.contains("B"))
		{
			extraTime = passengerStandUpTime(currentPassenger.targetRow, "C", plane);
		}
		
		else if(currentPassenger.targetColumn.contains("E"))
		{
			extraTime = passengerStandUpTime(currentPassenger.targetRow, "D", plane);
		}
		
		else if(currentPassenger.targetColumn.contains("F"))
		{
			extraTime = passengerStandUpTime(currentPassenger.targetRow, "D", plane);
			extraTime = passengerStandUpTime(currentPassenger.targetRow, "E", plane) + extraTime;
		}
		
		return extraTime;
	}
	
	public static Passenger[] deletePassengerAtPosition(Passenger[] activePassengers, int i)
	{		
		//Create temp list to store copies of still active passengers
		Passenger[] tempPassengers = new Passenger[activePassengers.length - 1];
		
		//For each passenger before the one that will be deleted
		for(int j = 0; j < i; j++)
		{
			tempPassengers[j] = activePassengers[j];
		}
		
		//For each passenger after the one that will be deleted
		for(int k = i; k < tempPassengers.length; k++)
		{
			tempPassengers[k] = activePassengers[k + 1];
		}
		
		//Replace the old active passenger list with the new temp list
		activePassengers = tempPassengers;
		
		return activePassengers;
	}
	
	//Adds a passenger to the active passenger list
	Passenger[] addPassenger(Passenger[] passengerList, Passenger[] activePassengers)
	{
		//Create temp list to copy old passengers into
		Passenger[] tempPassengers = new Passenger[activePassengers.length + 1];
		
		//For each old passenger + one new passenger add them to active passenger list
		for(int j = 0; j < tempPassengers.length - 1; j++)
		{
			tempPassengers[j] = activePassengers[j];
		}
		
		activePassengers = tempPassengers;
		
		return activePassengers;
	}
	
	//If user selects Debug Mode, chart of all active passengers is printed
	//to the screen for each clock tick.
	void printDebugChart(Passenger[] activePassengers)
	{
		//Format Column Headings
		System.out.printf("%1s  %-10s   %-10s  %-14s%n", "Passenger ID", "Current Row", "Destination", "CDT");
		
		//For each member of the active passengers list, print the necessary information
		for(int i = 0; i < activePassengers.length; i++)
		{
			Passenger currentPassenger = activePassengers[i];
			System.out.printf("%7d  %11s   %11s   %6d%n", currentPassenger.passengerID, currentPassenger.currentRow, currentPassenger.destination, currentPassenger.cDT);
		}
		
		System.out.println("");
	}
	
	//Returns an integer value that represents a letter's position in the alphabet
	int getAlphabetPosition(String targetColumn)
	{
		//Get ascii value of A (base letter)
		int firstLetter = 'A';
		
		//Get ascii value of targetColumn
		char passengerColumn = targetColumn.charAt(0);
		int ascii = (int) passengerColumn;
		
		//The difference + 1 represents targetColumns position in the alphabet
		int columnNumber = ascii - firstLetter + 1;
		
		return columnNumber;
	}
	
	static void printAirplaneToWindow(Passenger[][] plane, Passenger[] activePassengers, int totalRows, int totalColumns, JTable dataTable)
	{
		//Determine int postion of aisle row
		int halfColumns = (totalColumns + 2) / 2;
		
		//Reset Aisle Row to Blank Values
		for(int i = 0; i < totalRows; i++)
		{
			dataTable.setValueAt("", i, halfColumns);
		}
		
		//Print each value in activePassengers and seated passengers to their corresponding table cell
		for(int i = 0; i < totalRows; i++)
		{
			dataTable.setValueAt("Row: " + (i + 1), i, 0);
			
			for(int j = 1; j < halfColumns; j++)
			{
				if(plane[i][j - 1] != null)
					dataTable.setValueAt(plane[i][j - 1].passengerID, i, j);
				else
					dataTable.setValueAt("", i, j);
			}
			
			if(i < activePassengers.length && activePassengers[activePassengers.length - i - 1] != null)
				dataTable.setValueAt(activePassengers[activePassengers.length - i - 1].passengerID, activePassengers[activePassengers.length - i - 1].currentRow - 1, halfColumns);
			else if(dataTable.getValueAt(i, halfColumns) != "")
			{
				//Do Nothing
			}
			else
				dataTable.setValueAt("", i, halfColumns);
			
			for(int j = totalColumns + 1; j > halfColumns; j--)
			{
				if(plane[i][j - 2] != null)
					dataTable.setValueAt(plane[i][j - 2].passengerID, i, j);
				else
					dataTable.setValueAt("", i, j);
			}
		} 
		
		dataTable.repaint();
	}
	
	static JTable initializeAnimationWindow(int totalColumns, int totalRows)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Vector<String> columnHeaders = new Vector<String>();
	    Vector<Vector> rowData = new Vector<Vector>();
		columnHeaders.addElement("Columns");
		char columnCharTitle = 'A';
		int halfColumns = totalColumns / 2;
		
		for(int i = 0; i < halfColumns; i++)
		{
		    String columnTitle = "" + columnCharTitle;
			columnHeaders.addElement(columnTitle);
			columnCharTitle++;
		}
		
		columnHeaders.addElement("Aisle");
		
		for(int i = halfColumns; i < totalColumns; i++)
		{
		    String columnTitle = "" + columnCharTitle;
			columnHeaders.addElement(columnTitle);
			columnCharTitle++;
		}
		
		for(int i = 0; i < totalRows; i++)
		{
		    Vector<String> currentRow = new Vector<String>();
		    
		    for(int j = 0; j < totalColumns + 1; j++)
		    {
		    	currentRow.addElement("");
		    }
		    
		    rowData.addElement(currentRow);
		}
	    
	    JTable dataTable = new JTable(rowData, columnHeaders);

	    JScrollPane scrollPane = new JScrollPane(dataTable);
	    frame.add(scrollPane, BorderLayout.CENTER);
	    frame.setSize(600, 1200);
	    frame.setVisible(true);
	    
		return dataTable;
	}
}
