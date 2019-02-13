/**
 *
 */
package chandara.weather;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * HTTP
 *
 * Usage: Static helper class containing HTTP utilities.
 *
 * @author John Chandara <chandara@iastate.edu>
 * @license MIT License (X11 Variant)
 * @version 190211
 *
 */
public class HTTP {

    /**
     * Generates a url based on base url and paramaters.
     *
     * @param strBaseURL
     * @param mapParams
     * @return formatted url
     * @throws UnsupportedEncodingException
     */
    public static String URL (String strBaseURL, Map<String, String> mapParams) throws UnsupportedEncodingException {
        if (mapParams.size () > 0) {
            strBaseURL += "?";
            int iParamsLeft = mapParams.size ();
            for (final Map.Entry<String, String> entry : mapParams.entrySet ()) {
                iParamsLeft --;
                strBaseURL += String.format ("%s=%s%s", URLEncoder.encode (entry.getKey (), "UTF-8"),
                        URLEncoder.encode (entry.getValue (), "UTF-8"), iParamsLeft != 0 ? "&" : "");
            }
        }

        return strBaseURL;
    }

    /**
     * Sends HTTP GET request and returns the response.
     *
     * @param url
     * @return httpresponse
     * @throws UnirestException
     */
    public static HttpResponse<JsonNode> Fetch (String url) throws UnirestException {
        return Unirest.get (url).asJson ();
    }

}
