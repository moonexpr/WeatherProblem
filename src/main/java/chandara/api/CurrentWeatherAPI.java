/**
 *
 */
package chandara.api;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * WeatherAPI
 *
 * Usage:
 *
 * @author John Chandara <chandara@iastate.edu>
 * @license MIT License (X11 Variant)
 * @category Educational
 * @version 190211
 *
 */
public class CurrentWeatherAPI implements API {

    private JSONObject                  m_jsonDataPrimary = null;
    private HashMap<String, JSONObject> m_arrDataStore    = null;
    private final static String[]       DATA_STORE        = {
            "coord", "main", "wind", "clouds", "sys",
    };

    public CurrentWeatherAPI( JSONObject payload ) throws Exception {
        this.m_jsonDataPrimary = payload;

        final int iStatusCode = this.m_jsonDataPrimary.getInt ("cod");
        if (iStatusCode != 200)
            return;

        this.m_arrDataStore = new HashMap<> ();
        this.RegisterDataStore ();
    }

    /*
     * (non-Javadoc)
     *
     * @see chandara.api.API#GetName()
     */
    @Override
    public String GetEndpoint () {
        return "weather";
    }

    /*
     * (non-Javadoc)
     *
     * @see chandara.api.API#GetNextAction(int)
     */
    @Override
    public StatusResponder GetNextAction () {
        final int iStatusCode = this.m_jsonDataPrimary.getInt ("cod");
        switch (iStatusCode) {
            case 200:
                return StatusResponder.CONTINUE;
            case 404:
                System.out.println ("> We cannot find your specified city!");
                return StatusResponder.RESTART;
            case 401:
                System.out.println ("> Failed to fetch payload, API key is malformed or invalid.");
                return StatusResponder.HALT;
            default:
                if (this.IsValid ())
                    return StatusResponder.CONTINUE;
                else
                    return StatusResponder.RESTART;

        }
    }

    private boolean IsValid () {
        final int code = this.m_jsonDataPrimary.getInt ("cod");
        final boolean bNoMessage = this.m_jsonDataPrimary.isNull ("message");

        return code == 200 && bNoMessage;
    }

    private void RegisterDataStore () {
        for (final String service : DATA_STORE)
            this.m_arrDataStore.put (service, this.m_jsonDataPrimary.getJSONObject (service));
    }

    public JSONObject GetCoordinates () {
        return this.m_arrDataStore.get ("coord");
    }

    public JSONObject GetGeneral () {
        return this.m_arrDataStore.get ("main");
    }

    public JSONObject GetWind () {
        return this.m_arrDataStore.get ("wind");
    }

    public JSONObject GetClouds () {
        return this.m_arrDataStore.get ("clouds");
    }

    public JSONObject GetSystemInformation () {
        return this.m_arrDataStore.get ("sys");
    }

}
