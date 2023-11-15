/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 238209
 *
 */
public class RendererPool {

	public static RendererPool INSTANCE;
	
	public static RendererPool getInstance() {
		if (INSTANCE == null) {
			 INSTANCE = new RendererPool();
		}
		
		return INSTANCE;
	}
	
	@Autowired
	private PackageJsonRenderer pjRenderer;
	
	@Autowired
	private ReactComponentRenderer reactComponentRenderer;
	
	@Autowired
	private BaseRenderer reactActionRenderer;
	
	@Autowired
	private ReactServiceRenderer reactServiceRenderer;
	
	@Autowired
	private ReactReducerRenderer reactReducerRenderer;
	
	@Autowired
	private ReactStoreRenderer reactStoreRenderer;

	@Autowired
	private ReactFrameworkRenderer reactFrameworkRenderer;
	
	@Autowired
	private ReactResourceRenderer reactResourceRenderer;
	
	@Autowired
	private BaseRenderer reactRouterRenderer;
	
	@Autowired
	private ReactContainerRenderer reactContainerRenderer;
	
	@Autowired
	private ReactUnitTestRenderer reactUnitTestRenderer;
	
	@Autowired
	private SpringBootDTORenderer springBootLayoutRenderer;
	
	@Autowired
	private SpringBootPomRenderer pomRenderer;
	
	@Autowired
	private SpringBootAppPropertiesRenderer applicationPropertiesRenderer;
	
	@Autowired
	private SpringBootMainRenderer springMainRenderer;
	
	@Autowired
	private SpringBootDTORenderer springDTORenderer;
	
	@Autowired
	private SpringBootControllerRenderer springControllerRenderer;
	
	@Autowired
	private SpringBootServiceRenderer springServiceRenderer;
	
	@Autowired
	private SpringBootServiceImplRenderer springServiceImplRenderer;
	
	@Autowired
	private SpringBootDAORenderer springBootDAORenderer;
	
	@Autowired
	private SpringBootDAOImplRenderer springBootDAOImplRenderer;
	
	@Autowired
	private SpringBootRepositoryRenderer springBootRepositoryRenderer;
	
	@Autowired
	private SpringBootResponseRenderer springBootResponseRenderer;
	
	@Autowired
	private SpringBootEntityRenderer springBootEntityRenderer;
	
	@Autowired
	private SpringBootCompConfRenderer springBootCompConfRenderer;
	
	@Autowired
	private SpringBootConstantsRenderer springBootConstantsRenderer;
	
	@Autowired
	private SpringBootExceptionRenderer springBootExceptionRenderer;
	
	@Autowired
	private SpringBootHelperRenderer springBootHelperRenderer;
	
	@Autowired
	private SpringBootUtilRenderer springBootUtilRenderer;
	
	@Autowired
	private BaseRenderer angularComponentRenderer;
	
	
	

	/**
	 * @return the angularComponentRenderer
	 */
	public BaseRenderer getAngularComponentRenderer() {
		return angularComponentRenderer;
	}

	/**
	 * @param angularComponentRenderer the angularComponentRenderer to set
	 */
	public void setAngularComponentRenderer(BaseRenderer angularComponentRenderer) {
		this.angularComponentRenderer = angularComponentRenderer;
	}

	/**
	 * @return the pjRenderer
	 */
	public PackageJsonRenderer getPjRenderer() {
		return pjRenderer;
	}

	/**
	 * @param pjRenderer the pjRenderer to set
	 */
	public void setPjRenderer(PackageJsonRenderer pjRenderer) {
		this.pjRenderer = pjRenderer;
	}

	/**
	 * @return the reactComponentRenderer
	 */
	public ReactComponentRenderer getReactComponentRenderer() {
		return reactComponentRenderer;
	}

	/**
	 * @param reactComponentRenderer the reactComponentRenderer to set
	 */
	public void setReactComponentRenderer(ReactComponentRenderer reactComponentRenderer) {
		this.reactComponentRenderer = reactComponentRenderer;
	}

