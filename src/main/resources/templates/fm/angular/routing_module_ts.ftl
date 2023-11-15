<#list ts.imports as imp>
import { <#list imp.importObjects as importObj>${importObj}<#if importObj?has_next>, </#if></#list> } from '${imp.packageNameFull}';
</#list>

<#if ts.routes??>
const routes: Routes = [
<#list ts.routes as route>
{path: ${route.path}, component: ${route.component}}<#if route?has_next>,</#if>
</#list>
];
</#if>

@NgModule({
  <#if ts.ngModuleExports??>
  exports: [
  	<#list ts.ngModuleExports as export>
     ${export}<#if export?has_next>,</#if>
	</#list>
  ],
  </#if>

  <#if ts.ngModuleImports??>
  imports: [
	<#list ts.ngModuleImports as import>
      ${import}<#if import?has_next>,</#if>
	</#list>
  ],
  </#if>
  <#if ts.ngModuleSchemas??>
  schemas: [
  	<#list ts.ngModuleSchemas as schema>
		${schema}<#if schema?has_next>,</#if>
	</#list>
  ],
  </#if>
  <#if ts.isApp == "true">
  bootstrap: [AppComponent],
  </#if>
  <#if ts.ngModuleDeclarations??>
  declarations: [
  	<#list ts.ngModuleDeclarations as declaration>
		${declaration}<#if declaration?has_next>,</#if>
	</#list>
  ]
  </#if>
})
export class ${ts.className} { }
