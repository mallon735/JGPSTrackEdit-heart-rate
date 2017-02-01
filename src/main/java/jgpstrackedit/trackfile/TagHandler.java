package jgpstrackedit.trackfile;

public interface TagHandler {
	void handleStartDocument(ParserContext context);
	void handleEndDocument(ParserContext context);
	void handleStartTag(ParserContext context, String qName);
	void handleEndTag(ParserContext context, String qName, String content);
}
