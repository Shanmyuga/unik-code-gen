package com.cognizant.fecodegen.components.render;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cognizant.fecodegen.bo.JsonDocument;

/**
 * To add the TypeScript declartions and methods
 * 
 * @author 696900
 *
 */
public class TypeScriptComponentGenerator {
	
	protected JsonDocument jsonDocument;
	boolean stubCreationFlag ;
	Map<String, String> declarationContent =new HashMap<String, String>();
	List<String> methodClickActions = new ArrayList<String>();
	String lastDeclartionString = "";
	
	/**
	 * Parse the UILayout from the given JSON
	 * 
	 * @param filepath
	 * @param fileName
	 * @param componentName
	 * @param uiLayout
	 */
	public void parseComponentsFromUiLayout(String filepath, String fileName, String componentName, String uiLayout) {
		
		JSONObject uiLayoutJsonObj = new JSONObject(uiLayout);
		JSONArray layoutJsonArr = uiLayoutJsonObj.getJSONArray("layout");
		for(int i= 0; i <layoutJsonArr.length(); i++){
			
			JSONObject jsonObjDoc =  (JSONObject) layoutJsonArr.get(i);
			
			if(jsonObjDoc.has("action"))
				methodClickActions.add(jsonObjDoc.getString("action"));
			
			JSONArray jsonColumnsArrObj =  jsonObjDoc.getJSONArray("columns");
			for(int j=0; j < jsonColumnsArrObj.length(); j++ ){
				
				JSONObject jsonColumnObj = jsonColumnsArrObj.getJSONObject(j);
				
				String type = jsonColumnObj.getString("type");
				String htmlID = jsonColumnObj.getString("htmlID");
				
				declarationContent.put(type, htmlID);
				System.out.println(""+declarationContent);
			}
		}
		
		writeDeclarationContentIntoTS(filepath, fileName);
		
		writeTSMethodStubsContentIntoTS(filepath, fileName);
	}
	
	
	/**
	 * Write the declaration Content into Temp TS file
	 * 
	 * @param filePath
	 * @param fileName
	 */
	public void writeDeclarationContentIntoTS(String filePath, String fileName){
		
		BufferedReader br= null;
		BufferedWriter bw= null;
		FileWriter fw = null;
		FileReader fr = null;
		
		lastDeclartionString = "";
		String oldFileName = fileName;
	    String tmpFileName = "tmp_"+fileName;
		try {
			
			File oldFile = new File(filePath + File.separator + oldFileName);
			File tmpFile = new File(filePath + File.separator + tmpFileName);
			
			fr = new FileReader(oldFile);
			br = new BufferedReader(fr);
			fw = new FileWriter(tmpFile.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			String st;
			boolean writeFlag = false;
			while ((st = br.readLine()) != null) {
				
				if(writeFlag) {
					
					for (Map.Entry<String, String> entry : declarationContent.entrySet())
					{
						System.out.println("Writing into file...");
						bw.write(entry.getValue()+": String;\n\r");
						lastDeclartionString = entry.getValue()+": String;";
						System.out.println("Completed");
					}
					writeFlag = false;
				}
				
				if (st.startsWith("export")) {
					writeFlag = true;
				}
				bw.write(st);
				bw.append("\n");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
				if(fr != null)
					fr.close();
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		deleteFile(filePath, fileName);
	}
	
	
	public void writeTSMethodStubsContentIntoTS(String filePath, String fileName) {
		
		BufferedReader br= null;
		BufferedWriter bw= null;
		FileWriter fw = null;
		FileReader fr = null;
		
		String oldFileName = fileName;
	    String tmpFileName = "tmp_"+fileName;
		try {
			
			File oldFile = new File(filePath + File.separator + oldFileName);
			File tmpFile = new File(filePath + File.separator + tmpFileName);
			
			fr = new FileReader(oldFile);
			br = new BufferedReader(fr);
			fw = new FileWriter(tmpFile.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			String st;
			boolean writeFlag = false;
			while ((st = br.readLine()) != null) {
				
				
				if(writeFlag){
					
					for(String clickAction : methodClickActions){
						
						System.out.println("Writing stubs into file...");
						bw.write(clickAction +"() {"+ "\n\r" + "}" );
						System.out.println("Completed");
					}
					
					writeFlag =false;
				}
				
				if(st.contains(lastDeclartionString)){
					writeFlag =true;
				}
				bw.write(st);
				bw.append("\n");

			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
				if(fr != null)
					fr.close();
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		deleteFile(filePath, fileName);
	}
	
	
	/**
	 * Delete the Temporary file which created to write
	 * 
	 * @param filePath
	 * @param fileName
	 */
	public void deleteFile(String filePath, String fileName) {
		
		// Once everything is complete, delete old file..
		File oldFile = new File(filePath + File.separator +fileName);
		oldFile.delete();

		// And rename tmp file's name to old file name
		File newFile = new File(filePath + File.separator +"tmp_"+fileName);
		newFile.renameTo(oldFile);
		
	}
	
}
