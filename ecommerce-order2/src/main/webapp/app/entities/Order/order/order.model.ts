import dayjs from 'dayjs/esm';

export interface IOrder {
  id: string;
  dateTime?: dayjs.Dayjs | null;
  product_name?: string | null;
  product_quantity?: number | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
