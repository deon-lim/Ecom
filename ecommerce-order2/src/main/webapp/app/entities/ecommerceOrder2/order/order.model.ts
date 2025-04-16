import dayjs from 'dayjs/esm';

export interface IOrder {
  id: string;
  customerId?: string | null;
  totalAmount?: number | null;
  createdOn?: dayjs.Dayjs | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
