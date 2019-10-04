package jgpstrackedit.trackfile;

import jgpstrackedit.data.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Common XML parser
 *
 * @author gerdba
 */
public class XmlParser 
{
	private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);
	private final XmlContentHandler xmlContentHandler;
	
	public XmlParser(TagHandler tagHandler) {
		this.xmlContentHandler = new XmlContentHandler(tagHandler);
	}
	
	public void parse(final java.net.URL url) throws SAXException,
			ParserConfigurationException, IOException {
		parse(new org.xml.sax.InputSource(url.toExternalForm()));
	}

	public void parse(final org.xml.sax.InputSource input) throws SAXException,
			ParserConfigurationException, IOException {
		javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory
				.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false); 
		XMLReader parser = factory.newSAXParser().getXMLReader();
		parser.setContentHandler(xmlContentHandler);
		parser.setErrorHandler(new XmlParserErrorHandler());
		parser.parse(input);
	}
	
	public List<Track> getTrack() {
		return this.xmlContentHandler.getTracks();
	}

	private static class XmlContentHandler implements ContentHandler {
		private final ParserContext context;
		private final TagHandler tagHandler;
		private transient StringBuilder contentBuffer;
		
		public XmlContentHandler(TagHandler tagHandler) {
			this.context = new ParserContext();
			this.tagHandler = tagHandler;
			this.contentBuffer = new StringBuilder();
		}
		
		public List<Track> getTracks() {
			return this.context.getTracks();
		}
		
		@Override
		public void setDocumentLocator(Locator locator) {
		}

		@Override
		public void startDocument() throws SAXException {
			this.tagHandler.handleStartDocument(context);
		}

		@Override
		public void endDocument() throws SAXException {
			this.tagHandler.handleEndDocument(context);
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			this.contentBuffer = new StringBuilder();
			context.pushContextEntry(qName, atts);
			this.tagHandler.handleStartTag(context, qName);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			this.tagHandler.handleEndTag(context, qName, contentBuffer.toString().trim());
			this.context.getContext().pop();
			this.contentBuffer = new StringBuilder();
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			this.contentBuffer.append(new String(ch, start, length));
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		@Override
		public void processingInstruction(String target, String data)
				throws SAXException {
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
		}
	}

	/**
	 * Creates default error handler used by this parser.
	 */
	private static class XmlParserErrorHandler implements ErrorHandler {
		public void error(SAXParseException ex) throws SAXException {
			logger.error(String.format("Parser error: %s", ex.getMessage()));
		}

		public void fatalError(SAXParseException ex) throws SAXException {
			logger.error(String.format("Parser fatal error: %s", ex.getMessage()));
		}

		public void warning(SAXParseException ex) throws SAXException {
			logger.warn(String.format("Parser warning: %s", ex.getMessage()));
		}
	}
}
