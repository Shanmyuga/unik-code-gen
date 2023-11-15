import React from 'react';
import { Navbar } from 'react-bootstrap';

import Header from '../component/header'
import LeftNavBar from '../component/leftNavBar';
import LeftNavSearch from '../component/leftNavSearch';

class DefaultPage extends React.Component {
  render() {
      return(
      	<div>
	      	<LeftNavBar />
	     
	        <section className="wrapper scrollable">
		      	<Header />
	      	</section>
      	</div>
      );
  }
}

export default DefaultPage;
