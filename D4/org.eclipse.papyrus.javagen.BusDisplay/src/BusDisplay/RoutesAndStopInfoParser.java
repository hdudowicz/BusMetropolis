package BusDisplay;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;


public class RoutesAndStopInfoParser {
    //ArrayList going to contain many route objects
    private ArrayList<Route> routes = new ArrayList<>();


    //Function to parse single route object out of a route
    private static Route parseRoute(String route_info, String timetable_info){
        String[] parts = route_info.split(",");
        String[] timetableParts = timetable_info.split(",");

        ArrayList<LocalTime> schedule = new ArrayList<>();

        //Start after bus route number
        for(int i = 1; i < timetableParts.length; i++){
            schedule.add(LocalTime.parse(timetableParts[i]));
        }

        return new Route(Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                schedule);
    }


    //Going to parse an array list of routes from a given file
    public static ArrayList<Route> parseRoutes (String filename, String ttFilename) throws IOException {
        BufferedReader file = new BufferedReader(
                new FileReader(filename));


        boolean endOf = false;
        ArrayList<Route> routes = new ArrayList<>();

        //Loop reads each line of the file one by one, checking if the next line of the file is not empty
        //which would mean the end of the file, if the end is reached the file is closed and loop is escaped
        //by closing the file and setting endOf to true, if the next line is not empty, route object r is parsed from
        //the line string before being added to the array and the array being returned at the end

        //Skip first line
        file.readLine();
        while (!endOf) {
            BufferedReader timetableFile = new BufferedReader(
                    new FileReader(ttFilename));
            String route_data = file.readLine();
            String timetable_data = "";

            if(route_data == null) {
                file.close();
                endOf = true;
            } else {
                for (String line = timetableFile.readLine(); line != null; line = timetableFile.readLine()){
                    if (line.split(",")[0].equals(route_data.split(",")[0])){
                        timetable_data = line;
                    }
                }
                Route r = parseRoute(route_data, timetable_data);
                routes.add(r);
            }
        }
        return routes;
    }

    //Parses a pair containing a bus stop id and stop name, takes a filename string
    public static Pair<String, String> parseBusInfo(String filename) throws IOException {
        BufferedReader file = new BufferedReader(
                new FileReader(filename));

        String line = file.readLine();

        if(line == null) {
            file.close();
            return null;
        } else {
            String[] parts = line.split(",");
            file.close();
            return new Pair<>(parts[0], parts[1]);
        }

    }
}
