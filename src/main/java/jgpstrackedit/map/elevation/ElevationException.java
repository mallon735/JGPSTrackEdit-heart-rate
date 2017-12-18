package jgpstrackedit.map.elevation;

/**
 * Exception during update the elevation.
 * 
 * @author hlutnik
 */
public class ElevationException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new instance of <code>ElevationException</code> without detail message.
     */
    public ElevationException() {
    }

    /**
     * Constructs an instance of <code>ElevationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ElevationException(String msg) {
        super(msg);
    }
}
