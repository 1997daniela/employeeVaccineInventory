import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IVaccine } from 'app/shared/model/vaccine.model';

export interface IApplicationUser {
  id?: number;
  identification?: string;
  birthday?: string | null;
  address?: string | null;
  cellphone?: string | null;
  internalUser?: IUser;
  vaccines?: IVaccine[] | null;
}

export const defaultValue: Readonly<IApplicationUser> = {};
