package jgpstrackedit.trackfile;

import java.io.File;
import java.net.URL;

import jgpstrackedit.data.Track;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class TrackFileManagerTest {
	@Test
	public void testOpenTcxFile() throws TrackFileException {
		final URL fileUrl = this.getClass().getResource("/Jaegerbaek-Hbf.tcx");
		Track track = TrackFileManager.openTrack(new File(fileUrl.getFile()));
		Assert.assertThat(track, CoreMatchers.is(CoreMatchers.notNullValue()));
	}
}
