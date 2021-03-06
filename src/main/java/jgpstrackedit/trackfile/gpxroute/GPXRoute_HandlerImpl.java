/*
 * File:           GPXRoute_HandlerImpl.java
 * Date:           20. Mai 2010  10:57
 *
 * @author  hlutnik
 * @version generated by NetBeans XML module
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jgpstrackedit.trackfile.gpxroute;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author hlutnik
 */
public class GPXRoute_HandlerImpl implements GPXRoute_Handler {
    private static final Logger logger = LoggerFactory.getLogger(GPXRoute_HandlerImpl.class);
    private Track track;
    private Point point;
    private boolean metadataFlag = false;

    public Track getTrack() {
        return track;
    }


	public void handle_time(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_time: " + meta);
        }
        if (metadataFlag) {
            track.setTime(data);
        } else {
        	if(point != null) {
        		point.setTime(data);
        	} else {
        		track.setTime(data);
        	}
        }
    }

    public void handle_text(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_text: " + meta);
        }
        track.setLinkText(data);
    }

    public void start_rtept(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_rtept: " + meta);
        }
        point = new Point(meta.getValue("lon"),meta.getValue("lat"));
        track.add(point);
    }

    public void end_rtept() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_rtept()");
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

    public void handle_name(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_name: " + meta);
        }
        if (metadataFlag)
            track.setName(data);
    }

    public void start_rte(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("start_rte: " + meta);
        }
    }

    public void end_rte() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("end_rte()");
        }
    }

    public void handle_copyright(final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_copyright: " + meta);
        }
        track.setCopyright(meta.getValue("author"));
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

    public void handle_ele(final String data, final Attributes meta) throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("handle_ele: " + meta);
        }
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

}
