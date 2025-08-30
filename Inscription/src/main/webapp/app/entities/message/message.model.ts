import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IMessage {
  id: number;
  contenu?: string | null;
  dateEnvoie?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
