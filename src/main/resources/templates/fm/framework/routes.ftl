import React, { Component, PropTypes } from 'react';
import {render} from 'react-dom';
import {BrowserRouter as Router, Route, IndexRoute, Switch, Link} from 'react-router-dom';
import { createBrowserHistory } from 'history';

<#assign defaultPage = false>
<#list routerComponents as router>
import ${router.containerName} from '${router.containerFileName}';
<#if router.containerName == "DefaultPage"><#assign defaultPage = true></#if>
</#list>

<#if defaultPage == false>
import DefaultPage from './container/defaultPage';
</#if>

class AppRouter extends Component {
  render() {
    const history = this.props.history;
    let authenticated = sessionStorage.getItem('authenticated');

    return (
      <div>
		  <DefaultPage authenticated = { authenticated } />
          <Switch>
              <#list routerComponents as router>
              
              <#if router.containerRoute == "" && router.containerName != "DefaultPage">
              <Route exact path='/${router.containerRoute}' component={${router.containerName}} name='${router.sectionName}' />
              </#if>
              
              <#if router.containerRoute != "">
              <Route path='/${router.containerRoute}' component={${router.containerName}} name='${router.sectionName}' />
              </#if>
              
              </#list>
          </Switch>
      </div>
    );
  }
}

export default AppRouter;
