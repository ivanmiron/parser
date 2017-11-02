package com.ennoia.buzone.penske.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FileScanner {

	private final Path filePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private static final Logger logger = LogManager.getLogger(FileScanner.class.getName());

	private static final String Y = new String("Y");
	private static final String EXTRAS = new String("Extras");
	private static final String DOCUMENT = new String("Document");
	private static final String EMITTER = new String("Emitter/Penske");
	private static final String RECEPTOR = new String("Receptor/Client");
	private static final String TOTALTAX = new String("TotalTax");
	private static final String TAX1 = new String("tax1");
	private static final String TAX2 = new String("tax2");
	private static final String TAX3 = new String("tax3");
	private static final String CONCEPT = new String("Concept");

	private Map<Integer, String> yMap = new HashMap<Integer, String>();
	private Map<Integer, String> extrasMap = new HashMap<Integer, String>();
	private Map<Integer, String> documentMap = new HashMap<Integer, String>();
	private Map<Integer, String> emitterMap = new HashMap<Integer, String>();
	private Map<Integer, String> receptorMap = new HashMap<Integer, String>();
	private Map<Integer, String> totalTaxMap = new HashMap<Integer, String>();
	private Map<Integer, String> tax1Map = new HashMap<Integer, String>();
	private Map<Integer, String> tax2Map = new HashMap<Integer, String>();
	private Map<Integer, String> tax3Map = new HashMap<Integer, String>();
	private List<HashMap<Integer, String>> conceptList = new ArrayList<HashMap<Integer, String>>();

	/**
	 * Constructor.
	 * 
	 * @param fileName
	 *            full name of an existing, readable file.
	 * 
	 */
	public FileScanner(String fileName) {
		filePath = Paths.get(fileName);
	}

	public final void processFile() throws IOException {
		try (Scanner scanner = new Scanner(filePath, ENCODING.name())) {
			logger.info("Starting parsing process...");
			processLine(scanner.nextLine(), FileScanner.Y, yMap);
			// showMap(yMap);
			processLine(scanner.nextLine(), FileScanner.EXTRAS, extrasMap);
			// showMap(extrasMap);
			processLine(scanner.nextLine(), FileScanner.DOCUMENT, documentMap);
			processLine(scanner.nextLine(), FileScanner.EMITTER, emitterMap);
			processLine(scanner.nextLine(), FileScanner.RECEPTOR, receptorMap);
			processLine(scanner.nextLine(), FileScanner.TOTALTAX, totalTaxMap);
			processLine(scanner.nextLine(), FileScanner.TAX1, tax1Map);
			processLine(scanner.nextLine(), FileScanner.TAX2, tax2Map);
			processLine(scanner.nextLine(), FileScanner.TAX3, tax3Map);

			while (scanner.hasNextLine()) {
				processConceptLine(scanner.nextLine());
			}
			showList();
		}
	}

	protected void processLine(String line, String typeOfLine, Map<Integer, String> map) {
		logger.info("Reading " + typeOfLine + " line.");
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter("\\|");
		try {
			if (scanner.hasNext()) {
				// assumes the line has a certain structure
				String lineType = scanner.next();
				if (lineType.equals(typeOfLine)) {
					for (int i = 1; scanner.hasNext(); i++)
						map.put(i, scanner.next());
				} else
					logger.error("Invalid " + typeOfLine + " line. Unable to process.");
			} else {
				logger.info("Empty or invalid line. Unable to process.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			scanner.close();
		}
	}

	protected void processConceptLine(String line) {
		logger.info("Reading " + FileScanner.CONCEPT + " line.");
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter("\\|");
		try {
			if (scanner.hasNext()) {
				// assumes the line has a certain structure
				String lineType = scanner.next();
				if (lineType.equals(FileScanner.CONCEPT)) {
					for (int i = 1; scanner.hasNext(); i++) {
						HashMap<Integer, String> conceptMap = new HashMap<Integer, String>();
						conceptMap.put(i, scanner.next());
						conceptList.add(conceptMap);
					}
				} else
					logger.error("Invalid " + FileScanner.CONCEPT + " line. Unable to process.");
			} else {
				logger.info("Empty or invalid line. Unable to process.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			scanner.close();
		}
	}

	protected void showMap(HashMap<Integer, String> hmap) {
		Set<Entry<Integer, String>> set = hmap.entrySet();
		Iterator<Entry<Integer, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, String> mentry = (Map.Entry<Integer, String>) iterator.next();
			logger.info("Key is: " + mentry.getKey() + " & Value is: " + mentry.getValue());
		}
	}

	protected void showList() {
		for (HashMap<Integer, String> mapValues : conceptList) {
			Set<Entry<Integer, String>> set = mapValues.entrySet();
			Iterator<Entry<Integer, String>> iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, String> mentry = (Map.Entry<Integer, String>) iterator.next();
				logger.info("Key is: " + mentry.getKey() + " & Value is: " + mentry.getValue());
			}
		}
	}

}