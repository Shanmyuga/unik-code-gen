/**
 * 
 */
package com.cognizant.fecodegen.service;

import java.util.Map;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.components.render.IRenderer;
import com.cognizant.fecodegen.components.render.RendererPool;
import com.cognizant.fecodegen.exception.CodeGenException;

/**
 * @author 238209
 *
 */
public class RendererFactory {

	public static IRenderer getRenderer(String type, String outFilePath, 
										JsonDocument configJson, Map<String, Object> inputProps, JsonDocument configMain) 
			throws CodeGenException {
		
		IRenderer renderer = null;
		
		switch (type) {
		case "PackageJson":
			renderer = RendererPool.INSTANCE.getPjRenderer();
			break;
			
		case "React":
			renderer = RendererPool.INSTANCE.getReactComponentRenderer();
			break;
		
		case "ReactFramework":
			renderer = RendererPool.INSTANCE.getReactFrameworkRenderer();
			break;
			
		case "ReactAction":
			renderer = RendererPool.INSTANCE.getReactActionRenderer();
			break;
			
		case "ReactService":
			renderer = RendererPool.INSTANCE.getReactServiceRenderer();
			break;
			
		case "ReactReducer":
			renderer = RendererPool.INSTANCE.getReactReducerRenderer();
			break;
		
		case "ReactStore":
			renderer = RendererPool.INSTANCE.getReactStoreRenderer();
			break;
			
		case "ReactResource":
			renderer = RendererPool.INSTANCE.getReactResourceRenderer();
			break;
		
		case "ReactRouter":
			renderer = RendererPool.INSTANCE.getReactRouterRenderer();
			break;
			
		case "ReactContainer":
			renderer = RendererPool.INSTANCE.getReactContainerRenderer();
			break;
			
		case "ReactUnitTests":
			renderer = RendererPool.INSTANCE.getReactUnitTestRenderer();
			break;
			
		case "Pom":
			renderer = RendererPool.INSTANCE.getPomRenderer();
			break;
			
		case "ApplicationProperties":
			renderer = RendererPool.INSTANCE.getApplicationPropertiesRenderer();
			break;
			
		case "Main":
			renderer = RendererPool.INSTANCE.getSpringMainRenderer();
			break;
			
		case "SpringBootDTO":
			renderer = RendererPool.INSTANCE.getSpringDTORenderer();
			break;
			
		case "SpringBootController":
			renderer = RendererPool.INSTANCE.getSpringControllerRenderer();
			break;
			
		case "SpringBootService":
			renderer = RendererPool.INSTANCE.getSpringServiceRenderer();
			break;
			
		case "SpringBootServiceImpl":
			renderer = RendererPool.INSTANCE.getSpringServiceImplRenderer();
			break;
			
		case "SpringBootDAO":
			renderer = RendererPool.INSTANCE.getSpringBootDAORenderer();
			break;
			
		case "SpringBootDAOImpl":
			renderer = RendererPool.INSTANCE.getSpringBootDAOImplRenderer();
			break;
			
		case "SpringBootRepository":
			renderer = RendererPool.INSTANCE.getSpringBootRepositoryRenderer();
			break;
			
		case "SpringBootResponse":
			renderer = RendererPool.INSTANCE.getSpringBootResponseRenderer();
			break;
			
		case "SpringBootEntity":
			renderer = RendererPool.INSTANCE.getSpringBootEntityRenderer();
			break;
		
		case "SpringBootConfig":
			renderer = RendererPool.INSTANCE.getSpringBootCompConfRenderer();
			break;
		
		case "SpringBootConstants":
			renderer = RendererPool.INSTANCE.getSpringBootConstantsRenderer();
			break;
			
		case "SpringBootException":
			renderer = RendererPool.INSTANCE.getSpringBootExceptionRenderer();
			break;
			
		case "SpringBootHelper":
			renderer = RendererPool.INSTANCE.getSpringBootHelperRenderer();
			break;
			
		case "SpringBootUtil":
			renderer = RendererPool.INSTANCE.getSpringBootUtilRenderer();
			break;
			
		case "Angular":
			renderer = RendererPool.INSTANCE.getAngularComponentRenderer();
			break;
			
		default:
			break;
		}
		
		renderer.setOutFilePath(outFilePath);
		renderer.setConfig(configJson);
		renderer.setProperties(inputProps);
		renderer.setCodeGenConfig(configMain);
		return renderer;
	}
	
}
