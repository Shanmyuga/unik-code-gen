package com.cognizant.fecodegen.visitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;

import com.cognizant.fecodegen.bo.JsParsedComponent;


public class JSNodeVisitor implements NodeVisitor {
	
	private static String name = null,functionName = null;
	private List<JsParsedComponent> jsParsedValues = new ArrayList<>();
	
	@Override
	public boolean visit(AstNode node) {
		String nodeClass = node.getClass().toString();
		
		
		if(node.getType() == Token.FUNCTION){
			functionName = ((FunctionNode)node).getFunctionName().getIdentifier();
		}				
		else if(nodeClass.equalsIgnoreCase("class org.mozilla.javascript.ast.StringLiteral"))
		{
			
			if( name != null && (name.equalsIgnoreCase("action") || name.equalsIgnoreCase("name") || name.equalsIgnoreCase("innerHTML")) ) {
				
				
				JsParsedComponent jsComponent = new JsParsedComponent();
				jsComponent.setFunctionName(functionName);
				jsComponent.setVariable(name);
				
				String splitString = null;
				String variableAction = (((StringLiteral)node)).getValue();
			        
		        Pattern p = Pattern.compile(Pattern.quote("'") + "(.*?)" + Pattern.quote("'"));
		        Matcher m = p.matcher(variableAction);
		        while (m.find()) {
		          splitString = m.group(1);
		        }    
				
		        if(splitString != null && name.equalsIgnoreCase("action") )
		        	  jsComponent.setVariableValue(splitString);
		        else
		        	jsComponent.setVariableValue(variableAction);
		        	
				
				jsParsedValues.add(jsComponent);
				
				name = null;
			}
		}
		else if(nodeClass.equalsIgnoreCase("class org.mozilla.javascript.ast.Name"))
		{
			name = ((Name) node).getIdentifier();
		}
		
		return true;
	}

	public List<JsParsedComponent> getJsParsedValues() {
		return jsParsedValues;
	}
		
	
}
