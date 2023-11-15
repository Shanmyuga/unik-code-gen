/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mifmif.common.regex.Generex;

import net.andreinc.mockneat.MockNeat;

/**
 * @author 238209
 *
 */
public class FormElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2885863737770517976L;

	private String htmlId;
	private String htmlIdCamCase;
	private String type;
	private String propValue;
	private String displayColumnValue;
	private boolean editable;
	private boolean editableAlways;
	private String dataType;
	
	private int minLength;
	private int maxLength;
	
	private boolean mandatory;
	private boolean formGroup;
	
	private boolean email;
	private String pattern;
	
	private List<TestDataPattern> testDataPatterns;
	
	private static boolean POSITIVE_CASE = true;
	private static boolean NEGATIVE_CASE = false;
	
	private List<ComponentOptions> optionsList;
	
	public void addTestDataPattern(boolean elementValid, boolean hasErrors, 
									String errorType, String inputData) {
		TestDataPattern pattern = new TestDataPattern();
		pattern.setElementValid(elementValid);
		pattern.setHasErrors(hasErrors);
		pattern.setErrorType(errorType);
		pattern.setInputData(inputData);
		
		if (testDataPatterns == null) {
			testDataPatterns = new ArrayList<TestDataPattern>();
		}
		
		testDataPatterns.add(pattern);
	}
	
	public void generateTestData(JsonArray validationOrderList) {
		for (JsonElement validationTypeEle : validationOrderList) {
			String validationType = validationTypeEle.getAsString();
			
			if (canApplyValidation(validationType)) {
				String data = generateData(validationType, NEGATIVE_CASE);
				addTestDataPattern(false, true, validationType, data);
				
				data = generateData(validationType, POSITIVE_CASE);
				addTestDataPattern(true, false, validationType, data);
			}
		}
	}

	private String generateData(String validationType, boolean validationCase) {
		String generexPattern = null;
		String pattrn = StringUtils.isNotBlank(pattern)? pattern: "[A-Za-z0-9]+";
		
		boolean limitToLength = false;
		String returnValue = "";
		if (validationCase == false) {
			switch (validationType) {
			case "email":
				generexPattern = "[A-Za-z0-9 ]{5,10}";
				break;
			case "minLength":
				generexPattern = pattrn + "{0," + (minLength - 1) + "}";
				break;
			case "maxLength":
				generexPattern = "[A-Za-z0-9]{" + (maxLength + 1) + ",}";
				break;
			case "pattern":
				generexPattern = "[A-Za-z0-9 \\._-]";
				limitToLength = true;
				break;
			}
		} else {
			if (email) {
				MockNeat mock = MockNeat.threadLocal();
				returnValue = mock.emails().val();
			} else if (minLength > 0 || maxLength > 0 || StringUtils.isNotBlank(pattern)) {
				generexPattern = pattrn;
				limitToLength = true;
			} else {
				generexPattern = "[A-Za-z0-9 ]{5,10}";
			}
		}
		
		if (StringUtils.isNotBlank(generexPattern)) {
			Generex generex = new Generex(generexPattern);
			
			returnValue = generateText(limitToLength, generex);
			
			// Validate data for negative Case
			if (validationCase == false) {
				if ("pattern".equalsIgnoreCase(validationType)) {
					int length = 5;
					if (maxLength > 0 && maxLength < 5) {
						length = maxLength;
					}

					returnValue = generex.random(length);
					if (returnValue.matches(pattern)) {
						List<String> matchedString = generex.getAllMatchedStrings();
						for (String matStr : matchedString) {
							if (matStr.matches(pattern) == false) {
								returnValue = matStr;
								break;
							}
						}
					}
				} else if ("minLength".equalsIgnoreCase(validationType)) {
					if (returnValue.length() >= minLength) {
						returnValue = returnValue.substring(0, minLength-1);
					}
				} else if ("maxLength".equalsIgnoreCase(validationType)) {
					if (returnValue.length() <= maxLength) {
						List<String> matchedString = generex.getAllMatchedStrings();
						for (String matStr : matchedString) {
							if (matStr.length() > maxLength) {
								returnValue = matStr;
								break;
							}
						}
					}
				}
			}
		}
		
		return returnValue;
	}

	/**
	 * @param limitToLength
	 * @param generex
	 * @return
	 */
	protected String generateText(boolean limitToLength, Generex generex) {
		String returnValue;
		if (limitToLength && (minLength > 0 || maxLength > 0)) {
			if (maxLength > 0) {
				returnValue = generex.random(minLength, maxLength);
			} else {
				returnValue = generex.random(minLength);
			}
		} else {
			returnValue = generex.random();
		}
		return returnValue;
	}
	
	public static void main(String[] args) {
		FormElement fm = new FormElement();
		
		fm.setMandatory(false);
		fm.setEmail(false);
		fm.setMinLength(3);
		fm.setMaxLength(10);
		fm.setPattern("[A-Za-z]*");
		
		JsonArray validationOrderList = new JsonArray();
		validationOrderList.add("required");
		validationOrderList.add("email");
		validationOrderList.add("minLength");
		validationOrderList.add("maxLength");
		validationOrderList.add("pattern");
		
		fm.generateTestData(validationOrderList);
		
		System.out.println(fm.getTestDataPatterns());
	}

	/**
	 * @param validationType
	 * @return
	 */
	protected boolean canApplyValidation(String validationType) {
		boolean canApplyValidation = false;
		if (StringUtils.equalsIgnoreCase("required", validationType) && mandatory) {
			canApplyValidation = true;
		} else if (StringUtils.equalsIgnoreCase("email", validationType) && email) {
			canApplyValidation = true;
		} else if (StringUtils.equalsIgnoreCase("minLength", validationType) && minLength > 0) {
			canApplyValidation = true;
		} else if (StringUtils.equalsIgnoreCase("maxLength", validationType) && maxLength > 0) {
			canApplyValidation = true;
		} else if (StringUtils.equalsIgnoreCase("pattern", validationType) 
				&& StringUtils.isNotBlank(pattern)) {
			canApplyValidation = true;
		}
		
		return canApplyValidation;
	}
	
	/**
	 * @return the htmlId
	 */
	public String getHtmlId() {
		return htmlId;
	}
	/**
	 * @param htmlId the htmlId to set
	 */
	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}
	/**
	 * @return the minLength
	 */
	public int getMinLength() {
		return minLength;
	}
	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}
	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	/**
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}
	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	/**
	 * @return the formGroup
	 */
	public boolean isFormGroup() {
		return formGroup;
	}
	/**
	 * @param formGroup the formGroup to set
	 */
	public void setFormGroup(boolean formGroup) {
		this.formGroup = formGroup;
	}
	/**
	 * @return the propValue
	 */
	public String getPropValue() {
		return propValue;
	}
	/**
	 * @param propValue the propValue to set
	 */
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	/**
	 * @return the displayColumnValue
	 */
	public String getDisplayColumnValue() {
		return displayColumnValue;
	}
	/**
	 * @param displayColumnValue the displayColumnValue to set
	 */
	public void setDisplayColumnValue(String displayColumnValue) {
		this.displayColumnValue = displayColumnValue;
	}
	/**
	 * @return the email
	 */
	public boolean isEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(boolean email) {
		this.email = email;
	}
	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the testDataPatterns
	 */
	public List<TestDataPattern> getTestDataPatterns() {
		return testDataPatterns;
	}

	/**
	 * @param testDataPatterns the testDataPatterns to set
	 */
	public void setTestDataPatterns(List<TestDataPattern> testDataPatterns) {
		this.testDataPatterns = testDataPatterns;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return the editableAlways
	 */
	public boolean isEditableAlways() {
		return editableAlways;
	}

	/**
	 * @param editableAlways the editableAlways to set
	 */
	public void setEditableAlways(boolean editableAlways) {
		this.editableAlways = editableAlways;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the optionsList
	 */
	public List<ComponentOptions> getOptionsList() {
		return optionsList;
	}

	/**
	 * @param optionsList the optionsList to set
	 */
	public void setOptionsList(List<ComponentOptions> optionsList) {
		this.optionsList = optionsList;
	}

	/**
	 * @return the htmlIdCamCase
	 */
	public String getHtmlIdCamCase() {
		return htmlIdCamCase;
	}

	/**
	 * @param htmlIdCamCase the htmlIdCamCase to set
	 */
	public void setHtmlIdCamCase(String htmlIdCamCase) {
		this.htmlIdCamCase = htmlIdCamCase;
	}
}
