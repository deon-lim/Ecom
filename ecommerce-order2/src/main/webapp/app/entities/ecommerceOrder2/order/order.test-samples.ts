import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 'ca41e08f-fbee-4803-8f4f-67340147c97a',
};

export const sampleWithPartialData: IOrder = {
  id: '4a73ae75-cfc7-4763-87bb-db55b537aee5',
  createdOn: dayjs('2025-04-16T02:16'),
};

export const sampleWithFullData: IOrder = {
  id: 'd7316eea-1023-4f9d-812e-54cfb50e8167',
  customerId: 'duh',
  totalAmount: 12034.51,
  createdOn: dayjs('2025-04-15T21:42'),
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
