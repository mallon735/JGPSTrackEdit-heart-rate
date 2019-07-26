package jgpstrackedit.map.elevation.mapquest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import jgpstrackedit.data.Track;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IProgressDetector;
import jgpstrackedit.trackfile.asc.ASC;

/**
 * Unit-Test for {@link MapQuestElevationCorrection}
 * 
 * If there are cases where no height data exists, the resulting height value is -32768.
 * In the cases of no height data, a statuscode of 601 ("No Data Error: No Valid Height Data Exists.") 
 * or 602 ("Partial Success: Some elevations were found but others were not within the Shuttle Radar
 * Topography Mission (SRTM) coverage.") will be returned. 
 * 
 * @author gerdba
 *
 */
public class MapQuestElevationCorrectionStatus602Test 
{
	private Track track = new Track();
	
	@Before
	public void setup() throws Exception {
		final URL fileUrl = this.getClass().getResource("/pointlist602.txt");
		ASC ascTrackFile = new ASC();
		track = ascTrackFile.openTrack(new File(fileUrl.getFile())).get(0);
	}
	
	
	@Test
	public void testUpdateElevationStatus602() throws ElevationException {
		MapQuestElevationCorrection mapQuestElevationCorrection = new TestMapQuestElevationCorrection();
		mapQuestElevationCorrection.updateElevation(track, new IProgressDetector() {
			@Override
			public void setProgress(int progress) {				
			}

			@Override
			public boolean isCanceled() {
				return false;
			}});
		
		assertThat(track.getPoint(0).getElevation(), is(626D));
		assertThat(track.getPoint(1).getElevation(), is(623D));
		assertThat(track.getPoint(2).getElevation(), is(0D));
		assertThat(track.getPoint(3).getElevation(), is(0D));
		assertThat(track.getPoint(4).getElevation(), is(627D));
	}
	
	private static class TestMapQuestElevationCorrection extends MapQuestElevationCorrection 
	{
		@Override
		InputStream openUrlStream(String request) throws IOException {
			final URL fileUrl = this.getClass().getResource("/mapquest_ec_status602.json");
			return new FileInputStream(new File(fileUrl.getFile()));
		}
	}
}
