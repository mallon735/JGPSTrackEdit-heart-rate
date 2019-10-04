package jgpstrackedit.data.util;

import java.awt.*;
import java.util.ArrayList;

/**
 * Java Code to get a color name from rgb/hex value/awt color
 *
 * The part of looking up a color name from the rgb values is edited from
 * https://gist.github.com/nightlark/6482130#file-gistfile1-java (that has some errors) by Ryan Mast (nightlark)
 *
 * @author Xiaoxiao Li
 *
 */
public class ColorUtils {
	private ArrayList<ColorName> colorList;
	private int nextColorIndex;
	public ColorUtils()
	{
		colorList = initColorList();
		nextColorIndex = 0;
	}

    /**
     * Initialize the color list that we have.
     * 	<xsd:enumeration value="Black"/>
		<xsd:enumeration value="DarkRed"/>
		<xsd:enumeration value="DarkGreen"/>
		<xsd:enumeration value="DarkYellow"/>
		<xsd:enumeration value="DarkBlue"/>
		<xsd:enumeration value="DarkMagenta"/>
		<xsd:enumeration value="DarkCyan"/>
		<xsd:enumeration value="LightGray"/>
		<xsd:enumeration value="DarkGray"/>
		<xsd:enumeration value="Red"/>
		<xsd:enumeration value="Green"/>
		<xsd:enumeration value="Yellow"/>
		<xsd:enumeration value="Blue"/>
		<xsd:enumeration value="Magenta"/>
		<xsd:enumeration value="Cyan"/>
		<xsd:enumeration value="White"/>
		<xsd:enumeration value="Transparent"/>
     */
    private ArrayList<ColorName> initColorList() {
        ArrayList<ColorName> colorList = new ArrayList<ColorName>();
        colorList.add(new ColorName("Black", 0x00, 0x00, 0x00));
        colorList.add(new ColorName("Red", 0xFF, 0x00, 0x00));
        colorList.add(new ColorName("Blue", 0x00, 0x00, 0xFF));
        colorList.add(new ColorName("Cyan", 0x00, 0xFF, 0xFF));
        colorList.add(new ColorName("Green", 0x00, 0x80, 0x00));
        colorList.add(new ColorName("DarkYellow", 0x8B, 0x8B, 0x00));
        colorList.add(new ColorName("LightGray", 0xD3, 0xD3, 0xD3));
        colorList.add(new ColorName("Magenta", 0xFF, 0x00, 0xFF));
        colorList.add(new ColorName("DarkGray", 0xA9, 0xA9, 0xA9));
        colorList.add(new ColorName("DarkGreen", 0x00, 0x64, 0x00));
        colorList.add(new ColorName("Yellow", 0xFF, 0xFF, 0x00));
        colorList.add(new ColorName("DarkRed", 0x8B, 0x00, 0x00));
        colorList.add(new ColorName("DarkCyan", 0x00, 0x8B, 0x8B));
        colorList.add(new ColorName("Gray", 0x80, 0x80, 0x80));
        colorList.add(new ColorName("DarkMagenta", 0x8B, 0x00, 0x8B));
        colorList.add(new ColorName("White", 0xFF, 0xFF, 0xFF));
        colorList.add(new ColorName("DarkBlue", 0x00, 0x00, 0x8B));

        return colorList;
    }

    /**
     * Get the closest color name from our list
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public String getColorNameFromRgb(int r, int g, int b) {
        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : colorList) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }

        if (closestMatch != null) {
            return closestMatch.getName();
        } else {
            return "Black";
        }
    }

    /**
     * Convert hexColor to rgb, then call getColorNameFromRgb(r, g, b)
     *
     * @param hexColor
     * @return
     */
    public String getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
    }

    public int colorToHex(Color c) {
        return Integer.decode("0x"
                + Integer.toHexString(c.getRGB()).substring(2));
    }

    public String getColorNameFromColor(Color color) {
        return getColorNameFromRgb(color.getRed(), color.getGreen(),
                color.getBlue());
    }

    public Color getColorFromName(String colorname) {
    	Color _col = new Color(0,0,0);
    	for (ColorName c : colorList) {
            if( c.getName().equals(colorname) ){
            	_col = new Color(c.getR(),c.getG(), c.getB() );
            	break;
            }
        }
       return _col;
    }

    public Color getNextColor() {
		ColorName colname= colorList.get( nextColorIndex );
		nextColorIndex++;
		if( nextColorIndex >= colorList.size() ){
			nextColorIndex = 0;
		}

        return new Color(colname.getR(),colname.getG(), colname.getB() );
    }
    
    public void reset() {
		nextColorIndex = 0;
    }


    /**
     * SubClass of ColorUtils. In order to lookup color name
     *
     * @author Xiaoxiao Li
     *
     */
    public class ColorName {
        public int r, g, b;
        public String name;

        public ColorName(String name, int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
        }

        public int computeMSE(int pixR, int pixG, int pixB) {
            return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
                    * (pixB - b)) / 3);
        }

        public int getR() {
            return r;
        }

        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }

        public String getName() {
            return name;
        }
    }
}