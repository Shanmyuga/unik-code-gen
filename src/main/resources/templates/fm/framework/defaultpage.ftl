import React from 'react';
import { Navbar } from 'react-bootstrap';

import Header from '../component/header'
import LeftNavBar from '../component/leftNavBar';
import LeftNavSearch from '../component/leftNavSearch';
import LanguageSelector from '../component/LanguageSelector';

class DefaultPage extends React.Component {
  render() {
      if (this.props.authenticated == 'true') {
		  return(
			  <nav className="navbar navbar-default navbar-static-top" role="navigation">
				  <Header />
				  <Navbar.Collapse>
					  <LanguageSelector />
				  </Navbar.Collapse>

				  <div className="navbar-default sidebar" role="navigation">
					  <div className="sidebar-nav navbar-collapse">
						  <LeftNavBar />
					  </div>
				  </div>
			  </nav>
		  );
	  } else {
		  return (<div></div>);
	  }
  }
}

export default DefaultPage;
