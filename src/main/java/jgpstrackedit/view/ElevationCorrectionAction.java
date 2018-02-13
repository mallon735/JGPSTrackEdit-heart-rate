package jgpstrackedit.view;

import java.awt.Component;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import jgpstrackedit.data.Track;
import jgpstrackedit.international.International;
import jgpstrackedit.map.elevation.ElevationException;
import jgpstrackedit.map.elevation.IElevationCorrection;
import jgpstrackedit.map.elevation.IProgressDetector;

public class ElevationCorrectionAction implements PropertyChangeListener {
	private ProgressMonitor progressMonitor;
	private ElevationCorrectionTask task;

	public void elevationCorrectionPerformed(IElevationCorrection elevationCorrection, List<Track> tracks,
			Frame parentComponent) 
	{
		progressMonitor = new ProgressMonitorWithProgressNote(parentComponent,
				International.getText("menu.Track.Update_Elevation"), 0, 100);
		progressMonitor.setMillisToDecideToPopup(0);
		progressMonitor.setMillisToPopup(0);

		task = new ElevationCorrectionTask(elevationCorrection, tracks, parentComponent);
		task.addPropertyChangeListener(this);
		task.execute();
	}

	/**
	 * Invoked when task's progress property changes. Update the progress monitor.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName() && evt.getNewValue() != null) {
			int progress = Integer.class.cast(evt.getNewValue());
			progressMonitor.setProgress(progress);
		}

		if (progressMonitor.isCanceled()) {
			task.cancel(true);
		}

		if (task.isDone()) {
			progressMonitor.close();
		}
	}

	/**
	 * The elevation correction task as {@link SwingWorker} thread.
	 * 
	 * @author gerdba
	 *
	 */
	private static class ElevationCorrectionTask extends SwingWorker<Void, Void> {
		private final IElevationCorrection elevationCorrection;
		private final List<Track> tracks;
		private final Frame parentComponent;
		
		private ElevationCorrectionTask(IElevationCorrection elevationCorrection, List<Track> tracks, Frame parentComponent) {
			this.elevationCorrection = elevationCorrection;
			this.tracks = tracks;
			this.parentComponent = parentComponent;
		}
		
		private void progagateProgress(int progress) {
			this.setProgress(progress);
		}
		
		@Override
		public Void doInBackground() {
			setProgress(0);
			for(Track track : tracks) {
				try {
					this.elevationCorrection.updateElevation(track, new ProgressDetector(this));
				} catch (ElevationException e) {
					e.printStackTrace();
					if (e.getMessage().equals("OVER_QUERY_LIMIT")) {
						JOptionPane.showMessageDialog(this.parentComponent,
								"The Google-API query limit was reached. Try another day to update elevations!",
								"Google-API-Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			return null;
		}
	}
	
	private static class ProgressDetector implements IProgressDetector {
		private final ElevationCorrectionTask elevationCorrectionTask;
		
		private ProgressDetector(ElevationCorrectionTask elevationCorrectionTask) {
			this.elevationCorrectionTask = elevationCorrectionTask;
		}
		
		@Override
		public void setProgress(int progress) {
			elevationCorrectionTask.progagateProgress(progress);
		}
		
	}

	/**
	 * Progressbar with a note, containing the progress in percent.
	 * 
	 * @author gerdba
	 *
	 */
	private static class ProgressMonitorWithProgressNote extends ProgressMonitor {
		public ProgressMonitorWithProgressNote(Component parentComponent, Object message, int min, int max) {
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
