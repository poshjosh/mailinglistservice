import dayjs from 'dayjs';
import { MailingListUserStatus } from 'app/shared/model/enumerations/mailing-list-user-status.model';

export interface IMailingListUser {
  id?: number;
  username?: string | null;
  emailAddress?: string | null;
  lastName?: string | null;
  firstName?: string | null;
  status?: MailingListUserStatus;
  timeCreated?: string;
  timeModified?: string;
}

export const defaultValue: Readonly<IMailingListUser> = {};
