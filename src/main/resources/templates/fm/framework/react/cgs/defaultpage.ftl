import React from 'react';
import { Navbar } from 'react-bootstrap';

import Header from '../component/header'
import LeftNavBar from '../component/leftNavBar';
import LeftNavSearch from '../component/leftNavSearch';
import LanguageSelector from '../component/LanguageSelector';

class DefaultPage extends React.Component {
  render() {
      return(
          <nav role="navigation">
              <Header />
              <Navbar.Collapse>
                  <LanguageSelector />
              </Navbar.Collapse>
               <Navbar>
              <LeftNavBar />
              </Navbar>

          </nav>
      );
  }
}

export default DefaultPage;
