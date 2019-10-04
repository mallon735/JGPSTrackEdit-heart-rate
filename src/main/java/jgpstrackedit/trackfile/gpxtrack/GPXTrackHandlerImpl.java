/*
 * File:           GPXTrackHandlerImpl.java
 * Date:           18. Januar 2012  11:05
 *
 * @author  hlutnik
 * @version generated by NetBeans XML module
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgpstrackedit.trackfile.gpxtrack;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * GPXTrackHandlerImpl
 *
 * @author hlutnik
 */
public class GPXTrackHandlerImpl implements GPXTrackHandler {
    private static final Logger logger = LoggerFactory.getLogger(GPXTrackHandlerImpl.class);

    private Track track;
    private Point point;
    private boolean metadataFlag = false;
    private boolean extensionsFlag = false;

    public void handle_gpsies_totalAscentMeter(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_gpsies_totalAscentMeter: " + meta);
        }
    }

    public void handle_text(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_text: " + meta);
        }
        track.setLinkText(data);
    }

    public void start_trkpt(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_trkpt: " + meta);
        }
        point = new Point(meta.getValue("lon"),meta.getValue("lat"));
        track.add(point);
    }

    public void end_trkpt() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_trkpt()");
        }
    }

    public void start_link(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_link: " + meta);
        }
        track.setLink(meta.getValue("href"));
    }

    public void end_link() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_link()");
        }
    }

    public void start_trk(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_trk: " + meta);
        }
    }

    public void end_trk() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_trk()");
        }
    }

    public void start_extensions(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_extensions: " + meta);
        }
        extensionsFlag=true;
    }

    public void end_extensions() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_extensions()");
        }
        extensionsFlag=false;
    }
    
    public void handle_gxxcolor(final String data) throws SAXException{
    	if (extensionsFlag){
            track.setExtensionsColor(data);
    	}
    }

    public void handle_time(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_time: " + meta);
        }
        if (metadataFlag)
            track.setTime(data);
        else
            if (point != null)
            	point.setTime(data);
    }

    public void start_trkseg(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_trkseg: " + meta);
        }
    }

    public void end_trkseg() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_trkseg()");
        }
    }

    public void handle_gpsies_maxHeightMeter(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_gpsies_maxHeightMeter: " + meta);
        }
    }

    public void handle_name(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_name: " + meta);
        }
        if (metadataFlag) {
            track.setName(data);
        } else {
        	if(track.getName() == null || track.getName().length() == 0) {
        		track.setName(data);
        	}
        }
    }

    public void handle_gpsies_totalDescentMeter(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_gpsies_totalDescentMeter: " + meta);
        }
    }

    public void handle_gpsies_property(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_gpsies_property: " + meta);
        }
    }

    public void handle_gpsies_minHeightMeter(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_gpsies_minHeightMeter: " + meta);
        }
    }

    public void start_gpx(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_gpx: " + meta);
        }
        track = new Track();
        StringBuilder gpxAttributes = new StringBuilder();
        for (int i=0; i<meta.getLength(); i++) {
           gpxAttributes.append(meta.getQName(i)+"=\""+
                                   meta.getValue(i)+"\" ");
        }
        track.setGpxAttributes(gpxAttributes.toString());
    }

    public void end_gpx() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_gpx()");
        }
        track.setValid(true);
    }

    public void handle_gpsies_trackLengthMeter(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_gpsies_trackLengthMeter: " + meta);
        }
    }

    public void handle_ele(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_ele: " + meta);
        }
        if (point != null)
            point.setElevation(data);
    }

    public void start_metadata(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_metadata: " + meta);
        }
        metadataFlag = true;
    }

    public void end_metadata() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_metadata()");
        }
        metadataFlag = false;
    }

	public Track getTrack() {
		return track;
	}
    
}
