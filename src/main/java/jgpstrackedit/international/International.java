/**
 * 
 */
package jgpstrackedit.international;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;

/** Handles all localization specific tasks and provides methods and fields to support localization.
 * @author Hubert
 *
 */
public class International {
	
	private static Locale currentLocale = Locale.getDefault();
	private static ResourceBundle stringResources;
		      
	/**
	 * @return the currentLocale
	 */
	public static Locale getCurrentLocale() {
		return currentLocale;
	}

	/**
	 * @param currentLocale the currentLocale to set
	 */
	public static void setCurrentLocale(Locale locale) {
		International.currentLocale = locale;
	}

	public static String getCurrentCountry() {
		return currentLocale.getCountry();
	}
	
	public static String getCurrentLanguage() {
		return currentLocale.getLanguage();
	}

	public static void initialize(String localeName) {
		System.out.println("CurrentLocale="+currentLocale.toString());
		stringResources = ResourceBundle.getBundle("jgpstrackedit.international.jgpstrackeditlang", currentLocale);
	}
	
	public static String getText(String key) {
		return stringResources.getString(key);
	}
	
	/** List of all countries according to ISO-3166
	 * 
	 */
	public static String[] getCountries() {
		
		String[] countries= new String[] { "AT",
			"DE", "FR", "AF", "AX", "AL", "DZ", "AS", "AD", "AO", "AI", "AQ",
			"AG", "AR", "AM", "AW", "AU", "AT", "AZ", "BS", "BH", "BD",
			"BB", "BY", "BE", "BZ", "BJ", "BM", "BT", "BO", "BQ", "BA",
			"BW", "BV", "BR", "IO", "BN", "BG", "BF", "BI", "KH", "CM",
			"CA", "CV", "KY", "CF", "TD", "CL", "CN", "CX", "CC", "CO",
			"KM", "CG", "CD", "CK", "CR", "CI", "HR", "CU", "CW", "CY",
			"CZ", "DK", "DJ", "DM", "DO", "EC", "EG", "SV", "GQ", "ER",
			"EE", "ET", "FK", "FO", "FJ", "FI", "FR", "GF", "PF", "TF",
			"GA", "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GD", "GP",
			"GU", "GT", "GG", "GN", "GW", "GY", "HT", "HM", "VA", "HN",
			"HK", "HU", "IS", "IN", "ID", "IR", "IQ", "IE", "IM", "IL",
			"IT", "JM", "JP", "JE", "JO", "KZ", "KE", "KI", "KP", "KR",
			"KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LI", "LT",
			"LU", "MO", "MK", "MG", "MW", "MY", "MV", "ML", "MT", "MH",
			"MQ", "MR", "MU", "YT", "MX", "FM", "MD", "MC", "MN", "ME",
			"MS", "MA", "MZ", "MM", "NA", "NR", "NP", "NL", "NC", "NZ",
			"NI", "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW",
			"PS", "PA", "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR",
			"QA", "RE", "RO", "RU", "RW", "BL", "SH", "KN", "LC", "MF",
			"PM", "VC", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL",
			"SG", "SX", "SK", "SI", "SB", "SO", "ZA", "GS", "SS", "ES",
			"LK", "SD", "SR", "SJ", "SZ", "SE", "CH", "SY", "TW", "TJ",
			"TZ", "TH", "TL", "TG", "TK", "TO", "TT", "TN", "TR", "TM",
			"TC", "TV", "UG", "UA", "AE", "GB", "US", "UM", "UY", "UZ",
			"VU", "VE", "VN", "VG", "VI", "WF", "EH", "YE", "ZM", "ZW" };
		Arrays.sort(countries);
		return countries;
	}
	

	/**
	 * List of all languages according to ISO 639-1 (Alpha 2 code)
	 */
	public static String[] languages = new String[] {"en","de","fr",
		"ab",
		"aa",
		"ae",
		"af",
		"am",
		"an",
		"ar",
		"as",
		"ay",
		"az",
		"ba",
		"be",
		"bg",
		"bh",
		"bi",
		"bn",
		"bo",
		"br",
		"bs",
		"ca",
		"ce",
		"ch",
		"co",
		"cs",
		"cu",
		"cv",
		"cy",
		"da",
		"de",
		"dv",
		"dz",
		"el",
		"en",
		"eo",
		"es",
		"et",
		"eu",
		"fa",
		"fi",
		"fj",
		"fo",
		"fr",
		"fy",
		"ga",
		"gd",
		"gl",
		"gn",
		"gu",
		"gv",
		"ha",
		"he",
		"hi",
		"ho",
		"hr",
		"ht",
		"hu",
		"hy",
		"hz",
		"ia",
		"id",
		"ie",
		"ii",
		"ik",
		"io",
		"is",
		"it",
		"iu",
		"ja",
		"jv",
		"ka",
		"ki",
		"kj",
		"kk",
		"kl",
		"km",
		"kn",
		"ko",
		"ks",
		"ku",
		"kv",
		"kw",
		"ky",
		"la",
		"lb",
		"li",
		"ln",
		"lo",
		"lt",
		"lv",
		"mg",
		"mh",
		"mi",
		"mk",
		"ml",
		"mn",
		"mo",
		"mr",
		"ms",
		"mt",
		"my",
		"na",
		"nb",
		"nd",
		"ne",
		"ng",
		"nl",
		"nn",
		"no",
		"nr",
		"nv",
		"ny",
		"oc",
		"om",
		"or",
		"os",
		"pa",
		"pi",
		"pl",
		"ps",
		"pt",
		"qu",
		"rm",
		"rn",
		"ro",
		"ru",
		"rw",
		"sa",
		"sc",
		"sd",
		"se",
		"sg",
		"si",
		"sk",
		"sl",
		"sm",
		"sn",
		"so",
		"sq",
		"sr",
		"ss",
		"st",
		"su",
		"sv",
		"sw",
		"ta",
		"te",
		"tg",
		"th",
		"ti",
		"tk",
		"tl",
		"tn",
		"to",
		"tr",
		"ts",
		"tt",
		"tw",
		"ty",
		"ug",
		"uk",
		"ur",
		"uz",
		"vi",
		"vo",
		"wa",
		"wo",
		"xh",
		"yi",
		"yo",
		"za",
		"zh",
		"zu"
	};
	
	

}
