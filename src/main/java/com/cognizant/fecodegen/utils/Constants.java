package com.cognizant.fecodegen.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static String RELATIVE_PATH = "path";
	public static String TEMPLATE_NAME = "templateName";
	public static String OUTPUT_FILE_NAME = "outputFileName";
	
	public static String OUTPUT_RENDERER_PATH = "codegen.output.path";
	
	public static final String SECTION_NAME = "sectionName";
	public static final String ACTION_NAME = "actionName";
	public static final String COMPONENT_NAME = "componentName";
	public static final String CONTAINER_NAME = "containerName";
	public static final String REDUCER_NAME = "reducerName";
	public static final String MODULE_NAME = "moduleName";

	public static final String ACTION_METHOD_NAME = "actionMethodName";
	
	public static final String SERVICE_FILENAME = "serviceFileName";
	public static final String ACTION_FILENAME = "actionFileName";
	public static final String REDUCER_FILENAME = "reducerFileName";
	public static final String ROUTER_FILENAME = "routerFileName";
	public static final String COMPONENT_FILENAME = "componentFileName";
	public static final String CONTAINER_FILENAME = "containerFileName";

	public static final String PUBLIC_PATH = "publicPath";
	public static final String GENERATE_LOGIN_SCREEN = "genLoginScreen";
	public static final String DEFAULT_PAGE = "<DefaultPage />";
	public static final String ROOT_REDUCER_NAME = "rootReducer.js";
	public static final String LOGIN = "Login";
	public static final String LOGIN_CONTAINER = "loginContainer.js";
	public static final String PAGE = "page";
	public static final String UI_NAME = "uiName";
	public static final String FTL_CHILD_COMPONENT = "childComponent";
	public static final String FTL_LABEL_STRIPWHITESPACE = "label_stripWhiteSpace";
	public static final String CHILDREN_ELEMENTS = "children";
	public static final String ROW_COUNT = "rowCount";
	public static final String COL_COUNT = "colCount";
	public static final String ROUTER_COMPONENTS = "routerComponents";
	public static final String COMPONENT_OPTIONS = "componentOptions";
	
	public static final String JSP_TAGS_DOM = "dom";
	public static final String JSP_TAGS_ATTRIBUTES = "attributes";
	public static final String JSP_TAGS_TREE = "tree";
	public static final String JSP_TAGS_TAG_NAME = "tag_name";
	public static final String JSP_TAGS_START_POSITION = "start_position";
	public static final String JSP_TAGS_END_POSITION = "end_position";
	public static final String JSP_TAGS_END_TAG_SYNTAX = "/>";
	
	public static final String COMMA = ",";
	public static final String FORWARD_SLASH = "/";
	public static final String SEPARATOR_LT = "<";
	public static final String SEPARATOR_GT = ">";
	public static final String SEPARATOR_EQUAL = "=";
	public static final String SEPARATOR_DOUBLE_QUOTES = "\"";
	
	//for spring boot code generation
	public static final String APPLICATION = "application";
	public static final String INLINEAPP = "inline-application";
	public static final String DEFAULT = "default"; 
	public static final String PACKAGE = "package";
	public static final String APPNAME = "apiName";
	public static final String PACKAGENAME = "com.cognizant.";
	
	public static final String CLASSNAME = "className";
	public static final String MEMBER = "member";
	public static final String REQUESTURL = "requestUrl";
	
	public static final String DATATYPE = "DataType";
	public static final String VARIABLE = "VariableName";
	public static final String METHOD = "methodName";
	public static final String COMPCONFMETHODNAME = "compConfigMethodName";
	public static final String COMPCONFBEANNAME = "compConfigBeanName";
	public static final String COMPCONFIGBEANLIST = "compConfigBeanList";
	public static final String CLASSREFERENCENAME = "classReferenceName";
	
	public static final String STRING = "String";
	public static final String BOOLEAN = "Boolean";
	public static final String DATE = "Date";
	
	public static final String TEXTBOX = "textbox";
	public static final String CHECKBOX = "checkbox";
	public static final String DATEPICKER = "datepicker";
	
	public static final String APPPROPERTIES = "application.properties";
	public static final String DTO = "DTO";
	public static final String CONTROLLER = "Controller";
	public static final String ENTITY = "Entity";
	public static final String MAIN = "Main";
	public static final String POM = "pom.xml";
	public static final String REPOSITORY = "Repository";
	public static final String RESPONSE = "Response";
	public static final String SERVICE = "Service";
	public static final String SERVICEIMPL = "ServiceImpl";
	public static final String DAO = "DAO";
	public static final String DAOIMPL = "DAOImpl";
	public static final String COMPCONFIG = "ComponentConfiguration";
	public static final String EXCEPTION = "Exception";
	public static final String HELPER = "Helper";
	public static final String CONSTANTS = "Constants";
	public static final String UTIL = "Util";
	
	public static final String SPACE_REGEX = "\\s";
	
	public static final String ID = "Id";
	public static final String LAYOUT = "layout";
	public static final String TYPE = "type";
	public static final String COLUMNS = "columns";
	public static final String LABEL = "label";
	public static final String CODE_GEN_CONF = "codegen.conf";
	public static final String OTHER_ATTRIBUTES = "otherAttributes";
	public static final String CURRENT_COMPONENT_NAME = "currentComponentName";
	public static final String REACT_COMPONENTS = "reactComponents";
	
	public static final String STATE_VARIABLES = "stateVariables";
	public static final String LOGIN_ROUTING_PATH_STATUS = "LoginRoutingPathStatus";
	
	public static final String TRUE = "true";
	public static final String APP = "app";
	public static final String APP_COMPONENT_CSS = "app.component.css";
	public static final String ROUTING_CONF_JSON = "routing-conf.json";
	public static final String APP_MODULE_TS = ".module.ts";
	public static final String APP_COMPONENT_TS = ".component.ts";
	public static final String BASE_PATH = "/src/app";
	public static final String APP_ROUTING_MODULE_TS = "app-routing.module.ts";
	public static final String PATH = "path:";
	public static final String FOR = ".for";
	public static final String MODULE_FLAG = "moduleFlag";
	public static final String USER_DEFINED_PATH = "userDefinedPath";
	public static final String FORM_TYPE = "form";
	public static final String COMPONENTS_LIST = "componentsList";
	public static final String PANEL_TYPE_MAIN_LEFT_RIGHT = "mainLeftRightPanel";
	public static final String PANEL_TYPE_LEFT = "leftPanel";
	public static final String PANEL_TYPE_RIGHT = "rightPanel";
	public static final String PANEL_TYPE_MAIN_TAB = "mainTabPanel";
	public static final String PANEL_TYPE_TAB = "tabPanel";
	
	public static final String COMPONENT_HTML_FILENAME = "componentHtmlFileName";
	public static final String COMPONENT_TS_FILENAME = "componentTsFileName";
	public static final String COMPONENT_CSS_FILENAME = "componentCssFileName";
	public static final String SECTION_HTML_ID = "SectionHtmlId";
	public static final String MODULE_PATH = "modulePath";
	public static final String MODEL_TEMPLATE = "modelType";
	public static final String APP_COMPONENT_HTML = "appComp";
	public static final String MODEL_NAME = "sectionModelName";
	
	public static boolean TRANSLATE_MODULE_FLAG = false;
	public static Map<String, String> labelValues = new HashMap<String,String>();
	public static final String ACTION_TYPE = "actionType";
	public static final String REDUCER_TYPE = "reducerType";
	public static final String ACTION_CONSTANT = "actionConstant";
	public static final String COMPONENT ="component";
	public static final String REDUCER ="reducer";
	public static final String ACTION ="action";
	public static final String REDUCER_TEMPLATE = "reducerTemplate";
	public static final String ACTION_TEMPLATE = "actionTemplate";
	
	public static final String TAG_COMPONENT_CONFIG = "componentConfig";
	public static final String TAG_TEMPLATES = "templates";
	public static final String TAG_TEMPLATE_BASE_NAME = "baseTemplateName";
	public static final String TAG_TEMPLATE_CODE = "templateCode";
	
}
