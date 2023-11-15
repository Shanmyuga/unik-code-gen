<#list ts.imports as imp>
import { <#list imp.importObjects as importObj>${importObj}<#if importObj?has_next>,</#if></#list> } from '${imp.packageNameFull}';
</#list>

@Component({
  selector: ${ts.components["selector"]},
  templateUrl: ${ts.components["templateUrl"]},
  styleUrls: ${ts.components["styleUrls"]}
})
export class ${ts.className} implements <#list ts.implementClasses as implClass>${implClass}<#if implClass?has_next>,</#if></#list> {

<#assign subscription = false>
<#assign rowList = false>
<#if dataTable??><#assign rowListVarName = dataTable[0].htmlId + "List"><#else><#assign rowListVarName = htmlID + "List"></#if>

<#if ts.declarations??>
<#list ts.declarations as declaration>
  <#if declaration.annotation??>@${declaration.annotation}()</#if> ${declaration.variableName}<#if declaration.annotation??> = new ${declaration.dataType}()<#else><#if declaration.dataType??>: ${declaration.dataType}</#if></#if><#if declaration.defaultValue?? && declaration.defaultValue != "">= [{prop: 'selected',name: '',sortable: false,canAutoResize: false,draggable: false,resizable: false,headerCheckboxable: true,checkboxable: true,width: 30},${declaration.defaultValue}</#if>; 	
  <#if declaration.variableName == "subscription"><#assign subscription = true></#if>
  <#if declaration.variableName == rowListVarName><#assign rowList = true></#if>
  
</#list>
</#if>

<#if dataTable??>
<#if !ts.variableNames['editing']??>
	editing = {
		
	};
</#if>
</#if>

<#if subscription == false>
	subscription: Subscription;
</#if>	
<#if rowList == false>	
  	${rowListVarName}: ${sectionModelName?cap_first}[];
</#if>

<#assign injectData = false>
<#assign mandatory = false>
<#assign displayFieldCss = false>
<#if ts.methods??>
<#list ts.methods as method>
  <#if method.methodName=="displayFieldCss">
  	<#assign displayFieldCss = true>
  </#if>
  <#if method.methodName=="isFieldValid">
	isFieldValid(field: string) {
	      return !this.${method.methodContent}.get(field).valid && this.${method.methodContent}.get(field).touched;
	  }
	<#assign mandatory = true>
	<#else>
	  ${method.methodName}(${method.methodArgs}) {
	  		${method.methodContent}
	  }
  </#if>
  <#if method.methodName == "injectData"><#assign injectData = true></#if>

</#list>
</#if>

<#if injectData == false>
  injectData(${htmlID}Observable: Observable<${sectionModelName?cap_first}[]>) {
      this.subscription = ${htmlID}Observable.subscribe(resp => {
          this.${rowListVarName} = resp;
      });
  }
</#if>  
<#if displayFieldCss == false && mandatory == true>	
displayFieldCss(field: string) {
    return {
      'is-invalid': this.isFieldValid(field),
      'has-feedback': this.isFieldValid(field)
    };
  }
</#if> 
<#if !ts.methodNames['getFieldErrorType']?? && mandatory == true>
  getFieldErrorType(field: string) {
      return this.${htmlID}.get(field).errors;
  }
</#if>

<#if dataTable??>
<#if !ts.methodNames['updateDataTableValue']??>
  updateDataTableValue(event, cell, rowIndex) {
    this.editing[rowIndex + '-' + cell] = false;
    this.${rowListVarName}[rowIndex][cell] = event.target.value;
    this.${rowListVarName}= [...this.${rowListVarName}];
  }
</#if>
</#if>
}
