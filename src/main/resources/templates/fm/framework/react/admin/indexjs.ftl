import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { Router } from 'react-router';
import { BrowserRouter } from 'react-router-dom';

import configureStore from './${configureStore}';
import AppRouter from './${routeFileName}';

const store = configureStore();

function Root() {
  return (<Provider store = { store }>
              <BrowserRouter>
                  <AppRouter />
              </BrowserRouter>
          </Provider>);
}

render(<Root />, document.getElementById("rootWrapper"));