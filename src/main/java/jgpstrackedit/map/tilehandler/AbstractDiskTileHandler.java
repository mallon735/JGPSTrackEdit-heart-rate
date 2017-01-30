/**
 * 
 */
package jgpstrackedit.map.tilehandler;

/**
 * @author Hubert
 *
 */
public abstract class AbstractDiskTileHandler extends AbstractTileHandler {

	private String baseDirectory;
	
	/**
	 * @return the baseDirectory
	 */
	public String getBaseDirectory() {
		return baseDirectory;
	}

	/**
	 * @param baseDirectory the baseDirectory to set
	 */
	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

}
