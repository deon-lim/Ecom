import axios from 'axios';
import { createAsyncThunk } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

export interface GatewayRoute {
  id: string;
  predicates: any;
  filters: any;
  uri: string;
}

export interface GatewayState {
  loading: boolean;
  routes: GatewayRoute[];
}

// thunk to load the routes from the management endpoint
export const getGatewayRoutes = createAsyncThunk(
  'administration/fetch_gateway_routes',
  async () => axios.get<GatewayRoute[]>('management/gateway'),
  {
    serializeError: serializeAxiosError,
  },
);
