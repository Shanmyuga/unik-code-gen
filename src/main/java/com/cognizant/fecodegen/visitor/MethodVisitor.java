package com.cognizant.fecodegen.visitor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.bo.jsonoutput.Action;
import com.cognizant.fecodegen.bo.jsonoutput.Method;
import com.cognizant.fecodegen.bo.jsonoutput.Validations;
import com.cognizant.fecodegen.utils.Constants;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * This method is used to extract the method level annotation for the action
 * class
 * 
 * @author 244898
 *
 */
public class MethodVisitor extends VoidVisitorAdapter {

	public void visit(MethodDeclaration n, Object arg) {

		Map<NameExpr, List<Node>> methodDetails = new HashMap<NameExpr, List<Node>>();
		List<Validations> methodValList = new ArrayList<>();
		
		try {
			Map<String, Action> actionMap = (HashMap) arg;
			Action action = actionMap.get(((ClassOrInterfaceDeclaration) n.getParentNode()).getName());
			Method method = new Method();
			method.setName(n.getName());

			// Setting all the method level annotations to a map
			for (AnnotationExpr ann : n.getAnnotations()) {
				methodDetails.put(ann.getName(), ann.getChildrenNodes());
			}

			Multimap<String, NormalAnnotationExpr> multimap;

			// Iterating over the map to extract the annotations information
			for (Map.Entry<NameExpr, List<Node>> entry : methodDetails.entrySet()) {
				multimap = ArrayListMultimap.create();
				
				Validations val = new Validations();
				for (Node node1 : entry.getValue()) {

					if (node1 instanceof StringLiteralExpr) {
						Field privateStringField;
						if (!"SuppressWarnings".equals(entry.getKey().getName())) {
							privateStringField = Method.class.getDeclaredField(entry.getKey().getName());
							privateStringField.setAccessible(true);
							privateStringField.set(method, ((StringLiteralExpr) node1).getValue());
						}
					}

					if (node1 instanceof MemberValuePair) {
						if (!"customValidators".equals(((MemberValuePair) node1).getName())) {
							Field privateStringField = Validations.class
									.getDeclaredField(((MemberValuePair) node1).getName());
							privateStringField.setAccessible(true);
							privateStringField.set(val, ((MemberValuePair) node1).getValue().toString()
									.replaceAll(Constants.SEPARATOR_DOUBLE_QUOTES, StringUtils.EMPTY));
						}
						
						for (Node node2 : node1.getChildrenNodes()) {
							extractArrayInitExpr(multimap, entry, node2);
						}
						
					}
				}
				if(val != null)
					methodValList.add(val);

				Field privateStringField = Method.class.getDeclaredField("Validations");
				privateStringField.setAccessible(true);
				privateStringField.set(method, methodValList);

				if (!multimap.isEmpty()) {
					methodValList = extractMemberValuePair(multimap, method , methodValList);
				}
			}

			if (null != method) {
				action.getMethods().add(method);
			}

		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to extract the array init expression and add it in the
	 * multi map
	 * 
	 * @param multimap
	 * @param entry
	 * @param node4
	 */
	private void extractArrayInitExpr(Multimap<String, NormalAnnotationExpr> multimap,
			Map.Entry<NameExpr, List<Node>> entry, Node node4) {
		if (node4 instanceof ArrayInitializerExpr) {
			for (Node node2 : node4.getChildrenNodes()) {
				if (node2 instanceof NormalAnnotationExpr) {
					multimap.put(entry.getKey().getName(), (NormalAnnotationExpr) node2);
				}
			}
		}
	}

	/**
	 * This method is used to extract the member value pair from the parsed java
	 * class annotation
	 * 
	 * @param multimap
	 * @param method
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private List<Validations> extractMemberValuePair(Multimap<String, NormalAnnotationExpr> multimap, Method method, List<Validations> valList)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		for (Map.Entry<String, NormalAnnotationExpr> multi : multimap.entries()) {

			if (multi.getKey().equals("Validations")) {
				Validations val = new Validations();
				for (Node node : multi.getValue().getChildrenNodes()) {
					if (node instanceof MemberValuePair) {

						Field privateStringField = Validations.class
								.getDeclaredField(((MemberValuePair) node).getName());
						privateStringField.setAccessible(true);
						privateStringField.set(val, ((MemberValuePair) node).getValue().toString()
								.replaceAll(Constants.SEPARATOR_DOUBLE_QUOTES, StringUtils.EMPTY));
					}
				}
				valList.add(val);
			}
		}

		Field privateStringField = Method.class.getDeclaredField("Validations");
		privateStringField.setAccessible(true);
		privateStringField.set(method, valList);
		
		return valList;
	}

}