	/**
	 * @return the reactActionRenderer
	 */
	public BaseRenderer getReactActionRenderer() {
		return reactActionRenderer;
	}

	/**
	 * @param reactActionRenderer the reactActionRenderer to set
	 */
	public void setReactActionRenderer(BaseRenderer reactActionRenderer) {
		this.reactActionRenderer = reactActionRenderer;
	}

	/**
	 * @return the reactServiceRenderer
	 */
	public ReactServiceRenderer getReactServiceRenderer() {
		return reactServiceRenderer;
	}

	/**
	 * @param reactServiceRenderer the reactServiceRenderer to set
	 */
	public void setReactServiceRenderer(ReactServiceRenderer reactServiceRenderer) {
		this.reactServiceRenderer = reactServiceRenderer;
	}

	/**
	 * @return the reactReducerRenderer
	 */
	public ReactReducerRenderer getReactReducerRenderer() {
		return reactReducerRenderer;
	}

	/**
	 * @param reactReducerRenderer the reactReducerRenderer to set
	 */
	public void setReactReducerRenderer(ReactReducerRenderer reactReducerRenderer) {
		this.reactReducerRenderer = reactReducerRenderer;
	}

	/**
	 * @return the reactStoreRenderer
	 */
	public ReactStoreRenderer getReactStoreRenderer() {
		return reactStoreRenderer;
	}

	/**
	 * @param reactStoreRenderer the reactStoreRenderer to set
	 */
	public void setReactStoreRenderer(ReactStoreRenderer reactStoreRenderer) {
		this.reactStoreRenderer = reactStoreRenderer;
	}

	/**
	 * @return the reactFrameworkRenderer
	 */
	public ReactFrameworkRenderer getReactFrameworkRenderer() {
		return reactFrameworkRenderer;
	}

	/**
	 * @param reactFrameworkRenderer the reactFrameworkRenderer to set
	 */
	public void setReactFrameworkRenderer(ReactFrameworkRenderer reactFrameworkRenderer) {
		this.reactFrameworkRenderer = reactFrameworkRenderer;
	}

	/**
	 * @return the reactResourceRenderer
	 */
	public ReactResourceRenderer getReactResourceRenderer() {
		return reactResourceRenderer;
	}

	/**
	 * @param reactResourceRenderer the reactResourceRenderer to set
	 */
	public void setReactResourceRenderer(ReactResourceRenderer reactResourceRenderer) {
		this.reactResourceRenderer = reactResourceRenderer;
	}

	/**
	 * @return the reactRouterRenderer
	 */
	public BaseRenderer getReactRouterRenderer() {
		return reactRouterRenderer;
	}

	/**
	 * @param reactRouterRenderer the reactRouterRenderer to set
	 */
	public void setReactRouterRenderer(BaseRenderer reactRouterRenderer) {
		this.reactRouterRenderer = reactRouterRenderer;
	}

	/**
	 * @return the reactContainerRenderer
	 */
	public ReactContainerRenderer getReactContainerRenderer() {
		return reactContainerRenderer;
	}

	/**
	 * @param reactContainerRenderer the reactContainerRenderer to set
	 */
	public void setReactContainerRenderer(ReactContainerRenderer reactContainerRenderer) {
		this.reactContainerRenderer = reactContainerRenderer;
	}
	
	/**
	 * @return the springBootLayoutRenderer
	 */
	public SpringBootDTORenderer getSpringBootLayoutRenderer() {
		return springBootLayoutRenderer;
	}

	/**
	 * @param springBootLayoutRenderer the springBootLayoutRenderer to set
	 */
	public void setSpringBootLayoutRenderer(SpringBootDTORenderer springBootLayoutRenderer) {
		this.springBootLayoutRenderer = springBootLayoutRenderer;
	}
	
	/**
	 * @return the pomRenderer
	 */
	public SpringBootPomRenderer getPomRenderer() {
		return pomRenderer;
	}

