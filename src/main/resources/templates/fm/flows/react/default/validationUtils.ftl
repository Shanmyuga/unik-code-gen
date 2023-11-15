import Joi from 'joi-browser';

export const joiOptions = {
	language: {
		key: '{{!key}}'
	},
	abortEarly: false,
	allowUnknown: true
};

export const validator = (schema, joiOption = joiOptions) => (values) => {
	const errors = {};
	
	Joi.validate(values, schema, joiOption, (err) => {
		console.log('validationg...');
		if (err) {
			for (const error of err.details) {
				console.log(JSON.stringify(error));
				errors[error.path] = error.message.replace(error.context.key, error.context.label);
			}
		}
	});
	
	console.log ('errors=' + JSON.stringify(errors));
	return errors;
}

<#if reactComponents??>

<#list reactComponents?keys as pageName>
	<#if pageName != "Login" && reactComponents[pageName].validationVariables??>
		<#assign schemaName = reactComponents[pageName].componentName?uncap_first>
		export const ${schemaName}Schema = Joi.object().keys({
		<#list reactComponents[pageName].validationVariables as stateVar>
			${stateVar.key}: Joi.string()<#if stateVar.minLength??>.min(${stateVar.minLength})</#if>
							 <#if stateVar.maxLength??>.max(${stateVar.maxLength})</#if>
							 <#if stateVar.mandatory?? && stateVar.mandatory == "true">.required()</#if>
							 <#if stateVar.email?? && stateVar.email == "true">.email()</#if>
							 <#if stateVar.pattern??>.regex(/${stateVar.pattern}/)</#if>.label('${stateVar.label}')
							 .options({
								 language: {
									 any: {
										 required: ' is Mandatory and Required.'
									 }, 
									 string: {
									 	truncate: 'truncate',
									 	min: ' length of minimum <#if stateVar.minLength??>${stateVar.minLength}<#else>0</#if> required.',
									 	max: ' length of maximum <#if stateVar.maxLength??>${stateVar.maxLength}<#else>0</#if> required.',
									 	email: ' is not a valid email format',
									 	regex: {
									 		base: ' is invalid'
									 	}
									 }
								 }
							 })<#if stateVar?has_next>,</#if>
		</#list>	
		});
	</#if>
</#list>

</#if>