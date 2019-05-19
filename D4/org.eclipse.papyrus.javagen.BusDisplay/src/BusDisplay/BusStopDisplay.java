package BusDisplay;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.lang.String;

/**
 * 
 */
public class BusStopDisplay implements Observer {
	public ArrayList<ExpectedBus> expectedBuses;
	public ArrayList<Route> callingRoutes;
	public String id;
	public String name;
    String[][] display = new String[10][5];

//	create is the constructor for the BusStopDisplay, it creates an example state of the system so it involves
//	creating routes and their timetables and involves parsing the given configuration files
	public BusStopDisplay create(String stopInfo, String rsInfo, String ttInfo) throws IOException {
	    BusStopDisplay busStopDisplay = new BusStopDisplay();

		busStopDisplay.id = RoutesAndStopInfoParser.parseBusInfo(stopInfo).getKey();
		busStopDisplay.name = RoutesAndStopInfoParser.parseBusInfo(stopInfo).getValue();

        busStopDisplay.callingRoutes = RoutesAndStopInfoParser.parseRoutes(rsInfo, ttInfo);
        busStopDisplay.addScheduledToExpected();

        return busStopDisplay;
	}


	//Adds scheduled buses from each route calling at the bus stop to the expected buses list sorting them in ascending time
	public void addScheduledToExpected (){
		ArrayList<ExpectedBus> expectedBusList = new ArrayList<>();
		List<Route> callingRoutes = getCallingRoutes();

		for(Route r: callingRoutes){
			for(LocalTime t: r.schedule)
				expectedBusList.add(new ExpectedBus(r.routeNo,
						BusStatus.onTime,
						0,
						r.destination,
						r.schedule.indexOf(t) + 1,
						t));
		}

		Collections.sort(expectedBusList);
		this.expectedBuses = expectedBusList;
	}


	public List<Route> getCallingRoutes() {
		return Collections.unmodifiableList(this.callingRoutes);
	}

	/**
	 *Gets departure times for the route with the routeNo passed as a parameter and returns them as unmodifiable list
	 */
	public List<LocalTime> getDepartureTimes(Integer routeNo) {
		List<LocalTime> departureTimes = new ArrayList<>();
		for(Route r: getCallingRoutes()) {
			if (r.routeNo.equals(routeNo)){
				departureTimes = r.schedule;
			}
		}
		return Collections.unmodifiableList(departureTimes);
	}

	/**
	 *Gets time of the next scheduled bus after the passed time for the route with the passed route number
	 */
	public LocalTime getTimeOfNextBus(Integer routeNo, LocalTime time) {
		LocalTime nextBusTime = LocalTime.now();
		for (Route r: getCallingRoutes()){
			if(r.routeNo.equals(routeNo)){
				LocalTime finalNextBusTime = nextBusTime;
				nextBusTime = r.schedule.stream().filter(x -> finalNextBusTime.isAfter(time)).findFirst().get();
			}
		}
		return nextBusTime;
	}

	/**
	 * Function to display bus times
	 */
	public void display(LocalTime time) {
		ArrayList<ExpectedBus> tempBusList = new ArrayList<>();
		tempBusList.addAll(this.expectedBuses);
		for (ExpectedBus expectedBus: this.expectedBuses){
			if(expectedBus.status == BusStatus.cancelled && expectedBus.time.isBefore(time)){
				tempBusList.remove(expectedBus);
			} else if (expectedBus.time.plusMinutes(expectedBus.delay + 3).isBefore(time)) {
				tempBusList.remove(expectedBus);
			} else if (this.expectedBuses.size() < 10){
				addScheduledToExpected();
			}
		}
		this.expectedBuses = tempBusList;

        /*Display is represented as a grid of cells with rows and columns, the display can show up to 10 rows of buses
        * and information about the bus like it's time and route number is each assigned to it's own column
        * (represented by the second dimension of the 'display' array). The for loop fills these rows and columns in
        * with variables from this BusStopDisplay object
        */
		for (int i = 0; i >= 0 && i < 10 && i < this.expectedBuses.size(); i++){
		    ExpectedBus thisBus = this.expectedBuses.get(i);

			display[i][0] = Integer.toString(i + 1);
            display[i][1] = Integer.toString(thisBus.routeNo);
            display[i][2] = thisBus.destination;
            display[i][3] = thisBus.time.toString();
            display[i][4] = thisBus.status == BusStatus.onTime ?
                    thisBus.delay + " minutes delay" : thisBus.status.toString();
		}
	}


    @Override
    public void update(Observable o, Object arg) {
	    expectedBuses.set(expectedBuses.indexOf(o), (ExpectedBus) o);
    }
}
