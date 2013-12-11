/**
 * 
 */
package simulator;

/**
 * @author Jordan
 *
 */
public class SeatList 
{
	
	public SeatList()
	{
		
	}
	
	public String[] createSeatList(int totalPassengers, int totalRows, int totalColumns)
	{
		//Declare array that will store list of seats
				String[] seatList = new String[totalPassengers];
				
				//Initialize variables to add first seat location to seatList
				int currentRow = totalRows;
				char currentColumn = 'C';
				int currentPosition = 0;
				boolean leftSideComplete = false;
				boolean rightSideComplete = false;
				
				//For every position in seatList, add a seat location
				for(int i = 0; i < totalRows; i++)
				{
					for(int j = 0; j < totalColumns / 2; j++)
					{
						currentPosition = (i * totalColumns) + j;
						seatList[currentPosition] = "" + currentRow + currentColumn;
						currentColumn--;
					}
					
					currentColumn = 'D';
					
					for(int k = totalColumns / 2; k < totalColumns; k++)
					{
						currentPosition = (i * totalColumns) + k;
						seatList[currentPosition] = "" + currentRow + currentColumn;
						currentColumn++;
					}
					
					currentColumn = 'C';
					currentRow--;					
				}
				return seatList;
	}
	
	public String[] createReverseSeatList(int totalPassengers, int totalRows, int totalColumns)
	{
		//Declare array that will store list of seats
		String[] seatList = new String[totalPassengers];
		
		//Initialize variables to add first seat location to seatList
		int currentRow = totalRows;
		char currentColumn = 'A';
		int currentPosition = 0;
		boolean leftSideComplete = false;
		boolean rightSideComplete = false;
		
		//For every position in seatList, add a seat location
		for(int i = 0; i < totalRows; i++)
		{
			for(int j = 0; j < totalColumns / 2; j++)
			{
				currentPosition = (i * totalColumns) + j;
				seatList[currentPosition] = "" + currentRow + currentColumn;
				currentColumn++;
			}
			
			currentColumn = 'F';
			
			for(int k = totalColumns / 2; k < totalColumns; k++)
			{
				currentPosition = (i * totalColumns) + k;
				seatList[currentPosition] = "" + currentRow + currentColumn;
				currentColumn--;
			}
			
			currentColumn = 'A';
			currentRow--;					
		}
		
		return seatList;
	}
	
	public String[] createWMASeatList(int totalPassengers, int totalRows, int totalColumns)
	{
		//Declare array that will store list of seats
		String[] seatList = new String[totalPassengers];
		
		//Initialize variables to add first seat location to seatList
		int currentRow = totalRows;
		char currentColumn = 'G';
		int currentSeat = 0;
		
		
		for(int j = 0; j < totalColumns; j++)
		{
			if(currentColumn == 'D')
				currentColumn = 'C';
			else if(currentColumn == 'B')
				currentColumn = 'D';
			else if(currentColumn == 'E')
				currentColumn = 'B';
			else if(currentColumn == 'A')
				currentColumn = 'E';
			else if(currentColumn == 'F')
				currentColumn = 'A';
			else if(currentColumn == 'G')
				currentColumn = 'F';
			currentRow = totalRows;
			//For every position in seatList, add a seat location
			for(int i = 0; i < totalRows; i++)
			{
				String destination = "" + currentRow + currentColumn;
				seatList[currentSeat] = destination;
				currentSeat = currentSeat + 1;
				currentRow--;
			}
		}
		return seatList;
	}
}
