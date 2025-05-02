import React from 'react';
import { Translate } from 'react-jhipster';
import Orders from './ecommerceOrder2/order/order';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.ecommerceProductProduct" />
      </MenuItem>
      {/* <MenuItem icon="asterisk" to="/customer">
        <Translate contentKey="global.menu.entities.customer" />
      </MenuItem> */}
      <MenuItem icon="asterisk" to="/orders">
        <Translate contentKey="global.menu.entities.orders" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
