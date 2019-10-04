package jgpstrackedit.trackfile;

import jgpstrackedit.data.Point;
import jgpstrackedit.data.Track;
import org.xml.sax.Attributes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ParserContext {
	private final Stack<ParserContextEntry> context;
	private final List<Track> tracks;
	private Track currentTrack;
	private Point currentPoint;
	
	public ParserContext() {
		this.context = new Stack<>();
		this.tracks = new LinkedList<>();
		this.currentTrack = new Track();
		this.tracks.add(this.currentTrack);
		this.currentPoint = null;
	}

	public Point getCurrentPoint() {
		return currentPoint;
	}

	public void setCurrentPoint(Point currentPoint) {
		this.currentPoint = currentPoint;
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
	
	public Track addNewTrack() {
		this.currentTrack = new Track();
		this.tracks.add(this.currentTrack);
		return currentTrack;
	}

	public void removeEmptyTracks() {
		final List<Track> rewrittenTracks = this.tracks
				.stream()
				.filter(track -> track.getNumberPoints() > 1)
				.collect(Collectors.toList());
		this.tracks.clear();
		this.tracks.addAll(rewrittenTracks);
	}
	
	public List<Track> getTracks() {
		return Collections.unmodifiableList(this.tracks);
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
