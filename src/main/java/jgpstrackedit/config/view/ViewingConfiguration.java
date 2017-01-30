package jgpstrackedit.config.view;

public class ViewingConfiguration {
	
	private static boolean showLength;
	private static boolean showInformation;

	/**
	 * @return the showLength
	 */
	public static boolean isShowLength() {
		return showLength;
	}

	/**
	 * @param showLength the showLength to set
	 */
	public static void setShowLength(boolean showLength) {
		ViewingConfiguration.showLength = showLength;
	}

	/**
	 * @return the showInformation
	 */
	public static boolean isShowInformation() {
		return showInformation;
	}

	/**
	 * @param showInformation the showInformation to set
	 */
	public static void setShowInformation(boolean showInformation) {
		ViewingConfiguration.showInformation = showInformation;
	}
	
	

}
