package jgpstrackedit.trackfile;

import java.util.Stack;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;

import org.xml.sax.Attributes;

public class ParserContext {
	private final Stack<ParserContextEntry> context;
	private final Track currentTrack;
	private Point curentPoint;
	
	public ParserContext(Track currentTrack) {
		this.context = new Stack<>();
		this.currentTrack = currentTrack;
		this.curentPoint = null;
	}

	public Point getCurentPoint() {
		return curentPoint;
	}

	public void setCurentPoint(Point curentPoint) {
		this.curentPoint = curentPoint;
	}

	public Stack<ParserContextEntry> getContext() {
		return context;
	}
	
	public ParserContextEntry getParserContextEntry(int idx) {
		try {
			return context.get(context.size() - 1 - idx);
		} catch(Exception e) {
			return new ParserContextEntry("", null);
		}
	}
	
	public void pushContextEntry(String qname, Attributes attributes) {
		this.context.push(new ParserContextEntry(qname, attributes));
	}

	public Track getCurrentTrack() {
		return currentTrack;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(ParserContextEntry pce : this.context) {
			builder.append("/");
			builder.append(pce.getQname());
		}
		
		return builder.toString();
	}
	
	
}
