// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package BusDisplay;

/************************************************************/
/**
 * 
 */
public class BusStopDisplay {
	/**
	 * 
	 */
	public ExpectedBus[] expectedbuses;
	/**
	 * 
	 */
	public BusStopId id;
	/**
	 * 
	 */
	public String name;

	/**
	 * 
	 * @return 
	 */
	public Route[] getCallingRoutes() {
	}

	/**
	 * 
	 * @param routeNo 
	 * @return 
	 */
	public Time[] getDepartureTimes(RouteId routeNo) {
	}

	/**
	 * 
	 * @return 
	 * @param routeNo 
	 * @param t 
	 */
	public Time getTimeOfNextBus(RouteId routeNo, Time t) {
	}

	/**
	 * 
	 * @param t 
	 */
	public void display(Time t) {
	}

	/**
	 * 
	 * @param stopInfo 
	 * @param rsInfo 
	 * @return 
	 * @param ttInfo 
	 */
	public BusStopDisplay create(Data stopInfo, Data rsInfo, Data ttInfo) {

	}
}
