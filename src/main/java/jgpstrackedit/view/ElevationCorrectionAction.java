package jgpstrackedit.view;

import jgpstrackedit.data.Track;
import jgpstrackedit.international.International;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IElevationCorrection;
import jgpstrackedit.map.elevation.IProgressDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Elevation correction action. In this class the {@link IElevationCorrection} impl. is combined with the progress bar an a thread running 
 * asynchron the correction task. 
 *  
 * @author gerdba
 *
 */
public class ElevationCorrectionAction 
{
	private static final Logger logger = LoggerFactory.getLogger(ElevationCorrectionAction.class);
	private static final String PROGRESS_BAR_TEXT_KEY = "menu.Track.Update_Elevation";
	
	private ProgressMonitor progressMonitor;
	private ElevationCorrectionTask task;

	public void elevationCorrectionPerformed(IElevationCorrection elevationCorrection, List<Track> tracks,
			Frame parentComponent) 
	{
		progressMonitor = new ProgressMonitorWithProgressNote(parentComponent,
				International.getText(PROGRESS_BAR_TEXT_KEY), 0, 100);
		progressMonitor.setMillisToDecideToPopup(0);
		progressMonitor.setMillisToPopup(0);
		progressMonitor.setProgress(0);

		task = new ElevationCorrectionTask(elevationCorrection, tracks, parentComponent, progressMonitor);
		new Thread(task).start();
	}

	/**
	 * The elevation correction task as {@link Runnable}
	 * 
	 * @author gerdba
	 *
	 */
	private static class ElevationCorrectionTask implements Runnable {
		private final IElevationCorrection elevationCorrection;
		private final List<Track> tracks;
		private final Frame parentComponent;
		private final ProgressMonitor progressMonitor;
		
		private ElevationCorrectionTask(IElevationCorrection elevationCorrection, List<Track> tracks, Frame parentComponent, ProgressMonitor progressMonitor) {
			this.elevationCorrection = elevationCorrection;
			this.tracks = tracks;
			this.parentComponent = parentComponent;
			this.progressMonitor = progressMonitor;
		}
		
		private void workOnTracks() {
			for(Track track : tracks) {
				try {
					this.elevationCorrection.updateElevation(track, new ProgressDetector(progressMonitor));
				} catch (ElevationException e) {
					logger.error("Error while update elevation!", e);
					if (e.getMessage().equals("OVER_QUERY_LIMIT")) {
						JOptionPane.showMessageDialog(this.parentComponent,
								"The Google-API query limit was reached. Try another day to update elevations!",
								"Google-API-Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		
		@Override
		public void run() {
			try {
				this.workOnTracks();
			} finally {
				this.progressMonitor.close();
			}
		}
	}
	
	/**
	 * Progress detector. Additional check if the user canceled the request. 
	 * 
	 * @author gerdba
	 *
	 */
	private static class ProgressDetector implements IProgressDetector {
		private final ProgressMonitor progressMonitor;
		
		private ProgressDetector(ProgressMonitor progressMonitor) {
			this.progressMonitor = progressMonitor;
		}
		
		@Override
		public void setProgress(int progress) {
			progressMonitor.setProgress(progress);
		}
		
		@Override
		public boolean isCanceled() {
			return progressMonitor.isCanceled();
		}
	}

	/**
	 * Progressbar with a note, containing the progress in percent.
	 * 
	 * @author gerdba
	 *
	 */
	private static class ProgressMonitorWithProgressNote extends ProgressMonitor {
		public ProgressMonitorWithProgressNote(Component parentComponent, String message, int min, int max) {
			super(parentComponent, message, getProgressNote(0), min, max);
		}

		@Override
		public void setProgress(int nv) {
			super.setProgress(nv);
			this.setNote(getProgressNote(nv));
		}

		private static String getProgressNote(int progress) {
			return String.format("        %d%%", progress);
		}
	}
}
