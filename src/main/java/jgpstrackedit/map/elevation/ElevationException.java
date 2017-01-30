/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.map.elevation;

/**
 *
 * @author hlutnik
 */
public class ElevationException extends Exception {

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
