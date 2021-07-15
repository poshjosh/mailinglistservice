import dayjs from 'dayjs';
import { IMailingListUser } from 'app/shared/model/mailing-list-user.model';
import { IMailingList } from 'app/shared/model/mailing-list.model';
import { MailingListUserStatus } from 'app/shared/model/enumerations/mailing-list-user-status.model';

export interface IUserMailingList {
  id?: number;
  status?: MailingListUserStatus;
  timeCreated?: string;
  timeModified?: string;
  user?: IMailingListUser;
  mailingList?: IMailingList;
}

export const defaultValue: Readonly<IUserMailingList> = {};
