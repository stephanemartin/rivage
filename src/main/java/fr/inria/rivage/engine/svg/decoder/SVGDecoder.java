package fr.inria.rivage.engine.svg.decoder;

import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.interfaces.ITreeElement;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


/**
 * This class allows to import SVG files as GEditor graphics.
 * This decoder does not cover the whole SVG standard. Fully or partially
 * supported are basic shapes, paths, transformations, color, text and
 * groups. Not supported are (e.g.) pictures, definitions, gradients,
 * patterns, viewports, marker symbols and animations.
 * The SVG standard is documented at http://www.w3.org/TR/SVG/.
 * @author Tobias Kuhn
 */
public class SVGDecoder {

	private File file;
	private GGroup svgRoot;
	private DecodeLogger decodeLogger = new DecodeLogger();
	
	private Logger log = Logger.getLogger(getClass());

	/**
	 * Creates a new Decoder for the import of the given SVG file. The
	 * import gets performed as you call decode().
	 * @param file the SVG file to import
	 */
	public SVGDecoder(File file) {
		this.file = file;
	}
	
	/**
	 * Performs the import of the SVG file.
	 * @return the group element that represents the content of the SVG file.
	 * @throws SVGDecodeException if the file could not get imported
	 */
	public GGroup decode() throws SVGDecodeException {
		log.debug("Reading SVG document.");
		try {
			SAXBuilder sb = new SAXBuilder();
			sb.setValidation(false);
			Document doc = sb.build(file);
			log.debug("SVG document parsed successfully.");
			decodeDoc(doc);
		} catch (SVGDecodeException ex) {
			throw ex;
		} catch (IOException ex) {
			throw new SVGDecodeException("Could not read file", ex);
		} catch (Exception ex) {
			throw new SVGDecodeException("Could not parse SVG document", ex);
		}
		log.debug("SVG document decoded successfully.");
		return svgRoot;
	}
	
	/**
	 * Returns the warnings that occured while decoding.
	 * @return the warnings
	 */
	public String getDecodeLog() {
		return decodeLogger.getWarnings();
	}
	
	private void decodeDoc(Document doc) throws SVGDecodeException {
		if (doc.getContentSize() >= 0) {
			Element re = null;
			for (Object o : doc.getContent()) {
				if (o instanceof Element) {
					re = (Element) o;
					break;
				}
			}
			if (re == null) {
				throw new SVGDecodeException("No root element found");
			}
			ITreeElement te = null;//ObjectDec.decode(re, new HashMap<String, Object>(), decodeLogger);
			if (!(te instanceof GGroup)) {
				throw new SVGDecodeException("No root element found");
			}
			svgRoot = (GGroup) te;
		} else {
			svgRoot = null;
		}
	}

}
