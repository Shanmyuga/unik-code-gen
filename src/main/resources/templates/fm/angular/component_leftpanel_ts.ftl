<#list ts.imports as imp>
import { <#list imp.importObjects as importObj>${importObj}<#if importObj?has_next>,</#if></#list> } from '${imp.packageNameFull}';
</#list>

@Component({
  selector: ${ts.components["selector"]},
  templateUrl: ${ts.components["templateUrl"]},
  styleUrls: ${ts.components["styleUrls"]}
})
export class ${ts.className} implements <#list ts.implementClasses as implClass>${implClass}<#if implClass?has_next>,</#if></#list> {

<#assign lpToggleEvent = false>
<#assign lpPushDataEvent = false>
<#assign formGroupVariable = false>
<#assign formGroupObs = false>
<#assign observableVarName = htmlID + "Observable">
<#if ts.declarations??>
<#list ts.declarations as declaration>
  <#if declaration.annotation??>@${declaration.annotation}()</#if> ${declaration.variableName}<#if declaration.annotation??> = new ${declaration.dataType}()<#else><#if declaration.dataType??>: ${declaration.dataType}</#if></#if><#if declaration.defaultValue?? && declaration.defaultValue != "">= [{prop: 'selected',name: '',sortable: false,canAutoResize: false,draggable: false,resizable: false,headerCheckboxable: true,checkboxable: true,width: 30},${declaration.defaultValue}</#if>; 	
  <#if declaration.variableName == "lpToggleEvent"><#assign lpToggleEvent = true></#if>
  <#if declaration.variableName == "lpPushDataEvent"><#assign lpPushDataEvent = true></#if>
  <#if declaration.variableName == htmlID><#assign formGroupVariable = true></#if>
  <#if declaration.variableName == observableVarName><#assign formGroupObs = true></#if>
  
</#list>
</#if>

<#if lpToggleEvent == false>	
	@Output() lpToggleEvent = new EventEmitter();
</#if>

<#if lpPushDataEvent == false>	
	@Output() lpPushDataEvent = new EventEmitter();
</#if>
<#if formGroupVariable == false>	
	${htmlID}: FormGroup; 	
</#if>
<#if formGroupObs == false>	
   	${htmlID}Observable: Observable<${htmlID?cap_first}Model[]>; 	
</#if>

<#assign fetchMethod = false>
<#assign onSubmit = false>
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
  <#if method.methodName == "fetch"><#assign fetchMethod = true></#if>
  <#if method.methodName == "onSubmit"><#assign onSubmit = true></#if>

</#list>
</#if>

<#if panelType == "leftPanel">
<#if onSubmit == false>	
  onSubmit(formData) {
  	this.fetch((data) => {this.${htmlID}Observable = of(data); 
		this.lpPushDataEvent.emit(this.${htmlID}Observable);
 	});
  }
</#if>

<#if !ts.methodNames['toggleSideNav']??>	
	toggleSideNav() {
		this.lpToggleEvent.emit('toggle');
	}
</#if>

<#if fetchMethod == false>
  fetch(cb) {
  		const req = new XMLHttpRequest();
    req.open('GET', 'assets/data/client.json');

    req.onload = () => {
      cb(JSON.parse(req.response));
    };

    req.send();
  }
</#if>
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
}
