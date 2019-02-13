/**
 *
 */
package chandara.api;

/**
 * API
 *
 * Usage: Specified how to implement a weather API.
 *
 * @author John Chandara <chandara@iastate.edu>
 * @license MIT License (X11 Variant)
 * @category Educational
 * @version 190211
 *
 */
public interface API {
    String GetEndpoint ();

    StatusResponder GetNextAction ();
}
