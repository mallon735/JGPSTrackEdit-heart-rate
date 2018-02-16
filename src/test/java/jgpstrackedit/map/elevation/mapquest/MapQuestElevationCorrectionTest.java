package jgpstrackedit.map.elevation.mapquest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IProgressDetector;
import jgpstrackedit.map.elevation.PointWrapper;

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
		Assert.assertThat(eapi.compress(getPoints(track)), is("{rcxHgpojAFc@Fe@??Gd@Gb@"));
	}
	
	@Test
	public void testUpdateElevation() throws ElevationException {
		MapQuestElevationCorrection mapQuestElevationCorrection = new TestMapQuestElevationCorrection();
		mapQuestElevationCorrection.updateElevation(track, new IProgressDetector() {
			@Override
			public void setProgress(int progress) {				
			}});
		
		int idx = 0;
		for(Point point : track.getPoints()) {
			String msg = String.format("Wrong elevation on point with index %d. Elevation is %f", idx, point.getElevation());
			assertThat(msg, point.getElevation(), is(119D));
			idx += 1;
		}
	}
	
	private static class TestMapQuestElevationCorrection extends MapQuestElevationCorrection 
	{
		private static final String RESPONSE = "{\"elevationProfile\":[{\"distance\":0,\"height\":119},{\"distance\":0.0133,\"height\":119},{\"distance\":0.0273,\"height\":119},{\"distance\":0.0273,\"height\":119},{\"distance\":0.0413,\"height\":119},{\"distance\":0.0546,\"height\":119}],\"info\":{\"statuscode\":0,\"copyright\":{\"imageAltText\":\"© 2017 MapQuest, Inc.\",\"imageUrl\":\"http://api.mqcdn.com/res/mqlogo.gif\",\"text\":\"© 2017 MapQuest, Inc.\"},\"messages\":[]}}";

		@Override
		InputStream openUrlStream(String request) throws IOException {
			return new ByteArrayInputStream(RESPONSE.getBytes());
		}
	}
	
	private List<PointWrapper> getPoints(Track track) {
		List<PointWrapper> list = new LinkedList<>();
		for(int i=0; i<track.getNumberPoints(); i++) {
			list.add(new PointWrapper(track.getPoint(i), i));
		}
		return list;
	}
}
