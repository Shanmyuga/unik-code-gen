package com.cognizant.fecodegen.visitor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.bo.jsonoutput.Action;
import com.cognizant.fecodegen.bo.jsonoutput.Result;
import com.cognizant.fecodegen.bo.jsonoutput.Validations;
import com.cognizant.fecodegen.utils.Constants;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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
 * This class is used to extract all the class level details for the actin class
 * 
 * @author 244898
 *
 */
public class ClassVisitor extends VoidVisitorAdapter {

	Map<NameExpr, List<Node>> classDetails = new HashMap<NameExpr, List<Node>>();

	public void visit(ClassOrInterfaceDeclaration n, Object arg) {

		Map<String, Action> actionMap = (HashMap) arg;
		Action action = new Action();

		try {
			action.setFileName(n.getName());
			List<AnnotationExpr> annotations = n.getAnnotations();

			// Adding all the class level annotations to the map
			for (AnnotationExpr ann : annotations) {
				classDetails.put(ann.getName(), ann.getChildrenNodes());
			}

			Multimap<String, NormalAnnotationExpr> multimap;

			// Iterating over the annotations map
			for (Map.Entry<NameExpr, List<Node>> entry : classDetails.entrySet()) {

				multimap = ArrayListMultimap.create();

				for (Node node1 : entry.getValue()) {
					if (node1 instanceof StringLiteralExpr) {
						// Setting the values to the object using reflections
						Field privateStringField = Action.class.getDeclaredField(entry.getKey().getName());
						privateStringField.setAccessible(true);
						privateStringField.set(action, ((StringLiteralExpr) node1).getValue());
					}
					if (node1 instanceof MemberValuePair) {
						for (Node node4 : node1.getChildrenNodes()) {
							extractArrayInitExpr(multimap, entry, node4);
						}
					}
					extractArrayInitExpr(multimap, entry, node1);
				}
				extractMemberValuePair(multimap, action);
			}
			actionMap.put(n.getName(), action);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to extract the member value pair from the parsed java
	 * class annotation
	 * 
	 * @param multimap
	 * @param action
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void extractMemberValuePair(Multimap<String, NormalAnnotationExpr> multimap, Action action)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<Validations> valList = new ArrayList<>();
		List<Result> resList = new ArrayList<>();

		for (Map.Entry<String, NormalAnnotationExpr> multi : multimap.entries()) {

			// Setting the validation annotation values to the object
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

				Field privateStringField = Action.class.getDeclaredField(multi.getKey());
				privateStringField.setAccessible(true);
				privateStringField.set(action, valList);
			}

			// Setting the results annotation values to the object
			if (multi.getKey().equals("Results")) {
				Result res = new Result();
				for (Node node : multi.getValue().getChildrenNodes()) {
					if (node instanceof MemberValuePair) {

						Field privateStringField = Result.class.getDeclaredField(((MemberValuePair) node).getName());
						privateStringField.setAccessible(true);
						privateStringField.set(res, ((MemberValuePair) node).getValue().toString()
								.replaceAll(Constants.SEPARATOR_DOUBLE_QUOTES, StringUtils.EMPTY));
					}
				}
				resList.add(res);

				Field privateStringField = Action.class.getDeclaredField(multi.getKey());
				privateStringField.setAccessible(true);
				privateStringField.set(action, resList);
			}

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
}