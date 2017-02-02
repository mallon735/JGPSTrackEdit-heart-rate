package jgpstrackedit.trackfile;

import java.io.File;
import java.net.URL;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.asc.ASC;
import jgpstrackedit.trackfile.gpxroute.GPXRoute;
import jgpstrackedit.trackfile.gpxtrack.GPXTrack;
import jgpstrackedit.trackfile.kml.KML;
import jgpstrackedit.trackfile.tcx.TCX;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class TrackFileManagerTest {
	@Test
	public void testOpenTcxFile() throws TrackFileException {
		TrackFileManager.addTrackFile(new GPXRoute());
		TrackFileManager.addTrackFile(new GPXTrack());
		TrackFileManager.addTrackFile(new KML());
		TrackFileManager.addTrackFile(new TCX());
		TrackFileManager.addTrackFile(new ASC());

		final URL fileUrl = this.getClass().getResource("/Jaegerbaek-Hbf.tcx");
		final Track track = TrackFileManager.openTrack(new File(fileUrl.getFile()));
		
		Assert.assertThat(track, CoreMatchers.is(CoreMatchers.notNullValue()));
		Assert.assertThat(track.isValid(), CoreMatchers.is(true));
	}
}
