/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.RouterComponent;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;

/**
 * @author 238209
 *
 */
public class ReactRouterRenderer extends BaseRenderer {

	static Logger LOGGER = Logger.getLogger(ReactRouterRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactRouterRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("Creating Routes... ");
		
		StringBuilder content = new StringBuilder();

		try {
			List<RouterComponent> routerComponents = new ArrayList<>();
			if (checkFileExists(null)) {
				routerComponents = readExistingRouterConfig();
			} 

			if (routerCompExists(routerComponents) == false) {
				RouterComponent routerComp = new RouterComponent();
				routerComp.setContainerName(CodeGenProperties.getCacheCompValueAsString(Constants.CONTAINER_NAME));
				routerComp.setContainerFileName(getRelativeSavedFileName(Constants.CONTAINER_FILENAME));
				routerComp.setSectionName(CodeGenProperties.getCacheCompValueAsString(Constants.SECTION_NAME));

				if (StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), 
						CodeGenProperties.getCacheValueAsString(Constants.GENERATE_LOGIN_SCREEN))) {
					routerComp.setContainerRoute("");
				} else {
					routerComp.setContainerRoute(CodeGenProperties.getCacheCompValueAsString(Constants.CONTAINER_NAME));
				}

				routerComponents.add(routerComp);
			}
			
			Map<String, Object> contextVariables = 
					getParametersMap(content, jsonDoc.getJson(), templateName, false);
			contextVariables.put(Constants.ROUTER_COMPONENTS, routerComponents);


			content.append(parser.parse(templateName, contextVariables));
			CodeGenProperties.putCacheValue(Constants.ROUTER_FILENAME, getSavedFileName());
			CodeGenProperties.putCacheValue(Constants.ROUTER_COMPONENTS, routerComponents);
			
			write(content.toString());
		} catch (CodeGenException e) {
			LOGGER.error("Error while generating framework files: ", e);
		}

		return false;
	}

	private boolean routerCompExists(List<RouterComponent> routerComponents) {
		boolean routerExists = false;
		
		String containerName = CodeGenProperties.getCacheCompValueAsString(Constants.CONTAINER_NAME);
		for (RouterComponent routerComponent : routerComponents) {
			if (StringUtils.equalsIgnoreCase(containerName, routerComponent.getContainerName())) {
				routerExists = true;
				break;
			}
		}
		
		return routerExists;
	}

	private List<RouterComponent> readExistingRouterConfig() throws CodeGenException {
		Map<String, RouterComponent> routerCompMap = new HashMap<>();
		
		Reader reader = null;
		try {
			reader = new FileReader(getOutputFile(null));
			List<String> lines = IOUtils.readLines(reader);
	
			if (lines != null) {
				for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
					String line = iterator.next();
	
					if (StringUtils.contains(line, "import ") && StringUtils.contains(line, "'.")) {
						String[] importLineArr = line.split(" ");
						if (importLineArr.length > 1) {
							String containerName = importLineArr[1];
							String containerFileName = null;
							
							if (importLineArr.length > 3) {
								containerFileName = importLineArr[3];
								containerFileName = StringUtils.substringBetween(containerFileName, "'");
							}
							
							RouterComponent routerComp = getRouterComponent(routerCompMap, containerName);
							routerComp.setContainerFileName(containerFileName);
						}
					}
					
					if (StringUtils.contains(line, "<Route ")) {
						String containerName = StringUtils.substringBetween(line, "component={", "}");
						
						RouterComponent routerComp = getRouterComponent(routerCompMap, containerName);

						String containerRoute = StringUtils.substringBetween(line, "path='/", "'");
						routerComp.setContainerRoute(containerRoute);
						
						String sectionName = StringUtils.substringBetween(line, "name='", "'");
						routerComp.setSectionName(sectionName);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while reading File: router.js", e);
			throw new CodeGenException(e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return new ArrayList<RouterComponent>(routerCompMap.values());
	}

	private RouterComponent getRouterComponent(Map<String, RouterComponent> routerCompMap, String containerName) {
		RouterComponent routerComp = routerCompMap.get(containerName);
		
		if (routerComp == null) {
			routerComp = new RouterComponent();
			routerComp.setContainerName(containerName);
			routerComp.setContainerRoute(StringUtils.EMPTY);
			routerCompMap.put(containerName, routerComp);
		}
		
		return routerComp;
	}
}
