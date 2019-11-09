package com.injury.sites;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.injury.errorhandling.BatchException;


public abstract class SiteParser extends BaseParser{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public abstract Map<String, String> parseIndex(String xhtml) throws BatchException;
	
	
	protected void getAllElementsByType(Element element, String name, String[] types, Map<String, String> inputFields) {
		logger.info("Entering getAllElementsByType()");
		logger.debug("name: " + name);
		logger.debug("types: " + java.util.Arrays.toString(types));
	
		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			if (ele != null) {
				final String type = ele.attr("type");
				for (int z = 0; z < types.length; z++) {
					if (type != null && types[z].equals(type)) {
						final String elementName = ele.attr("name");
						final String elementValue = ele.attr("value");
						if (elementName != null && elementName.length() > 0) {
							inputFields.put(elementName, elementValue);
						}
					}
				}
			}
		}

		logger.info("Exiting getAllElementsByType()");
	}
	
	protected Map<String, String> getAllElementsByNameForClass(Document doc, String name, String attrName, Map<String, String> inputFields, String[] types) {
		logger.info("Entering getAllElementsByNameForClass()");
		logger.debug("name: " + name);
		logger.debug("attrName: " + attrName);

		final Elements elements = doc.select(name);
		if (elements != null && elements.size() > 0) {
			for (int y = 0; (elements != null && y < elements.size()); y++) {
				final Element element = elements.get(y);
				logger.trace("Element: " + element);
				String className = element.attr("name");
				for (int x = 0; x < types.length; x++) {
					if (className != null && types[x].equals(className)) {
						String dataValue = element.attr(attrName);
						if (dataValue == null || dataValue.length() == 0) {
							String type = element.attr("type");
							if (type != null && type.equals("checkbox")) {
								dataValue = "on";
							} else {
								dataValue = "";
							}
						}
						inputFields.put(element.attr("name"), dataValue);
					}
				}
			}
		}

		logger.info("Exiting getAllElementsByNameForClass()");
		return inputFields;
	}
	
	protected void getAllSelects(Element element, Map<String, String> inputFields) {
		logger.info("Entering getAllElementsByType()");

		final Elements elements = element.select("select");
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element select = elements.get(x);
			if (select != null) {
				final String selectName = select.attr("name");
				inputFields.put(selectName, "0");
			}
		}

		logger.info("Exiting getAllElementsByType()");
	}
	
	protected Map<String, String> parseInputField(Elements inputs, int inputNum) {
		logger.info("Entering parseInputField()");
		logger.trace("Elements: " + inputs);

		final Map<String, String> inputMap = new HashMap<String, String>();
		if (inputs != null && inputs.size() > 0) {
			logger.debug("inputs.size(): " + inputs.size());
			Element input = null;
			if (inputs.size() == inputNum) {
				input = inputs.get(inputNum - 1);
			} else {
				input = inputs.get(inputNum);
			}

			if (input != null) {
				final String id = input.attr("id");
				final String name = input.attr("name");
				final String value = input.attr("value");
				final String rel = input.attr("rel");
				logger.debug("input id: " + id);
				logger.debug("input name: " + name);
				logger.debug("input value: " + value);

				inputMap.put("id", id);
				inputMap.put("name", name);
				inputMap.put("value", value);
				inputMap.put("rel", rel);
			}
		}

		logger.info("Exiting parseInputField()");
		return inputMap;
	}
	
	protected Map<String, String> parseSelectField(Elements selects) {
		logger.info("Entering parseSelectField()");
		logger.trace("Elements: " + selects);
		final Map<String, String> selectMap = new HashMap<String, String>();

		if (selects != null && selects.size() > 0) {
			final Element select = selects.get(0);
			if (select != null) {
				final String id = select.attr("id");
				final String name = select.attr("name");
				final String value = select.attr("value");
				final String forAttr = select.attr("for");
				logger.debug("select id: " + id);
				logger.debug("select name: " + name);
				logger.debug("select value: " + value);
				logger.debug("select for: " + forAttr);

				selectMap.put("id", id);
				selectMap.put("name", name);
				selectMap.put("value", value);
				selectMap.put("for", forAttr);
			}
		}

		logger.info("Exiting parseSelectField()");
		return selectMap;
	}
	
	
	
	protected Map<String, String> parseSpread(String spreadString, int index) {
		logger.info("Entering parseSpread()");
		logger.debug("spreadString: " + spreadString);
		logger.debug("index: " + index);
		final Map<String, String> values = new HashMap<String, String>();

		// Check for valid string
		if (spreadString != null) {
			String val = null;
			if (index != -1) {
				val = spreadString.substring(0, index);
			} else {
				val = spreadString;
			}

			String valindicator = val.substring(0,1);
			if (valindicator != null) {
				if ("-".equals(valindicator) || "+".equals(valindicator)) {
					val = val.substring(1);		
				} else {
					// check for a PK
					if ("p".equalsIgnoreCase(valindicator)) {
						valindicator = "+";
						val = "0";
					} else if ("e".equalsIgnoreCase(valindicator)) {
						valindicator = "+";
						val = "0";
					}
				}
			}
			val = val.replaceAll("\u00BD", ".5");
			val = val.replaceAll("&nbsp;", "");
			val = val.trim();
			values.put("val", val);
			values.put("valindicator", valindicator);
		}

		logger.info("values: " + values);
		logger.info("Exiting parseSpread()");
		return values;
	}
	
	
	protected String getHtmlFromLastIndex(Element td, String lastIndex) {
		logger.info("Entering getHtmlFromLastIndex()");
		logger.trace("td: " + td);
		logger.debug("lastIndex: " + lastIndex);

		String data = td.html();
		if (data != null) {
			int index = data.lastIndexOf(lastIndex);
			if (index != -1) {
				data = data.substring(index + 1);
				data = data.replaceAll("&nbsp;", " ");
				data = data.trim();
			}
		}
		
		logger.info("Entering getHtmlFromLastIndex()");
		return data;
	}
	

	
	protected Map<String, String> parseTotal(String totalString, int index) {
		logger.info("Entering parseTotal()");
		logger.debug("totalString: " + totalString);
		logger.debug("index: " + index);
		final Map<String, String> values = new HashMap<String, String>();

		// Check for valid string
		if (totalString != null && index != -1) {
			if (totalString.substring(1, 2) != null && !totalString.substring(1, 2).equals(" ")) {
				// Do nothing
			} else {
				totalString = totalString.substring(0, 1) + totalString.substring(2);
			}

			String val = null;
			if (index != -1) {
				val = totalString.substring(0, index);
			} else {
				val = totalString;
			}

			String valindicator = val.substring(0,1);
			if (valindicator != null) {
				if ("o".equalsIgnoreCase(valindicator) || "u".equalsIgnoreCase(valindicator)) {
					val = val.substring(1);
				}
			}
			val = val.replaceAll("\u00BD", ".5");
			val = val.replaceAll("&nbsp;", "");
			val = val.trim();
			values.put("val", val);
			values.put("valindicator", valindicator);
		} else if (totalString != null && totalString.length() > 0) {
			String val = null;
			String valindicator = totalString.substring(0,1);
			logger.debug("valindicator: " + valindicator);
			if (valindicator != null) {
				if ("o".equalsIgnoreCase(valindicator) || "u".equalsIgnoreCase(valindicator)) {
					val = totalString.substring(1);
					val = super.reformatValues(val);
					logger.debug("val: " + val);
					values.put("val", val);
					values.put("valindicator", valindicator);
				}
			}
		}

		logger.info("values: " + values);
		logger.info("Exiting parseTotal()");
		return values;
	}
	
	

	protected void getAllElementsByName(Document doc, String name, String attrName, Map<String, String> inputFields) {
		logger.info("Entering getAllElementsByName()");
		logger.debug("name: " + name);
		logger.debug("attrName: " + attrName);

		final Elements elements = doc.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element element = elements.get(x);
			String dataValue = null;
			if (element != null) {
				String eleName = element.attr("name");
				dataValue = element.attr(attrName);
				if (dataValue == null || dataValue.length() == 0) {
					String type = element.attr("type");
					if (type != null && type.equals("checkbox")) {
						dataValue = "on";
					} else {
						dataValue = "";
					}
				}
				inputFields.put(eleName, dataValue);
			}
		}

		logger.info("Exiting getAllElementsByName()");
	}
	
	protected String getElementByName(Document doc, String name, String attrName) {
		logger.info("Entering getElementByName()");
		logger.debug("name: " + name);
		logger.debug("attrName: " + attrName);
		String retValue = "";

		final Elements elements = doc.select(name);
		if (elements != null && elements.size() > 0) {
			final Element element = elements.get(0);
			if (element != null) {
				retValue = element.attr(attrName);
			}
		}

		logger.info("Exiting getElementByName()");
		return retValue;
	}
	
	protected String getHtmlFromElement(Element element, String name, int position, boolean gethtml) {
		logger.info("Entering getHtmlFromElement()");
		logger.trace("Element: " + element);
		logger.trace("name: " + name);
		logger.trace("position: " + position);
		String retValue = null;

		final Elements elements = element.select(name);
		if (elements != null && elements.size() > 0) {
			final Element ele = elements.get(position);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		} else if (gethtml) {
			retValue = element.html();
			if (retValue != null) {
				retValue = retValue.replaceAll("&nbsp;", "");
				retValue = retValue.trim();
			}
		}

		logger.info("Exiting getHtmlFromElement()");
		return retValue;
	}
	
	/**
	 * Method for processing TDSport code
	 *  
	 */
	protected Map<String, String> getAllElementsByNameByElement(Element element, String name, String attrName, Map<String, String> inputFields) {
		logger.info("Entering getAllElementsByNameByElement()");
		logger.debug("name: " + name);
		logger.debug("attrName: " + attrName);

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			String dataValue = null;
			if (ele != null) {
				String eleName = ele.attr("name");
				dataValue = ele.attr(attrName);
				if (dataValue == null || dataValue.length() == 0) {
					String type = ele.attr("type");
					if (type != null && type.equals("checkbox")) {
						dataValue = "on";
					} else {
						dataValue = "";
					}
				}
				inputFields.put(eleName, dataValue);
			}
		}

		logger.info("Exiting getAllElementsByNameByElement()");
		return inputFields;
	}
	
	/**
	 * Method for processing TDSport code
	 */

	protected String parseHtmlBefore(String html, String beforeString) {
		logger.info("Entering parseHtmlBefore()");
		logger.debug("html: " + html);
		logger.debug("beforeString: " + beforeString);
		String retValue = "";

		if (html != null && html.length() > 0) {
			int index = html.indexOf(beforeString);
			if (index != -1) {
				retValue = html.substring(0 ,index);
			}
		}

		logger.info("Exiting parseHtmlBefore()");
		return retValue;
	}
	
	
	/**
	 * Method required for Linetracker site
	 * @param element
	 * @param name
	 * @param attrName
	 * @param inputFields
	 */
	protected void getAllElementsByName(Element element, String name, String attrName, Map<String, String> inputFields) {
		logger.info("Entering getAllElementsByName()");
		logger.debug("name: " + name);
		logger.debug("attrName: " + attrName);

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			String dataValue = null;
			if (ele != null) {
				String eleName = ele.attr("name");
				dataValue = ele.attr(attrName);
				if (dataValue == null || dataValue.length() == 0) {
					String type = ele.attr("type");
					if (type != null && type.equals("checkbox")) {
						dataValue = "on";
					} else {
						dataValue = "";
					}
				}
				inputFields.put(eleName, dataValue);
			}
		}

		logger.info("Exiting getAllElementsByName()");
	}
	
	/**
	 * method required for linetracker site
	 * @param td
	 * @return
	 */
	protected String getHtmlFromElement(Element td) {
		logger.info("Entering getHtmlFromElement()");
		logger.debug("td: " + td);

		String data = td.html();
		if (data != null) {
			data = data.replaceAll("&nbsp;", "");
			data = data.trim();
		}

		logger.info("Entering getHtmlFromElement()");
		return data;
	}
	
	/**
	 * Method required for linetracker
	 * @param element
	 * @param name
	 * @param position
	 * @return
	 */
	protected String getHtmlByClassName(Element element, String name, int position) {
		logger.info("Entering getHtmlByClassName()");
		logger.debug("Element: " + element);
		logger.debug("name: " + name);
		logger.debug("position: " + position);
		String retValue = "";

		final Elements elements = element.getElementsByClass(name);
		if (elements != null && elements.size() > 0) {
			final Element ele = elements.get(position);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		}

		logger.info("Exiting getHtmlByClassName()");
		return retValue;
	}
	
	//methods required for Sports411 sites
	protected void getAllElementsByTypeWithCheckbox(Element element, String name, String[] types, Map<String, String> inputFields) {
		logger.info("Entering getAllElementsByTypeWithCheckbox()");
		logger.debug("name: " + name);
		logger.debug("types: " + java.util.Arrays.toString(types));

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			if (ele != null) {
				final String type = ele.attr("type");
				for (int z = 0; z < types.length; z++) {
					if (type != null && types[z].equals(type)) {
						final String elementName = ele.attr("name");
						String elementValue = ele.attr("value");
						if (type.equals("checkbox")) {
							elementValue = "on";
						}
						inputFields.put(elementName, elementValue);
					}
				}
			}
		}

		logger.info("Exiting getAllElementsByTypeWithCheckbox()");
	}
	
	//method required for sports411
	protected void parseSelectFieldByNumBlank(Elements selects, int num, Map<String, String> selectMap) {
		logger.info("Entering parseSelectFieldByNumBlank()");
		logger.debug("Elements: " + selects);
		logger.debug("num: " + num);

		if (selects != null && selects.size() > 0) {
			final Element select = selects.get(num);
			if (select != null) {
				final String name = select.attr("name");
				selectMap.put(name, "");
			}
		}

		logger.info("Exiting parseSelectFieldByNumBlank()");
	}
	
	protected void parseSelectFieldByNumBlank(Elements selects, Map<String, String> selectMap) {
		logger.info("Entering parseSelectFieldByNumBlank()");
		logger.debug("Elements: " + selects);
		
		if (selects != null && selects.size() > 0) {
			for (Element singleSelect : selects) {
				if (singleSelect != null) {
					final String name = singleSelect.attr("name");
					logger.info("Name: " + name);
					selectMap.put(name, "");
				}
			}
		}
		logger.info("Exiting parseSelectFieldByNumBlank()");
	}
	
	//method required for sports411
	protected String getHtmlFromAllElements(Element element, String name) {
		logger.info("Entering getHtmlFromAllElements()");
		logger.debug("Element: " + element);
		logger.debug("name: " + name);
		String retValue = "";

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		}

		logger.info("Exiting getHtmlFromAllElements()");
		return retValue;
	}
	
	protected void parseSelectFieldByNum(Elements selects, int num, Map<String, String> selectMap) {
		logger.info("Entering parseSelectFieldByNum()");
		logger.debug("Elements: " + selects);
		logger.debug("num: " + num);

		if (selects != null && selects.size() > 0) {
			final Element select = selects.get(num);
			if (select != null) {
				final String id = select.attr("id");
				final String name = select.attr("name");
				final String value = select.attr("value");
				final String forAttr = select.attr("for");
				logger.debug("select id: " + id);
				logger.debug("select name: " + name);
				logger.debug("select value: " + value);
				logger.debug("select for: " + forAttr);

				selectMap.put("id", id);
				selectMap.put("name", name);
				selectMap.put("value", value);
				selectMap.put("for", forAttr);
			}
		}

		logger.info("Exiting parseSelectFieldByNum()");
	}
	
}
