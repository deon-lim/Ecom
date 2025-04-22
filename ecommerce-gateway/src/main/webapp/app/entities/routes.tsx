import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import { Route } from 'react-router-dom';

import Product from './ecommerceProduct/product';
import entitiesReducers from './reducers';

import Customer from './customer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('ecommercegateway', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="product/*" element={<Product />} />
        <Route path="customer/*" element={<Customer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
