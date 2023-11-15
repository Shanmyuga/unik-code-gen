/**
 * 
 */
package com.cognizant.fecodegen.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author 238209
 *
 */
@Configuration
public class CorsFilterConfiguration implements Filter {
	//implements ContainerResponseFilter
	//public void filter()
	private static Logger logger = Logger.getLogger(CorsFilterConfiguration.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String[] allowDomain = {"http://localhost:8080",
								"http://ec2-54-244-200-41.us-west-2.compute.amazonaws.com:8080"};

		Set<String> allowedOrigins = new HashSet<String>(Arrays.asList (allowDomain));   

	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    String originHeader = httpRequest.getHeader("Origin");
	    
	    HttpServletResponse httpResponse = (HttpServletResponse) response;
	    if (allowedOrigins.contains(originHeader)) {
	    	httpResponse.setHeader("Access-Control-Allow-Origin", originHeader);
	    }
	    
	    httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
	    httpResponse.setHeader("Access-Control-Allow-Headers", "access-control-allow-origin, origin, accept, authorization, X-Auth-Token, Content-Type");
	    httpResponse.setHeader("Access-Control-Expose-Headers", "custom-header1, custom-header2");
	    httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
	    httpResponse.setHeader("Access-Control-Max-Age", "4800");

	    logger.info("---CORS Configuration Completed---");
	    chain.doFilter(request, response);
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	@Override
	public void destroy() {
	}
	
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	} 
	
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/login/auth").allowedOrigins("http://localhost:8080");
            }
        };
	}
}
