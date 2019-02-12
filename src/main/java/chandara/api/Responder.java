/**
 *
 */
package chandara.api;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

/**
 * Responder
 *
 * Usage: The primary purpose of this class is the act as a aggregator of common
 * information between the various APIs offered from OpenWeatherMap.
 *
 * @author John Chandara <chandara@iastate.edu>
 * @license MIT License (X11 Variant)
 * @version 190211
 *
 */
public class Responder {
    ;
    private API     m_apiEndpoint = null;
    private boolean m_bFailed     = false;

    public Responder( String strEndpoint, HttpResponse<JsonNode> response ) {
        final JSONObject payload = response.getBody ().getObject ();

        try {
            switch (strEndpoint) {
                case "weather":
                    this.m_apiEndpoint = new CurrentWeatherAPI (payload);
                    break;
                default:
                    break;
            }
        } catch (final Exception e) {
            this.m_bFailed = true;
        }
    }

    public StatusResponder GetNextAction () {
        if (this.m_bFailed)
            return StatusResponder.RESTART;

        return this.m_apiEndpoint.GetNextAction ();
    }

    public API GetAPI () {
        return this.m_apiEndpoint;
    }

}
