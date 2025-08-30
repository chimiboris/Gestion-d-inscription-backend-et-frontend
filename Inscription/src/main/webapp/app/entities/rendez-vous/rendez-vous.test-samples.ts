import dayjs from 'dayjs/esm';

import { IRendezVous, NewRendezVous } from './rendez-vous.model';

export const sampleWithRequiredData: IRendezVous = {
  id: 28286,
  dateRdv: dayjs('2025-06-18T08:02'),
  motif: 'coupable moderne',
};

export const sampleWithPartialData: IRendezVous = {
  id: 25807,
  dateRdv: dayjs('2025-06-17T17:02'),
  motif: 'admirablement jusque coac coac',
};

export const sampleWithFullData: IRendezVous = {
  id: 18651,
  dateRdv: dayjs('2025-06-18T10:20'),
  motif: 'de par membre du personnel',
  statut: 'EN_ATTENTE',
  commentaire: 'sincère d’autant que',
};

export const sampleWithNewData: NewRendezVous = {
  dateRdv: dayjs('2025-06-18T06:03'),
  motif: 'sus hésiter',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
