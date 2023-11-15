import {createStore, applyMiddleware } from 'redux';
import { createLogger } from 'redux-logger';
import promiseMiddleware from 'redux-promise-middleware';
import thunk from 'redux-thunk';
import rootReducer from '../reducer/rootReducer';

const createStoreWithMiddleware = applyMiddleware (
  promiseMiddleware(),
  thunk,
  createLogger()
)(createStore);

export default function configureStore(initialState) {
    const store = createStoreWithMiddleware(rootReducer, initialState);

    if (module.hot) {
      module.hot.accept ('../reducer/rootReducer', () => {
          const nextRootReducer = require('../reducer/rootReducer');

          store.replaceReducer(nextRootReducer);
      });
    }

    return store;
}