/**
 * 
 */
package jgpstrackedit.map.tilehandler;

import java.awt.Image;


/**
 * @author Hubert
 *
 */
public abstract class AbstractDiskTileCommand extends AbstractTileCommand {

	String baseDirectory;

	/**
	 * @return the baseDirectory
	 */
	public String getBaseDirectory() {
		return baseDirectory;
	}

	/**
	 * @param baseDirectory
	 *            the baseDirectory to set
	 */
	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	/* (non-Javadoc)
	 * @see jgpstrackedit.map.AbstractTileCommand#doAction()
	 */
	@Override
	public void doAction() {
		// TODO Auto-generated method stub

	}

}
