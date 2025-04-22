import dayjs from 'dayjs';
import { MembershipStatus } from 'app/shared/model/enumerations/membership-status.model';

export interface ICustomer {
  id?: string;
  firstName?: string;
  lastName?: string;
  dateOfBirth?: dayjs.Dayjs | null;
  phoneNumber?: string;
  addressLine1?: string;
  addressLine2?: string | null;
  postalCode?: string;
  city?: string;
  state?: string | null;
  country?: string;
  preferences?: string | null;
  loyaltyPoints?: number | null;
  membershipStatus?: keyof typeof MembershipStatus | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ICustomer> = {};
