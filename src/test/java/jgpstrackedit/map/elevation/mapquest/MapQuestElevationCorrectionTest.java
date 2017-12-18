package jgpstrackedit.map.elevation.mapquest;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.map.elevation.google.GoogleElevationCorrection;

/**
 * Unit-Test for {@link MapQuestElevationCorrection}
 * 
 * Example request: http://open.mapquestapi.com/elevation/v1/profile?key=Fmjtd%7Cluubn96ynu%2C2s%3Do5-907guw&shapeFormat=cmp&outShapeFormat=none&latLngCollection={rcxHgpojAFc@Fe@??Gd@Gb@
 * Response: {"elevationProfile":[{"distance":0,"height":119},{"distance":0.0133,"height":119},{"distance":0.0273,"height":119},{"distance":0.0273,"height":119},{"distance":0.0413,"height":119},{"distance":0.0546,"height":119}],"info":{"statuscode":0,"copyright":{"imageAltText":"© 2017 MapQuest, Inc.","imageUrl":"http://api.mqcdn.com/res/mqlogo.gif","text":"© 2017 MapQuest, Inc."},"messages":[]}}
 * response with error: {"elevationProfile":[],"info":{"statuscode":500,"copyright":{"imageAltText":"© 2017 MapQuest, Inc.","imageUrl":"http://api.mqcdn.com/res/mqlogo.gif","text":"© 2017 MapQuest, Inc."},"messages":["Error processing request: String index out of range: 16 Please see the documentation for the Elevation Service at http://open.mapquestapi.com/elevation/ for details on correctly formatting requests."]}}
 * 
 * @author gerdba
 *
 */
public class MapQuestElevationCorrectionTest 
{
	private final Track track = new Track();
	
	@Before
	public void setup() {
		track.add(new Point(12.372676784D, 51.305579927D, 0D));
		track.add(new Point(12.372855184D, 51.305536627D, 0D));
		track.add(new Point(12.373045184D, 51.305504927D, 0D));
		
		track.add(new Point(12.373045184D, 51.305504927D, 0D));
		track.add(new Point(12.372855184D, 51.305536627D, 0D));
		track.add(new Point(12.372676784D, 51.305579927D, 0D));
	}
	
	
	/**
	 * Test compressing points for mapquest elevation request. 
	 */
	@Test
	public void testCompress() {
		MapQuestElevationCorrection eapi = new MapQuestElevationCorrection();
		Assert.assertThat(eapi.compress(track.getPoints()), is("{rcxHgpojAFc@Fe@??Gd@Gb@"));
	}
}
