import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 'ca41e08f-fbee-4803-8f4f-67340147c97a',
};

export const sampleWithPartialData: IOrder = {
  id: '4a73ae75-cfc7-4763-87bb-db55b537aee5',
  product_quantity: 18193,
};

export const sampleWithFullData: IOrder = {
  id: 'd7316eea-1023-4f9d-812e-54cfb50e8167',
  dateTime: dayjs('2025-04-12T18:20'),
  product_name: 'fund helplessly',
  product_quantity: 10498,
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
