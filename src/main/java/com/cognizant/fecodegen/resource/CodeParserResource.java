/**
 * 
 */
package com.cognizant.fecodegen.resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.fecodegen.CodeGenTemplateParser;
import com.cognizant.fecodegen.bo.CodeParseRequest;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.JspParser2;

/**
 * Resource Class to parse source code
 * 	Struts 2 - JSP
 * 
 * @author 238209
 *
 */
@RestController
public class CodeParserResource {

	private static Logger LOGGER = Logger.getLogger(CodeParserResource.class);
	
	@Autowired
	protected CodeGenTemplateParser parser;
	
	@PostMapping("/process/parse")
	public @ResponseBody String processUi(@RequestBody CodeParseRequest request) {
		String response = "Success !!";
		
		try {
			JspParser2 parser = new JspParser2(request.getFileName());
			parser.parse();
		} catch (CodeGenException e) {
			LOGGER.error("Error while parsing JSP file: " + request.getFileName(), e);
		}
		
		return response;
	}
	
	public static void main(String[] args) {
		CodeParseRequest request = new CodeParseRequest();
		request.setFileName("D:\\244898\\Project\\Parser\\NIHB\\itemSearch.jsp");
		
		CodeParserResource res = new  CodeParserResource();
		res.processUi(request);
	}
	
}
