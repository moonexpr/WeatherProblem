package chandara.weather;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    public void testUnitConversion1 () {
        final double results = App.ConvertUnits (252f);

        assertEquals ("252K should be -7F", -7, (int) Math.floor (results));
    }

    public void testUnitConversion2 () {
        final double results = App.ConvertUnits (0);

        assertEquals ("0K should be -460F", -460, (int) Math.floor (results));
    }

    public void testHTTP401 () throws UnsupportedEncodingException, UnirestException {
        final Map<String, String> mapParams = new HashMap<> ();
        mapParams.put ("appid", "starfish22");
        mapParams.put ("q", "Des Moines");

        final HttpResponse<JsonNode> response = App.fetch (mapParams);
        assertEquals ("The appid 'starfish22' should yield a 404 response.", 401,
                response.getBody ().getObject ().getInt ("cod"));
    }

    public void testHTTP404 () throws UnsupportedEncodingException, UnirestException {
        if (ConfigStore.API_KEY.isEmpty ())
            return;

        final Map<String, String> mapParams = new HashMap<> ();
        mapParams.put ("appid", ConfigStore.API_KEY);
        mapParams.put ("q", "starfish22");

        final HttpResponse<JsonNode> response = App.fetch (mapParams);
        assertEquals ("The query 'starfish22' should yield a 404 response.", 404,
                response.getBody ().getObject ().getInt ("cod"));
    }

    public void testHTTP200 () throws UnsupportedEncodingException, UnirestException {
        if (ConfigStore.API_KEY.isEmpty ())
            return;

        final Map<String, String> mapParams = new HashMap<> ();
        mapParams.put ("appid", ConfigStore.API_KEY);
        mapParams.put ("q", "Des Moines");

        final HttpResponse<JsonNode> response = App.fetch (mapParams);
        assertEquals ("The query 'Des Moines' should yield a 200 response.", 200,
                response.getBody ().getObject ().getInt ("cod"));
    }

}
