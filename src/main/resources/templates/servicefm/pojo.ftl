package ${package}.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
 
public class ${className} {
	
	<#list member as variable>  
	@JsonProperty(${variable.get("VariableName")}) 
    private ${variable.get("DataType").getAsString()} ${variable.get("VariableName").getAsString()};
    </#list>
    
    public ${className}(){}
    
    <#list member as variable>  
    public ${variable.get("DataType").getAsString()} get${variable.get("methodName").getAsString()}(){
        return ${variable.get("VariableName").getAsString()};
    }
    public void set${variable.get("methodName").getAsString()}(${variable.get("DataType").getAsString()} ${variable.get("VariableName").getAsString()}){        
        this.${variable.get("VariableName").getAsString()} = ${variable.get("VariableName").getAsString()};
    } 
    </#list>
   
}