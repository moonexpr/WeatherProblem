/**
 *
 */
package chandara.weather;

import junit.framework.TestCase;

/**
 * RepoTests
 *
 * @author John Chandara <chandara@iastate.edu>
 * @license MIT License (X11 Variant)
 * @version 190212
 *
 */
public class RepoTests extends TestCase {

    public void testToken () {
        assertEquals ("API Key should not be on the repository!", "", ConfigStore.API_KEY);
    }

    public void testBaseURL () {
        assertNotSame ("Base URL should be specified.", "", ConfigStore.BASE_URL);
    }

    public void testEndpoint () {
        assertNotSame ("Endpoint should be specified.", "", ConfigStore.BASE_ENDPOINT);
    }

}
