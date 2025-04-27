import { IUser } from 'app/shared/model/user.model';

export interface ICustomer {
  id?: string;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
  city?: string | null;
  userId?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ICustomer> = {};
