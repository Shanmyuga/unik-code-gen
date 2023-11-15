/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public class ZipUtils {
    public static void main(String[] args) {
        //File file = new File("/Users/pankaj/sitemap.xml");
        //String zipFileName = "/Users/pankaj/sitemap.zip";
        
        File dir = new File("D:\\238209\\Technical\\angular-samples\\angular-ui-gen\\angular-demo");
        String zipDirName = "D:\\238209\\Technical\\angular-samples\\angular-ui-gen\\angular-demo.zip";
        
        //zipSingleFile(file, zipFileName);
        
        ZipUtils.zipDirectory(dir, zipDirName);
    }

    public static void zipDirectoryByOs (String zipDirName) {
    	File dir = new File(zipDirName);
    	
    	boolean isWindows = System.getProperty("os.name")
				  .toLowerCase().startsWith("windows");
    	
    	try {
			if (isWindows) {
				zipDirectory(dir, zipDirName + getZipExtension());
			} else {
				String folderName = null;
				String folderPath = null;
				if (StringUtils.contains(zipDirName, "\\")) {
					folderName = StringUtils.substringAfterLast(zipDirName, "\\");
					folderPath = StringUtils.substringBeforeLast(zipDirName, "\\");
				} else {
					folderName = StringUtils.substringAfterLast(zipDirName, Constants.FORWARD_SLASH);
					folderPath = StringUtils.substringBeforeLast(zipDirName, Constants.FORWARD_SLASH);
				}
				
				HelperUtil.codeGenerate("cd " + folderPath + " && tar -zcvf " + StringUtils.trim(folderName) 
												+ getZipExtension() + " " + folderName);
			}
		} catch (CodeGenException e) {
			//
		}
    }
    
    /**
     * This method zips the directory
     * @param dir
     * @param zipDirName
     */
    public static void zipDirectory(File dir, String zipDirName) {
        try {
        	List<String> filesListInDir = populateFilesList(dir);
            //now zip files one by one
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipDirName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for(String filePath : filesListInDir){
                System.out.println("Zipping "+filePath);
                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
                zos.putNextEntry(ze);
                //read the file and write to ZipOutputStream
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method populates all the files in a directory to a List
     * @param dir
     * @return 
     * @throws IOException
     */
    private static List<String> populateFilesList(File dir) throws IOException {
    	List<String> filesListInDir = new ArrayList<String>();
    	
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()) { 
            	if (file.getAbsolutePath().contains("node_modules") == false
            			&& file.getAbsolutePath().contains(".git") == false) {
            		filesListInDir.add(file.getAbsolutePath());
            	}
            } else if (StringUtils.equalsIgnoreCase(file.getName(), "node_modules") == false 
            		&& StringUtils.equalsIgnoreCase(file.getName(), ".git") == false) { 
            	filesListInDir.addAll(populateFilesList(file));
            }
        }
        
        return filesListInDir;
    }

    /**
     * This method compresses the single file to zip format
     * @param file
     * @param zipFileName
     */
    public static void zipSingleFile(File file, String zipFileName) {
        try {
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //add a new Zip Entry to the ZipOutputStream
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            
            //Close the zip entry to write to zip file
            zos.closeEntry();
            //Close resources
            zos.close();
            fis.close();
            fos.close();
            System.out.println(file.getCanonicalPath()+" is zipped to "+zipFileName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public static String getZipExtension() {
    	boolean isWindows = System.getProperty("os.name")
				  .toLowerCase().startsWith("windows");
    	
    	if (isWindows) {
    		return ".zip";
    	}
    	
    	return ".tar.gz";
    }
}
