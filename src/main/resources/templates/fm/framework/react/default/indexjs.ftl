import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { Router } from 'react-router';
import { BrowserRouter } from 'react-router-dom';
import { loadTranslations, setLocale, syncTranslationWithStore } from 'react-redux-i18n';

import translationsObject from './asset/props/translations';

import configureStore from './${configureStore}';
import AppRouter from './${routeFileName}';

const store = configureStore();
syncTranslationWithStore(store)
store.dispatch(loadTranslations(translationsObject));
store.dispatch(setLocale('en'));

function Root() {
  return (<Provider store = { store }>
              <BrowserRouter>
                  <AppRouter />
              </BrowserRouter>
          </Provider>);
}

render(<Root />, document.getElementById("wrapper"));