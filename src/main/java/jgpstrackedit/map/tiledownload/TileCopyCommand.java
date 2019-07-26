/**
 * 
 */
package jgpstrackedit.map.tiledownload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jgpstrackedit.map.tilehandler.AbstractDiskTileCommand;
import jgpstrackedit.map.util.TileNumber;

/**
 * @author hlutnik
 *
 */
public class TileCopyCommand extends AbstractDiskTileCommand 
{
	private static Logger logger = LoggerFactory.getLogger(TileCopyCommand.class);
	
	private String sourceFile;
	private String destinationFile;
	private CopyErrorObserver copyErrorObserver;
		
	public TileCopyCommand(String sourceFile, String destinationFile, TileNumber tileNumber, CopyErrorObserver observer) {
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
		this.setTileNumber(tileNumber);
		this.copyErrorObserver = observer;
	}

	@Override
	public void doAction() {
		try {
			logger.info(String.format("copy file %s to %s", this.sourceFile, this.destinationFile));
			copy(this.sourceFile, this.destinationFile);
		} catch (IOException e) {
			logger.error(String.format("copy file %s to %s", this.sourceFile, this.destinationFile),e);
			copyErrorObserver.errorOccured(getTileNumber());
		}
	}
	
	public static void copy(String source, String dest) throws IOException {
		Files.copy(new File(source).toPath(),new File(dest).toPath());
	}
}
