export interface IProduct {
  id?: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  stock?: number | null;
}

export const defaultValue: Readonly<IProduct> = {};
