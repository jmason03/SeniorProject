package simulator;

public class TestSimulations {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int totalPassengers = 204;
		int totalRows = totalPassengers / 6;
		int aisleSitTime = 1;
		int middleSitTime = 2;
		int windowSitTime = 3;
		double percentBags = 0.5;
		
		SeatList tempSeatList = new SeatList();
		String[] seatList = tempSeatList.createSeatList(totalPassengers, totalRows, windowSitTime);
		
		PassengerList tempPasList = new PassengerList();
		Passenger[] passengerList = tempPasList.createPassengerList(totalPassengers, seatList, aisleSitTime, middleSitTime, windowSitTime, percentBags);
		
		Passenger[] activePassengers = new Passenger[4];
		activePassengers[0] = passengerList[0];
		activePassengers[1] = passengerList[1];
		activePassengers[2] = passengerList[2];
		activePassengers[3] = passengerList[3];
		
		activePassengers = deletePassengerAtPosition(activePassengers, passengerList, 0);
		
		System.out.println(activePassengers.length);
	}
	
	static Passenger[] deletePassengerAtPosition(Passenger[] activePassengers, Passenger[] passengerList, int i)
	{
		//Indicate that passenger has seated
		passengerList[activePassengers[i].listPosition].seated = true;
		
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
		
		activePassengers = tempPassengers;
		
		return activePassengers;
	}

}