	/**
	 * @param pomRenderer the pomRenderer to set
	 */
	public void setPomRenderer(SpringBootPomRenderer pomRenderer) {
		this.pomRenderer = pomRenderer;
	}

	/**
	 * @return the applicationPropertiesRenderer
	 */
	public SpringBootAppPropertiesRenderer getApplicationPropertiesRenderer() {
		return applicationPropertiesRenderer;
	}

	/**
	 * @param applicationPropertiesRenderer the applicationPropertiesRenderer to set
	 */
	public void setApplicationPropertiesRenderer(SpringBootAppPropertiesRenderer applicationPropertiesRenderer) {
		this.applicationPropertiesRenderer = applicationPropertiesRenderer;
	}

	/**
	 * @return the springMainRenderer
	 */
	public SpringBootMainRenderer getSpringMainRenderer() {
		return springMainRenderer;
	}

	/**
	 * @param springMainRenderer the springMainRenderer to set
	 */
	public void setSpringMainRenderer(SpringBootMainRenderer springMainRenderer) {
		this.springMainRenderer = springMainRenderer;
	}
	
	/**
	 * @return the springDTORenderer
	 */
	public SpringBootDTORenderer getSpringDTORenderer() {
		return springDTORenderer;
	}

	/**
	 * @param springDTORenderer the springDTORenderer to set
	 */
	public void setSpringDTORenderer(SpringBootDTORenderer springDTORenderer) {
		this.springDTORenderer = springDTORenderer;
	}

	/**
	 * @return the springControllerRenderer
	 */
	public SpringBootControllerRenderer getSpringControllerRenderer() {
		return springControllerRenderer;
	}

	/**
	 * @param springControllerRenderer the springControllerRenderer to set
	 */
	public void setSpringControllerRenderer(SpringBootControllerRenderer springControllerRenderer) {
		this.springControllerRenderer = springControllerRenderer;
	}

	/**
	 * @return the springServiceRenderer
	 */
	public SpringBootServiceRenderer getSpringServiceRenderer() {
		return springServiceRenderer;
	}

	/**
	 * @param springServiceRenderer the springServiceRenderer to set
	 */
	public void setSpringServiceRenderer(SpringBootServiceRenderer springServiceRenderer) {
		this.springServiceRenderer = springServiceRenderer;
	}

	/**
	 * @return the springServiceImplRenderer
	 */
	public SpringBootServiceImplRenderer getSpringServiceImplRenderer() {
		return springServiceImplRenderer;
	}

	/**
	 * @param springServiceImplRenderer the springServiceImplRenderer to set
	 */
	public void setSpringServiceImplRenderer(SpringBootServiceImplRenderer springServiceImplRenderer) {
		this.springServiceImplRenderer = springServiceImplRenderer;
	}
	
	/**
	 * @return the springBootDAORenderer
	 */
	public SpringBootDAORenderer getSpringBootDAORenderer() {
		return springBootDAORenderer;
	}

	/**
	 * @param springBootDAORenderer the springBootDAORenderer to set
	 */
	public void setSpringBootDAORenderer(SpringBootDAORenderer springBootDAORenderer) {
		this.springBootDAORenderer = springBootDAORenderer;
	}

	/**
	 * @return the springBootDAOImplRenderer
	 */
	public SpringBootDAOImplRenderer getSpringBootDAOImplRenderer() {
		return springBootDAOImplRenderer;
	}

	/**
	 * @param springBootDAOImplRenderer the springBootDAOImplRenderer to set
	 */
	public void setSpringBootDAOImplRenderer(SpringBootDAOImplRenderer springBootDAOImplRenderer) {
		this.springBootDAOImplRenderer = springBootDAOImplRenderer;
	}

	/**
	 * @return the springBootRepositoryRenderer
	 */
	public SpringBootRepositoryRenderer getSpringBootRepositoryRenderer() {
		return springBootRepositoryRenderer;
	}

