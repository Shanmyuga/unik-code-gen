/**
 * 
 */
package com.cognizant.fecodegen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cognizant.fecodegen.CodeGenTemplateParser;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.components.AngularJsGenerator;
import com.cognizant.fecodegen.components.GeneratorPool;
import com.cognizant.fecodegen.components.ReactJsGenerator;
import com.cognizant.fecodegen.components.SpringBootGenerator;
import com.cognizant.fecodegen.components.SpringBootGeneratorPool;
import com.cognizant.fecodegen.components.SpringBootLayoutGenerator;
import com.cognizant.fecodegen.components.render.AngularComponentRenderer;
import com.cognizant.fecodegen.components.render.BaseRenderer;
import com.cognizant.fecodegen.components.render.PackageJsonRenderer;
import com.cognizant.fecodegen.components.render.ReactActionRenderer;
import com.cognizant.fecodegen.components.render.ReactComponentRenderer;
import com.cognizant.fecodegen.components.render.ReactContainerRenderer;
import com.cognizant.fecodegen.components.render.ReactFrameworkRenderer;
import com.cognizant.fecodegen.components.render.ReactReducerRenderer;
import com.cognizant.fecodegen.components.render.ReactResourceRenderer;
import com.cognizant.fecodegen.components.render.ReactRouterRenderer;
import com.cognizant.fecodegen.components.render.ReactServiceRenderer;
import com.cognizant.fecodegen.components.render.ReactStoreRenderer;
import com.cognizant.fecodegen.components.render.ReactUnitTestRenderer;
import com.cognizant.fecodegen.components.render.RendererPool;
import com.cognizant.fecodegen.components.render.SpringBootAppPropertiesRenderer;
import com.cognizant.fecodegen.components.render.SpringBootCompConfRenderer;
import com.cognizant.fecodegen.components.render.SpringBootConstantsRenderer;
import com.cognizant.fecodegen.components.render.SpringBootControllerRenderer;
import com.cognizant.fecodegen.components.render.SpringBootDAOImplRenderer;
import com.cognizant.fecodegen.components.render.SpringBootDAORenderer;
import com.cognizant.fecodegen.components.render.SpringBootDTORenderer;
import com.cognizant.fecodegen.components.render.SpringBootEntityRenderer;
import com.cognizant.fecodegen.components.render.SpringBootExceptionRenderer;
import com.cognizant.fecodegen.components.render.SpringBootHelperRenderer;
import com.cognizant.fecodegen.components.render.SpringBootMainRenderer;
import com.cognizant.fecodegen.components.render.SpringBootPomRenderer;
import com.cognizant.fecodegen.components.render.SpringBootRepositoryRenderer;
import com.cognizant.fecodegen.components.render.SpringBootResponseRenderer;
import com.cognizant.fecodegen.components.render.SpringBootServiceImplRenderer;
import com.cognizant.fecodegen.components.render.SpringBootServiceRenderer;
import com.cognizant.fecodegen.components.render.SpringBootUtilRenderer;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.HelperUtil;

/**
 * @author 238209
 *
 */
@Configuration
public class ComponentConfiguration {

	@Bean
	public GeneratorPool generatorPool() {
		return GeneratorPool.getInstance();
	}
	
	@Bean
	public RendererPool rendererPool() {
		return RendererPool.getInstance();
	}

	@Bean
	public ReactJsGenerator reactJsGenerator() {
		return new ReactJsGenerator();
	}
	
	@Bean
	public AngularJsGenerator angularJsGenerator() {
		return new AngularJsGenerator();
	}

	@Bean
	public CodeGenTemplateParser codeGenTemplateParser() {
		return new CodeGenTemplateParser();
	}
	
	@Bean
	public PackageJsonRenderer packageJsonRenderer() {
		return new PackageJsonRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactComponentRenderer reactComponentRenderer() {
		return new ReactComponentRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public BaseRenderer reactActionRenderer() {
		return new ReactActionRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactServiceRenderer reactServiceRenderer() {
		return new ReactServiceRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactReducerRenderer reactReducerRenderer() {
		return new ReactReducerRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactStoreRenderer reactStoreRenderer() {
		return new ReactStoreRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactFrameworkRenderer reactFrameworkRenderer() {
		return new ReactFrameworkRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactResourceRenderer reactResourceRenderer() {
		return new ReactResourceRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactUnitTestRenderer reactUnitTestRenderer() {
		return new ReactUnitTestRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public BaseRenderer reactRouterRenderer() {
		return new ReactRouterRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public ReactContainerRenderer reactContainerRenderer() {
		return new ReactContainerRenderer(CodeGenProperties.getProps());
	}
	
	
	@Bean
	public SpringBootGeneratorPool springBootGeneratorPool() {
		return SpringBootGeneratorPool.getInstance();
	}
	
	@Bean
	public SpringBootGenerator springBootGenerator() {
		return new SpringBootGenerator();
	}
	
	@Bean
	public UILayout uiLayout() {
		return new UILayout();
	}
	
	@Bean
	public SpringBootLayoutGenerator springBootLayoutGenerator() {
		return new SpringBootLayoutGenerator();
	}
	
	@Bean
	public SpringBootPomRenderer pomRenderer() {
		return new SpringBootPomRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootAppPropertiesRenderer applicationPropertiesRenderer() {
		return new SpringBootAppPropertiesRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootMainRenderer springMainRenderer() {
		return new SpringBootMainRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootDTORenderer springDTORenderer() {
		return new SpringBootDTORenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootControllerRenderer springControllerRenderer() {
		return new SpringBootControllerRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootServiceRenderer springServiceRenderer() {
		return new SpringBootServiceRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootDAORenderer springBootDAORenderer() {
		return new SpringBootDAORenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootDAOImplRenderer springBootDAOImplRenderer() {
		return new SpringBootDAOImplRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootServiceImplRenderer springServiceImplRenderer() {
		return new SpringBootServiceImplRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootRepositoryRenderer springRepositoryRenderer() {
		return new SpringBootRepositoryRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootResponseRenderer springResponseRenderer() {
		return new SpringBootResponseRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootEntityRenderer springEntityRenderer() {
		return new SpringBootEntityRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootCompConfRenderer springBootCompConfRenderer() {
		return new SpringBootCompConfRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootConstantsRenderer springBootConstantsRenderer() {
		return new SpringBootConstantsRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootExceptionRenderer springBootExceptionRenderer() {
		return new SpringBootExceptionRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootHelperRenderer springBootHelperRenderer() {
		return new SpringBootHelperRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public SpringBootUtilRenderer springBootUtilRenderer() {
		return new SpringBootUtilRenderer(CodeGenProperties.getProps());
	}
	
	@Bean
	public HelperUtil helperUtil() {
		return new HelperUtil();
	}
	
	@Bean
	public BaseRenderer angularComponentRenderer() {
		return new AngularComponentRenderer(CodeGenProperties.getProps());
	}
}
