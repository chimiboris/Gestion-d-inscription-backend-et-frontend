import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 31754,
  contenu: 'snif',
  dateEnvoie: dayjs('2025-06-17T16:40'),
};

export const sampleWithPartialData: IMessage = {
  id: 22376,
  contenu: "à l'entour de joliment tendre",
  dateEnvoie: dayjs('2025-06-17T16:58'),
};

export const sampleWithFullData: IMessage = {
  id: 12568,
  contenu: 'ding répondre afin que',
  dateEnvoie: dayjs('2025-06-18T09:17'),
};

export const sampleWithNewData: NewMessage = {
  contenu: 'trop personnel professionnel',
  dateEnvoie: dayjs('2025-06-18T12:42'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
