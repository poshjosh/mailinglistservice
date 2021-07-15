import dayjs from 'dayjs';

export interface IMailingList {
  id?: number;
  name?: string;
  description?: string | null;
  timeCreated?: string;
  timeModified?: string;
}

export const defaultValue: Readonly<IMailingList> = {};
