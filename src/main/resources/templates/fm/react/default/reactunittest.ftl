import React from 'react';
import { shallow, mount } from 'enzyme';
import configureMockStore from "redux-mock-store";

import ${component.containerName} from '${component.containerRelativePath}';
import ${component.reducerName?uncap_first} from '${component.reducerRelativePath}';

const mockStore = configureMockStore();
const store = mockStore({ ${component.reducerName?uncap_first}: {
    data: 'test'
  }
});

describe('${component.componentName}', () => {
  it('should render login correctly', () => {
    const component = shallow(<${component.containerName} />, { context: { store } });

    expect(component).toBeDefined();
  });

  <#if component.variables??>
  it('should render field elements', () => {
    const component = mount(<${component.containerName} />, { context: { store } });

    <#list component.variables as variable>
    	<#if variable.htmlTag?? && variable.htmlTag == 'input'>expect(component.find('${variable.htmlTag}[name="${variable.key}"]').length).toEqual(1);</#if>
    	<#if variable.htmlTag?? && variable.htmlTag == 'button'>expect(component.find('${variable.htmlTag}[name="${variable.key}"]').length).toEqual(1);</#if>
    	<#if variable.htmlTag?? && variable.htmlTag == 'select'>expect(component.find('${variable.htmlTag}').length).toEqual(1);</#if>
	</#list>
  });
  </#if>
});
