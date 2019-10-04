/**
 * 
 */
package jgpstrackedit.gpsies;

import jgpstrackedit.data.Track;
import jgpstrackedit.trackfile.kml.KMLWriter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author Hubert
 * 
 */
public class GPSiesUpLoad 
{
	private static final String UPLOAD_URL = "http://www.gpsies.com/upload.do";
	private static Logger logger = LoggerFactory.getLogger(GPSiesUpLoad.class);

	private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
	private static final String CHARSET_DEFAULT = "UTF-8";

	/**
	 * A track may be uploaded to gpsies.com if the trackname does not begin
	 * with prefix "test" and the track length exceeds 250 m
	 * 
	 * @param track
	 *            the track to test
	 * @return true if the track may be uploaded
	 */
	public boolean checkUploadable(Track track) {
				return (track.getName() != null && !track.getName().startsWith("test") && track
				.getLength() > 0.25);
	}

	public void saveTempKML(String trackName, Track track) {
		KMLWriter kmlWriter = new KMLWriter();
		File file = new File("gpsiesupload");
		file.mkdirs();
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(getFileName(trackName)))));
			kmlWriter.print(track, out);
			out.close();
		} catch (FileNotFoundException e) {
			logger.error(String.format("Cannot write to file! (%s)", getFileName(trackName)), e);
		}
	}

	public int uploadFile(String filename, String trackTypes, File importFile,
			String username, String password) throws HttpException, IOException {

		int result = -1;

		if (username != null && password != null && importFile.exists()) {

			PostMethod postMethod = new PostMethod(UPLOAD_URL);

			// please set the language, eg. "fr-fr"
			postMethod.addRequestHeader("Accept-Language", "de-de");
			postMethod.addRequestHeader("User-Agent", "JGPSTrackEdit ("
					+ importFile + ")");

			String validationString = username + "|"
					+ DigestUtils.md5Hex(password);
			String encryptedString = new String(
					Base64.encodeBase64(validationString.getBytes()));

			Part[] parts = {
					new StringPart("device", "JGPSTrackEdit"),
					new StringPart("authenticateHash", encryptedString),
					new StringPart("trackTypes", trackTypes),
					new StringPart("filename", filename),
					new StringPart("uploadButton", "speichern"),
					new FilePart("formFile", importFile,
							OCTET_STREAM_CONTENT_TYPE, CHARSET_DEFAULT) };

			postMethod.setRequestEntity(new MultipartRequestEntity(parts,
					postMethod.getParams()));

			HttpClient httpclient = new HttpClient();
			httpclient.getParams().setParameter("Content-type",
					"text/html;charset=UTF-8");

			httpclient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(5000);

			result = httpclient.executeMethod(postMethod);

		}

		return result;

	}

	public String getFileName(String trackName) {
		return "gpsiesupload" + File.separator + trackName + ".kml";
	}
}
