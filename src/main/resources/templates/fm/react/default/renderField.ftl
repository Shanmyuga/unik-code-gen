import React from 'react';

export const renderField = props => {
  if (props.type == "dropdown") {
  	return (
  		<div className={(props.meta.touched && props.meta.error)?"form-group has-error has-feedback":"form-group"}>
  			<label htmlFor="{props.name}" className="control-label">{props.label}</label>
  			<div>
			    <select {...props.input} className="form-control" {...props}>
			      <option value="">Select ...</option>
			      {props.children}
			    </select>
			    {props.meta.touched && props.meta.error && <div className="help-block has-error">{props.meta.error}</div>}
		    </div>
		</div>
  	);
  } else if (props.type == "radio" || props.type == "checkbox") {
  	return (
  		<div className={(props.meta.touched && props.meta.error)?"form-group has-error has-feedback":"form-group"}>
  			<label htmlFor="{props.name}" className="control-label">{props.label}</label>
  			<div>
  				{props.children}
			    {props.meta.touched && props.meta.error && <div className="help-block has-error">{props.meta.error}</div>}
		    </div>
		</div>
  	);
  } else if (props.type == "submit" || props.type == "button") {
  	var propsClass = "btn " + props.className;
  	return (
  		<div className="form-group">
			<button name={props.input.name} type={props.type} className={propsClass}>{props.label}</button>
		</div>
  	);	
  } else {
	  return (
		<div className={(props.meta.touched && props.meta.error)?"form-group has-error has-feedback":"form-group"}>
			<label htmlFor="{props.name}" className="control-label">{props.label}</label>
			<div>
			  <input {...props.input} placeholder={props.placeholder} type={props.type} className="form-control" {...props} />
			  {props.meta.touched &&
				((props.meta.error && <div className="help-block has-error">{props.meta.error}</div>) ||
				  (props.meta.warning && <span>{props.meta.warning}</span>))}
			</div>
		</div>
	  );
  }
};