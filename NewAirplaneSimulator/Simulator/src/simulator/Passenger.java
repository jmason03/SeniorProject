/**
 * 
 */
package simulator;

/**
 * @author Jordan
 *
 */
public class Passenger 
{
	int walkspeed;
	int bags;
	String destination;
	double boardtime;
	boolean personalSpace;
	int cDT;
	int sitTime;
	int targetRow;
	int currentRow;
	int listPosition;
	String targetColumn;
	boolean seated;
	int passengerID;
	int companionID;
	
	
	public Passenger(int curWalkSpeed, int curBags, String curDestination, boolean animationToggle, boolean personalSpaceToggle, int aisleSitTime, int middleSitTime, int windowSitTime, int passengerListPosition, int ID)
	{
		walkspeed = curWalkSpeed;
		bags = curBags;
		destination = curDestination;
		boardtime = 0.0;
		//personalSpace false is equivalent to 1 row and true equivalent to 2 rows
		personalSpace = personalSpaceToggle;
		targetColumn = getTargetColumn(destination);
		targetRow = getPassengerRow(destination);
		currentRow = 1;
		seated = false;
		passengerID = ID;
		companionID = -1;
		listPosition = passengerListPosition;
		if(targetColumn.contains("A") || targetColumn.contains("F"))
			sitTime = windowSitTime;
		else if(targetColumn.contains("B") || targetColumn.contains("E"))
			sitTime = middleSitTime;
		else
			sitTime = aisleSitTime;
	}
		
	public int getBags()
	{
		return bags;
	}
	
	public int setBags(int newBags)
	{
		bags = newBags;
		return bags;
	}
	
	public String getDestination()
	{
		return destination;
	}
	
	public String setDestination(String newDestination)
	{
		destination = newDestination;
		return destination;
	}
	
	public double getBoardTime()
	{
		return boardtime;
	}
	
	public double setBoardTime(double newBoardTime)
	{
		boardtime = newBoardTime;
		return boardtime;
	}
	
	private String getTargetColumn(String destination)
	{
			//Initialize variables used inside for loop
			targetColumn = "";
			
			int curPassengerRow = getPassengerRow(destination);
			
			//Get number of digits in row, this is used to know where at in the destination string the seat column
			//letter is located
			int digitsinRow = String.valueOf(curPassengerRow).length();
			
			//Parse current passenger's column letter from their seat destination
			targetColumn = "" + destination.charAt(digitsinRow);
		
		return targetColumn;
	}
	
	private int getPassengerRow(String destination)
	{
		int j = 0;
		String stringPassengerRow = "";
		
		//Parse current passenger's row number from their seat destination
		while(Character.isDigit(destination.charAt(j)))
		{
			stringPassengerRow = stringPassengerRow + destination.charAt(j);
			j++;
		}
		
		int curPassengerRow = Integer.parseInt(stringPassengerRow);
		
		return curPassengerRow;
	}
	
	public void setCompanionID(int newCompanionID)
	{
		this.companionID = newCompanionID;
	}
}