	/**
	 * @param springBootRepositoryRenderer the springBootRepositoryRenderer to set
	 */
	public void setSpringBootRepositoryRenderer(SpringBootRepositoryRenderer springBootRepositoryRenderer) {
		this.springBootRepositoryRenderer = springBootRepositoryRenderer;
	}

	/**
	 * @return the SpringBootResponseRenderer
	 */
	public SpringBootResponseRenderer getSpringBootResponseRenderer() {
		return springBootResponseRenderer;
	}

	/**
	 * @param springBootResponseRenderer the springBootResponseRenderer to set
	 */
	public void setSpringBootResponseRenderer(SpringBootResponseRenderer springBootResponseRenderer) {
		this.springBootResponseRenderer = springBootResponseRenderer;
	}

	/**
	 * @return the springBootEntityRenderer
	 */
	public SpringBootEntityRenderer getSpringBootEntityRenderer() {
		return springBootEntityRenderer;
	}

	/**
	 * @param springBootEntityRenderer the springBootEntityRenderer to set
	 */
	public void setSpringBootEntityRenderer(SpringBootEntityRenderer springBootEntityRenderer) {
		this.springBootEntityRenderer = springBootEntityRenderer;
	}

	/**
	 * @return the springBootCompConfRenderer
	 */
	public SpringBootCompConfRenderer getSpringBootCompConfRenderer() {
		return springBootCompConfRenderer;
	}

	/**
	 * @param springBootCompConfRenderer the springBootCompConfRenderer to set
	 */
	public void setSpringBootCompConfRenderer(SpringBootCompConfRenderer springBootCompConfRenderer) {
		this.springBootCompConfRenderer = springBootCompConfRenderer;
	}

	/**
	 * @return the springBootConstantsRenderer
	 */
	public SpringBootConstantsRenderer getSpringBootConstantsRenderer() {
		return springBootConstantsRenderer;
	}

	/**
	 * @param springBootConstantsRenderer the springBootConstantsRenderer to set
	 */
	public void setSpringBootConstantsRenderer(SpringBootConstantsRenderer springBootConstantsRenderer) {
		this.springBootConstantsRenderer = springBootConstantsRenderer;
	}

	/**
	 * @return the springBootExceptionRenderer
	 */
	public SpringBootExceptionRenderer getSpringBootExceptionRenderer() {
		return springBootExceptionRenderer;
	}

	/**
	 * @param springBootExceptionRenderer the springBootExceptionRenderer to set
	 */
	public void setSpringBootExceptionRenderer(SpringBootExceptionRenderer springBootExceptionRenderer) {
		this.springBootExceptionRenderer = springBootExceptionRenderer;
	}

	/**
	 * @return the springBootHelperRenderer
	 */
	public SpringBootHelperRenderer getSpringBootHelperRenderer() {
		return springBootHelperRenderer;
	}

	/**
	 * @param springBootHelperRenderer the springBootHelperRenderer to set
	 */
	public void setSpringBootHelperRenderer(SpringBootHelperRenderer springBootHelperRenderer) {
		this.springBootHelperRenderer = springBootHelperRenderer;
	}

	/**
	 * @return the springBootUtilRenderer
	 */
	public SpringBootUtilRenderer getSpringBootUtilRenderer() {
		return springBootUtilRenderer;
	}

	/**
	 * @param springBootUtilRenderer the springBootUtilRenderer to set
	 */
	public void setSpringBootUtilRenderer(SpringBootUtilRenderer springBootUtilRenderer) {
		this.springBootUtilRenderer = springBootUtilRenderer;
	}

	/**
	 * @return the reactUnitTestRenderer
	 */
	public ReactUnitTestRenderer getReactUnitTestRenderer() {
		return reactUnitTestRenderer;
	}

	/**
	 * @param reactUnitTestRenderer the reactUnitTestRenderer to set
	 */
	public void setReactUnitTestRenderer(ReactUnitTestRenderer reactUnitTestRenderer) {
		this.reactUnitTestRenderer = reactUnitTestRenderer;
	}

	
}
