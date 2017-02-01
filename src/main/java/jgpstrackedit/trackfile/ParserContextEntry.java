package jgpstrackedit.trackfile;

import org.xml.sax.Attributes;

public class ParserContextEntry {
	private final Attributes attributes;
	private final String qname;
	
	public ParserContextEntry(String qname, Attributes attributes) {
		this.attributes = attributes;
		this.qname = qname;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public String getQname() {
		return qname;
	}	
}
