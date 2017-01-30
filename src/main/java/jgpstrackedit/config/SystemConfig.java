/**
 * 
 */
package jgpstrackedit.config;

/**
 * @author hlutnik
 *
 */
public class SystemConfig {
	
	private static String dirSeparatorChar = null;
	
	public static String dirSeparator() {
		if (dirSeparatorChar == null) {
			dirSeparatorChar = "_";
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf("win") >= 0)
				dirSeparatorChar = "\\";
			if (os.indexOf("mac") >= 0)
				dirSeparatorChar = "/";
			if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)
				dirSeparatorChar = "/";
			if (os.indexOf("sunos") >= 0)
				dirSeparatorChar = "/";
		}
		return dirSeparatorChar;
		
	}
	 
}
