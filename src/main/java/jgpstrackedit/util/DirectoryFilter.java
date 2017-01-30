/**
 * 
 */
package jgpstrackedit.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/** FileFilter for directories.
 * @author Hubert
 *
 */
public class DirectoryFilter extends FileFilter {

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file) {
		// TODO Auto-generated method stub
		return file.isDirectory();
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Directory";
	}

}
