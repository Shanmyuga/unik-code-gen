package ${package}.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Table(name = "${requestUrl}")
public class ${className}Entity {
	
	@Id
	<#list member as variable> 
	@Column(name = "${variable.get("VariableName").getAsString()}")  
    private ${variable.get("DataType").getAsString()} ${variable.get("VariableName").getAsString()};
    </#list>
    
    public ${className}Entity(){}
	
    <#list member as variable>  
    public ${variable.get("DataType").getAsString()} get${variable.get("methodName").getAsString()}(){
        return ${variable.get("VariableName").getAsString()};
    }
    public void set${variable.get("methodName").getAsString()}(${variable.get("DataType").getAsString()} ${variable.get("VariableName").getAsString()}){        
        this.${variable.get("VariableName").getAsString()} = ${variable.get("VariableName").getAsString()};
    } 
    </#list>
   
}
