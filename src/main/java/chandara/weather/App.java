package chandara.weather;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import chandara.api.CurrentWeatherAPI;
import chandara.api.Responder;
import chandara.api.StatusResponder;

public class App {
    private final static String BASE_URL      = "https://api.openweathermap.org/data/2.5/";
    private final static String BASE_ENDPOINT = "weather";
    private final static String API_KEY       = "";

    private static Scanner      scanner;

    public static void main (String[] args) throws UnirestException, UnsupportedEncodingException {
        if (scanner == null)
            scanner = new Scanner (System.in);

        final Map<String, String> mapParams = prompt ();

        query (mapParams, args);
    }

    private static void query (Map<String, String> mapParams, String[] args)
            throws UnsupportedEncodingException, UnirestException {
        final Responder responder = new Responder (BASE_ENDPOINT, fetch (mapParams));
        final StatusResponder statusResponder = responder.GetNextAction ();

        switch (statusResponder) {
            case RESTART:
                main (args);
                break;
            case CONTINUE:
                final CurrentWeatherAPI api = (CurrentWeatherAPI) responder.GetAPI ();
                respond (api);
                break;
            case RETRY:
                query (mapParams, args);
                break;
            default:
                main (args);
                break;

        }
    }

    /**
     * Converts from Kevin to Fahrenheit.
     *
     * @param iKelvin
     * @return Fahrenheit representation
     */
    private static double ConvertUnits (double iKelvin) {
        return (iKelvin - 273.15) * 9 / 5 + 32;
    }

    private static void respond (CurrentWeatherAPI api) {
        final double dTemp = ConvertUnits (api.GetGeneral ().getDouble ("temp"));
        System.out.printf ("Current Temperature: %.2f F", dTemp);
    }

    private static Map<String, String> prompt () {
        System.out.print ("Please enter your location: ");

        final Map<String, String> mapParams = new HashMap<> ();
        mapParams.put ("q", scanner.nextLine ());
        mapParams.put ("appid", API_KEY);

        return mapParams;
    }

    public static HttpResponse<JsonNode> fetch (Map<String, String> mapParams)
            throws UnsupportedEncodingException, UnirestException {
        final String url = HTTP.URL (BASE_URL + BASE_ENDPOINT, mapParams);

        return HTTP.Fetch (url);
    }
}