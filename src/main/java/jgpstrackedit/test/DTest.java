package jgpstrackedit.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class DTest {
	private static final Logger logger = LoggerFactory.getLogger(DTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(Locale.getDefault().toString());
		logger.info(Locale.getAvailableLocales().toString());
	}

}
