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

/**
 * App
 *
 * Usage: A simple application which returns the current temperature of a city
 * in fahrenheit.
 *
 * @author John Chandara <chandara@iastate.edu>
 * @license MIT License (X11 Variant)
 * @version 190212
 *
 */
public class App {
    private static Scanner scanner;

    public static void main (String[] args) throws UnirestException, UnsupportedEncodingException {
        if (scanner == null)
            scanner = new Scanner (System.in);

        System.out.print ("Please enter your location: ");

        final Map<String, String> mapParams = new HashMap<> ();
        mapParams.put ("q", scanner.nextLine ());
        mapParams.put ("appid", ConfigStore.API_KEY);

        query (mapParams, args);
    }

    /**
     * Fetches the payload and attempts to use it with our local implementation.
     * Uses StatusResponder in order to figure out what to do next.
     *
     * @param mapParams
     * @param args
     * @throws UnsupportedEncodingException
     * @throws UnirestException
     */
    public static void query (Map<String, String> mapParams, String[] args)
            throws UnsupportedEncodingException, UnirestException {
        final Responder responder = new Responder (ConfigStore.BASE_ENDPOINT, fetch (mapParams));
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
    public static double ConvertUnits (double iKelvin) {
        return (iKelvin - 273.15) * 9 / 5 + 32;
    }

    /**
     * Called when the payload has successfully been resolved with our local
     * implementation.
     *
     * @param api
     */
    public static void respond (CurrentWeatherAPI api) {
        final double dTemp = ConvertUnits (api.GetGeneral ().getDouble ("temp"));
        System.out.printf ("Current Temperature: %.2f F", dTemp);
    }

    /**
     * Creates GET request to grab the payload using given paramaters.
     *
     * @param mapParams
     * @return HttpReponse
     * @throws UnsupportedEncodingException
     * @throws UnirestException
     */
    public static HttpResponse<JsonNode> fetch (Map<String, String> mapParams)
            throws UnsupportedEncodingException, UnirestException {
        final String url = HTTP.URL (ConfigStore.BASE_URL + ConfigStore.BASE_ENDPOINT, mapParams);

        return HTTP.Fetch (url);
    }
}