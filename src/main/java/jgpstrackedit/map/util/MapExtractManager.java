/**
 * 
 */
package jgpstrackedit.map.util;

import jgpstrackedit.data.Point;
import jgpstrackedit.util.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.TreeMap;

/** 
 * Manages map extracts.
 * 
 * @author Hubert
 *
 */
public class MapExtractManager 
{
	private static Logger logger = LoggerFactory.getLogger(MapExtractManager.class);
	private static TreeMap<String,MapExtract> mapExtracts = new TreeMap<String,MapExtract>();
	
	public static void add(MapExtract mapExtract) {
		mapExtracts.put(mapExtract.getName(),mapExtract);
	}
	
	public static void remove(MapExtract mapExtract) {
		mapExtracts.remove(mapExtract.getName());
	}

	public static void remove(String mapExtract) {
		mapExtracts.remove(mapExtract);
	}

    public static void add(String mapExtractAsString) {
    	String[] token = mapExtractAsString.split(";");
    	mapExtracts.put(token[0],new MapExtract(token[0],Parser.parseInt(token[1]),token[2],token[3]));
    }
    
	public static void add(String mapExtractName, int zoomLevel,
			Point upperLeftBoundary) {
		mapExtracts.put(mapExtractName,new MapExtract(mapExtractName,zoomLevel,upperLeftBoundary));
	}

	public static String[] mapExtractNames() {
		String[] ret = new String[mapExtracts.size()];
		int index = 0;
		for (String name:mapExtracts.keySet()) {
			ret[index++] = name;
		}
		return ret;
	}
	
    public static void load() {
    	// Load country default map extracts
    	add("AE;7;49.237552;27.139010");
    	add("AT;7;8.862796;49.395033");
    	add("AR;4;-96.495602;-21.968955");
    	add("AU;4;95.962405;-5.976619");
    	add("BD;6;81.669192;28.598060");
    	add("BE;7;-0.381642;52.363617");
    	add("BR;4;-92.403684;12.560015");
    	add("BY;6;17.860599;56.865856");
    	add("CA;3;-166.292233;78.379433");
    	add("CH;7;3.523441;48.433225");
    	add("CL;3;-142.023434;7.261640");
    	add("CN;4;66.398196;53.298711");
    	add("CZ;7;11.201238;51.376000");
    	add("CO;5;-91.826901;14.484714");
    	add("DE;6;-0.156979;54.229910");
    	add("DK;6;-0.058102;58.400616");
    	add("DZ;5;-15.878415;37.494440");
    	add("EG;6;22.079349;31.499091");
    	add("ES;6;-11.648679;44.006606");
    	add("ET;5;21.705814;20.313302");
    	add("FR;6;-8.402219;50.376040");
    	add("GR;6;15.987987;42.255370");
    	add("HK;10;113.585849;22.723315");
    	add("HU;7;14.905276;49.006630");
    	add("ID;4;84.657474;17.181997");
    	add("IL;7;31.263919;33.755657");
    	add("IN;4;39.976077;37.771677");
    	add("IQ;6;36.075931;37.869297");
    	add("IT;5;-4.952512;48.363064");
    	add("KN;6;28.649173;5.612303");
    	add("KR;6;118.616214;41.639995");
    	add("MA;6;-15.647702;36.519263");
    	add("MD;7;24.364505;48.759764");
    	add("MK;8;19.272342;42.621561");
    	add("MY;5;85.294681;18.115917");
    	add("NG;6;-1.700558;14.586205");
    	add("NL;7;0.815311;53.915456");
    	add("PH;5;102.752445;22.619391");
    	add("PK;5;51.105228;39.800635");
    	add("PL;6;9.368167;55.496627");
    	add("PT;6;-15.625729;43.905667");
    	add("QA;8;49.034305;26.499549");
    	add("RO;6;15.355716;49.546769");
    	add("RS;7;15.520999;46.179937");
    	add("RU;3;25.682864;75.414347");
    	add("SE;4;-20.536618;71.989395");
    	add("SI;8;12.740970;46.997823");
    	add("SK;7;13.944530;50.358697");
    	add("TH;5;84.086185;22.393078");
    	add("TN;6;-0.003171;38.264011");
    	add("TR;6;25.628421;43.000566");
    	add("UA;5;13.202396;55.794431");
    	add("UG;7;28.294864;4.217504");
    	add("UK;5;-23.513913;62.052838");
    	add("US;4;-131.602902;54.540020");
    	add("VN;5;86.261478;26.920061");
    	add("LAST_MAP_EXTRACT;7;8.862796;49.395033");
    	//
    	try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("MapExtracts.dat")));
            String mapExtractAsString = in.readLine();
            while (mapExtractAsString != null) {
            	add(mapExtractAsString);
            	mapExtractAsString = in.readLine();
            }
            in.close();
		} catch (Exception e) {
			logger.error("Cannot read map extract manager file!", e);
		}
    }
    
	public static void save() {
		try {
			logger.info("MapExtractManager: save");
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("MapExtracts.dat"))));
			for (MapExtract mapExtract:mapExtracts.values()) {
				out.println(mapExtract.getName()+";"+mapExtract.getZoomLevel()+";"+
			                mapExtract.getUpperLeftBoundary().getLongitudeAsString()+";"+
			                mapExtract.getUpperLeftBoundary().getLatitudeAsString());
				/*logger.info(mapExtract.getName()+";"+mapExtract.getZoomLevel()+";"+
		                mapExtract.getUpperLeftBoundary().getLongitudeAsString()+";"+
		                mapExtract.getUpperLeftBoundary().getLatitudeAsString());*/
			}
			out.close();
		} catch (IOException e) {
			logger.error("Cannot save map extract manager file!", e);
		}
	}

	public static MapExtract get(String selectedMapExtractName) {
		return mapExtracts.get(selectedMapExtractName);
	}

	public static boolean contains(String mapExtractName) {
		return mapExtracts.containsKey(mapExtractName);
	}

}
