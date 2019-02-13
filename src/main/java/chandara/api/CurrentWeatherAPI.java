/**
 *
 */
package chandara.api;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * WeatherAPI
 *
 * Usage: Local implementation of OpenWeatherMap's CurrentWeather API intended
 * for accessor use.
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

    /**
     * Returns if the payload is in a valid format.
     *
     * @return Is valid format
     */
    private boolean IsValid () {
        final int code = this.m_jsonDataPrimary.getInt ("cod");
        final boolean bNoMessage = this.m_jsonDataPrimary.isNull ("message");

        return code == 200 && bNoMessage;
    }

    /**
     * Called on construction. Registers JSON objects from main body.
     *
     */
    private void RegisterDataStore () {
        for (final String service : DATA_STORE)
            this.m_arrDataStore.put (service, this.m_jsonDataPrimary.getJSONObject (service));
    }

    /**
     * Returns coordinate information JSON object.
     *
     * @return JSONObject coord
     */
    public JSONObject GetCoordinates () {
        return this.m_arrDataStore.get ("coord");
    }

    /**
     * Returns general information JSON object.
     *
     * @return JSONObject main
     */
    public JSONObject GetGeneral () {
        return this.m_arrDataStore.get ("main");
    }

    /**
     * Returns wind information JSON object.
     *
     * @return JSONObject wind
     */
    public JSONObject GetWind () {
        return this.m_arrDataStore.get ("wind");
    }

    /**
     * Returns cloud information JSON object.
     *
     * @return JSONObject clouds
     */
    public JSONObject GetClouds () {
        return this.m_arrDataStore.get ("clouds");
    }

    /**
     * Returns system information JSON object.
     *
     * @return JSONObject sys
     */
    public JSONObject GetSystemInformation () {
        return this.m_arrDataStore.get ("sys");
    }

}
