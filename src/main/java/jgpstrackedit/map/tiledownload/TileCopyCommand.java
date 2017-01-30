/**
 * 
 */
package jgpstrackedit.map.tiledownload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jgpstrackedit.map.tilehandler.AbstractDiskTileCommand;
import jgpstrackedit.map.util.TileNumber;

/**
 * @author hlutnik
 *
 */
public class TileCopyCommand extends AbstractDiskTileCommand {

	private String sourceFile;
	private String destinationFile;
	private TileNumber tileNumber;
	private CopyErrorObserver copyErrorObserver;
	
	/**
	 * @param tileNumber the tileNumber to set
	 */
	public void setTileNumber(TileNumber tileNumber) {
		this.tileNumber = tileNumber;
	}
	
	public TileNumber getTileNumber() {
		return this.tileNumber;
	}
	/**
	 * @param copyErrorObserver the copyErrorObserver to set
	 */
	public void setCopyErrorObserver(CopyErrorObserver copyErrorObserver) {
		this.copyErrorObserver = copyErrorObserver;
	}
	public TileCopyCommand(String sourceFile, String destinationFile, TileNumber tileNumber, CopyErrorObserver observer) {
		setSourceFile(sourceFile);
		setDestinationFile(destinationFile);
		setTileNumber(tileNumber);
		setCopyErrorObserver(observer);
	}
	/**
	 * @return the sourceFile
	 */
	public String getSourceFile() {
		return sourceFile;
	}

	/**
	 * @param sourceFile the sourceFile to set
	 */
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * @return the destinationFile
	 */
	public String getDestinationFile() {
		return destinationFile;
	}

	/**
	 * @param destinationFile the destinationFile to set
	 */
	public void setDestinationFile(String destinationFile) {
		this.destinationFile = destinationFile;
	}

	/* (non-Javadoc)
	 * @see jgpstrackedit.map.tilehandler.AbstractDiskTileCommand#doAction()
	 */
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		try {
			System.out.println("CopyCommand: "+getSourceFile()+" -> "+getDestinationFile());
			Thread.yield();
			copy(getSourceFile(),getDestinationFile());
		} catch (FileNotFoundException e) {
			copyErrorObserver.errorOccured(getTileNumber());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void copy(String source, String dest) throws IOException {
		File file = new File(dest);
		if (!file.exists()) {
		BufferedInputStream in = new BufferedInputStream(
				new FileInputStream(source));
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(dest));
		int b = in.read();
		while (b != -1) {
			out.write((byte)b);
			b = in.read();
		}
		in.close();
		out.close();	
		}
	}

}
