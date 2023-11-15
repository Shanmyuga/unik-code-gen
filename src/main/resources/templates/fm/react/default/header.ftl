<#if propKey??>
<#else>
	<br />
</#if>
<#if htmlTag??>
<#if label??><${htmlTag}<#if labelCss??> class="${labelCss}"</#if>>${label}:</${htmlTag}></#if><#if dataPosition??><#if dataPosition == "bottom"><br/></#if></#if><#if propKey??><${htmlTag}<#if valueCss??> class="${valueCss}"</#if>>${propKey}</${htmlTag}></#if>
<#else>
<#if label??><span<#if labelCss??> class="${labelCss}"</#if>>${label}:</span></#if><#if dataPosition??><#if dataPosition == "bottom"><br/></#if></#if><#if propKey??><span<#if valueCss??> class="${valueCss}"</#if>>${propKey}</span></#if>
</#if>
<#if propKey??>
<#else>
	<hr style="word-spacing: 0; margin-top: 0px; margin-bottom: 5px; border-top-color: black !important; height: 2px">
</#if>
