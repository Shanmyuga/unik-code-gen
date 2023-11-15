/**
 * 
 */
package com.cognizant.fecodegen.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 238209
 *
 */
@RestController
public class TestResource {
	
	@GetMapping("/test")
	public @ResponseBody String testSuccess() {
		return "Success !!";
	}
	
}
