import { combineReducers } from 'redux';
import { reducer as reduxFormReducer } from 'redux-form';
import { i18nReducer } from 'react-redux-i18n';

<#if reactComponents??>
<#list reactComponents?keys as component>
<#if reactComponents[component].isTab??>
<#else>
import ${reactComponents[component].reducerName?uncap_first} from './${reactComponents[component].reducerFileName?keep_after("/")}';
</#if>
</#list>
</#if>	

const rootReducer = combineReducers({
<#if reactComponents??>
<#list reactComponents?keys as component>
<#if reactComponents[component].isTab??>
<#else>
	${reactComponents[component].reducerName?uncap_first},
</#if>
</#list>
</#if>  
	form: reduxFormReducer,
	i18n: i18nReducer
});

export default rootReducer;