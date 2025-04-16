import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'order',
    data: { pageTitle: 'Orders' },
    loadChildren: () => import('./ecommerceOrder2/order/order.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
