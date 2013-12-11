package simulator;

import java.util.Random;

public class PassengerList 
{
	PassengerList()
	{
		
	}
	
	Passenger[] createPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, double percentBags)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		for (int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		return passengerList;
	}
	
	Passenger[] createRandomPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, double percentBags)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		for (int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		
		Random randomGenerator = new Random();
		for (int j = 0; j < passengerList.length; j++)
		{
			int randomposition = randomGenerator.nextInt(passengerList.length);
			Passenger tempPassenger = passengerList[j];
			passengerList[j] = passengerList[randomposition];
			passengerList[randomposition] = tempPassenger;
			passengerList[randomposition].listPosition = randomposition;
		}
		return passengerList;
	}
	
	Passenger[] createZoneRandomPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, int totalZones, double percentBags)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		//Determine number of passengers in each zone
		int passengersPerZone = (int) totalPassengers / totalZones;
		
		//Create ordered list of passengers
		for (int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		
		//For each zone, randomly swap passenger positions for passengers within current zone
		for (int k = 1; k <= totalZones; k++)
		{				
			for (int j = (k - 1) * passengersPerZone; j < (k * passengersPerZone); j++)
			{
				//k * passengersPerZone = highest (last) passenger position in current zone
				//(k - 1) * passengerPerZone = lowest (first) passenger position in current zone
				int upperLimit = k * passengersPerZone;
				int lowerLimit = (k - 1) * passengersPerZone;
				int randomposition = lowerLimit + (int)(Math.random() * (upperLimit - lowerLimit));
				Passenger tempPassenger = passengerList[j];
				passengerList[j] = passengerList[randomposition];
				passengerList[randomposition] = tempPassenger;
				passengerList[randomposition].listPosition = randomposition;
			}
		}
		return passengerList;
	}
	
	Passenger[] createReversePassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, double percentBags)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		for (int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[i], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[i], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		return passengerList;
	}
	
	Passenger[] createWMAPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, double percentBags)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		for (int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[i], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[i], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		
		return passengerList;
	}
	
	Passenger[] createWMARandomPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, double percentBags, int totalRows, int totalColumns)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		for (int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[i], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[i], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		
		//For each zone, randomly swap passenger positions for passengers within current zone
		for (int k = 1; k <= totalColumns; k++)
		{				
			for (int j = (k - 1) * totalRows; j < (k * totalRows); j++)
			{
				//k * passengersPerZone = highest (last) passenger position in current zone
				//(k - 1) * passengerPerZone = lowest (first) passenger position in current zone
				int upperLimit = k * totalRows;
				int lowerLimit = (k - 1) * totalRows;
				int randomposition = lowerLimit + (int)(Math.random() * (upperLimit - lowerLimit));
				Passenger tempPassenger = passengerList[j];
				passengerList[j] = passengerList[randomposition];
				passengerList[randomposition] = tempPassenger;
				passengerList[randomposition].listPosition = randomposition;
			}
		}
		
		return passengerList;
	}
	
	Passenger[] createAlternateRowsPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, int totalColumns, double percentBags)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		int currentSeat = 0;
		int offSet = totalColumns * 2;
		
		for(int i = 0; i < totalPassengers; i++)
		{
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[currentSeat * offSet], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[currentSeat * offSet], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			currentSeat++;
		}
		
		return passengerList;
	}
	
	Passenger[] createPyramidPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, int totalColumns, double percentBags)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersPerBagAssignment = 0;
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		for (int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		
		return passengerList;
	}
	
	Passenger[] createRandomCompanionsPassengerList(int totalPassengers, String[] seatList, int aisleSitTime, int middleSitTime, int windowSitTime, int totalColumns, double percentBags, double percentCompanions)
	{
		int passengersWithBags = (int) (totalPassengers * percentBags);
		int passengersWithCompanions = (int) (totalPassengers * percentCompanions);
		int passengersPerBagAssignment = 0;
		int companionAssignmentFreq = 0;
		
		if(passengersWithBags == 0)
			passengersPerBagAssignment = 100000; //Set to large number so that no passengers get bags
		else
			passengersPerBagAssignment = totalPassengers / passengersWithBags;
		
		if(passengersWithCompanions == 0)
			companionAssignmentFreq = 100000; //Set to large number so that no passengers get companions
		else
			companionAssignmentFreq = (totalPassengers / passengersWithCompanions) * 2;
		
		//Create list of passengers and assign them to a seat in seatList
		Passenger[] passengerList = new Passenger[totalPassengers];
		
		for(int i = 0; i < totalPassengers; i++)
		{ 
			if(i % passengersPerBagAssignment == 0)
				passengerList[i] = new Passenger(5, 1, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
			else
				passengerList[i] = new Passenger(5, 0, seatList[seatList.length - (i + 1)], false, false, aisleSitTime, middleSitTime, windowSitTime, i, i);
		}
		
		for(int i = 1; i < passengerList.length - 1; i++)
		{
			if(i % companionAssignmentFreq == 0)
			{
				if(passengerList[i].destination.contains("C") || passengerList[i].destination.contains("D"))
				{
					//Set companion of passengerList[i - 1] to passengerList[i]
					passengerList[i - 1].setCompanionID(passengerList[i].passengerID);
					passengerList[i].setCompanionID(passengerList[i - 1].passengerID);
				}
				
				else
				{
					//Set companion of passengerList[i] to be passengerList [i + 1]
					passengerList[i].setCompanionID(passengerList[i + 1].passengerID);
					passengerList[i + 1].setCompanionID(passengerList[i].passengerID);
				}
			}
		}
		
		Random randomGenerator = new Random();
		Passenger[] newPassengerList = new Passenger[passengerList.length];
		int positionAdded = 0;
		
		while(passengerList.length > 0)
		{
			int randomPosition = randomGenerator.nextInt(passengerList.length);
			if(randomPosition > 0 && passengerList[randomPosition].companionID == passengerList[randomPosition - 1].passengerID)
			{
				newPassengerList[positionAdded] = passengerList[randomPosition - 1];
				positionAdded++;
				newPassengerList[positionAdded] = passengerList[randomPosition];
				positionAdded++;
				passengerList = CalculateTime.deletePassengerAtPosition(passengerList, randomPosition);
				passengerList = CalculateTime.deletePassengerAtPosition(passengerList, randomPosition - 1);
			}
			
			else if(randomPosition < (passengerList.length - 1) && passengerList[randomPosition].companionID == passengerList[randomPosition + 1].passengerID)
			{
				newPassengerList[positionAdded] = passengerList[randomPosition];
				positionAdded++;
				newPassengerList[positionAdded] = passengerList[randomPosition + 1];
				positionAdded++;
				passengerList = CalculateTime.deletePassengerAtPosition(passengerList, randomPosition + 1);
				passengerList = CalculateTime.deletePassengerAtPosition(passengerList, randomPosition);
			}
			
			else
			{
				newPassengerList[positionAdded] = passengerList[randomPosition];
				positionAdded++;
				passengerList = CalculateTime.deletePassengerAtPosition(passengerList, randomPosition);
			}
			
		}
		
		return newPassengerList;
	}
}
