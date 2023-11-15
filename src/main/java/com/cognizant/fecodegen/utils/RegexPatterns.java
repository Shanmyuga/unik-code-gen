/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.util.regex.Pattern;

/**
 * @author 238209
 *
 */
public interface RegexPatterns {
	public static String COMMENTS_REGEX = "<%--.*--%>";
	public static Pattern COMMENTS_PATTERN = Pattern.compile(COMMENTS_REGEX);

	public static String HTML_TAG_REGEX = "<[a-zA-Z:0-9]+";
	public static Pattern HTML_TAG_PATTERN = Pattern.compile(HTML_TAG_REGEX);
	
	public static String HTML_TAG_LINE_REGEX = "<[a-zA-Z:0-9]+[.\\s\\S]*?>";
	public static Pattern HTML_TAG_LINE_PATTERN = Pattern.compile(HTML_TAG_LINE_REGEX);
	
	public static String HTML_END_TAG_REGEX = "</.*?>";
	public static Pattern HTML_END_TAG_PATTERN = Pattern.compile(HTML_END_TAG_REGEX);
	
	public static String  TS_IMPORT_REGEX = "import[.\\s\\S]*?;";
	public static Pattern TS_IMPORT_PATTERN = Pattern.compile(TS_IMPORT_REGEX);
	
	public static String  TS_COMPONENT_REGEX = "@Component[^\\)]+\\)";
	public static Pattern TS_COMPONENT_PATTERN = Pattern.compile(TS_COMPONENT_REGEX);
	
	public static String  TS_CLASSNAME_REGEX = "export.*\\{";
	public static Pattern TS_CLASSNAME_PATTERN = Pattern.compile(TS_CLASSNAME_REGEX);
	
	public static String  TS_DECLARATION_REGEX = "[\\@a-zA-Z0-9\\(\\) \\[\\{=]+: [.\\s\\S]*?;";
	public static Pattern TS_DECLARATION_PATTERN = Pattern.compile(TS_DECLARATION_REGEX);
	
	public static String  TS_METHODNAME_REGEX = "[ \\t]*.*\\(.*\\)[ ]+\\{";
	public static Pattern TS_METHODNAME_PATTERN = Pattern.compile(TS_METHODNAME_REGEX);

	public static String  TS_TYPES_REGEX = "export[.\\s\\S]*?\\}";
	public static Pattern TS_TYPES_PATTERN = Pattern.compile(TS_TYPES_REGEX);
	
	public static String  MODULE_IMPORT_REGEX = Pattern.quote("imports: [") + "(?s)(.*?)" + Pattern.quote("],");
	public static Pattern MODULE_IMPORT_PATTERN = Pattern.compile(MODULE_IMPORT_REGEX);
	
	public static String  MODULE_DECLARATIONS_REGEX = Pattern.quote("declarations: [") + "(?s)(.*?)" + Pattern.quote("]");
	public static Pattern MODULE_DECLARATIONS_PATTERN = Pattern.compile(MODULE_DECLARATIONS_REGEX);
	
	public static String  MODULE_SCHEMA_REGEX = Pattern.quote("schemas: [") + "(?s)(.*?)" + Pattern.quote("]");
	public static Pattern MODULE_SCHEMA_PATTERN = Pattern.compile(MODULE_SCHEMA_REGEX);
	
	public static String  MODULE_ROUTE_REGEX = Pattern.quote("Routes = [") + "(?s)(.*?)" + Pattern.quote("]");
	public static Pattern MODULE_ROUTE_PATTERN = Pattern.compile(MODULE_ROUTE_REGEX);
	
	public static String  MODULE_ROUTE_ELEMENT_REGEX = Pattern.quote("{") + "(?s)(.*?)" + Pattern.quote("}");
	public static Pattern MODULE_ROUTE_ELEMENT_PATTERN = Pattern.compile(MODULE_ROUTE_ELEMENT_REGEX);
	
	public static String  MODULE_EXPORT_REGEX = Pattern.quote("exports: [") + "(?s)(.*?)" + Pattern.quote("]");
	public static Pattern MODULE_EXPORT_PATTERN = Pattern.compile(MODULE_EXPORT_REGEX);
	
	public static String  MODULE_FORROOT_REGEX = "[A-Za-z]*.forRoot[ ]*(\\(\\{)[.\\s\\S]*?(\\}\\))";
	public static Pattern MODULE_FORROOT_PATTERN = Pattern.compile(MODULE_FORROOT_REGEX);
}
