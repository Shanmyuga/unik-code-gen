package com.cognizant.fecodegen.utils;

public interface JspConstants {
	String[] FORM_TAGS = {"s:form","s:textbox","s:dropdown","a","s:button","s:text","s:textfield","s:select","s:submit","s:reset","s:iterator","s:if","s:property","s:hidden","c:url"};
	String[] EVENT_ATTRIBUTES = {"onclick","onblur","onkeypress"};
	String[] VALIDATION_ATTRIBUTES = {"maxlength"};
	String[] NAVIGATION_TAGS = {"s:form","s:button","s:submit"};
	String[] SECTION_TAGS = {"s:form"};
	String[] COLUMN_TAG = {"s:textfield","s:select","s:submit","s:reset"};
}