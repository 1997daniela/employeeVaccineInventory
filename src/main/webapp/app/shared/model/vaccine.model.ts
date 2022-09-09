import dayjs from 'dayjs';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { VaccineType } from 'app/shared/model/enumerations/vaccine-type.model';

export interface IVaccine {
  id?: number;
  vaccineType?: VaccineType;
  vaccinationDate?: string;
  doses?: number;
  applicationUser?: IApplicationUser;
}

export const defaultValue: Readonly<IVaccine> = {};
