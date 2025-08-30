import dayjs from 'dayjs/esm';

import { IDossier, NewDossier } from './dossier.model';

export const sampleWithRequiredData: IDossier = {
  id: 5161,
  statut: 'EN_COURS_VERIF',
  dateSoumission: dayjs('2025-05-28T05:25'),
};

export const sampleWithPartialData: IDossier = {
  id: 10367,
  statut: 'INCOMPLET',
  dateSoumission: dayjs('2025-05-28T00:54'),
  commentaire: 'ha',
};

export const sampleWithFullData: IDossier = {
  id: 5565,
  statut: 'REFUSE',
  dateSoumission: dayjs('2025-05-28T01:34'),
  commentaire: 'assez comme',
};

export const sampleWithNewData: NewDossier = {
  statut: 'EN_ATTENTE',
  dateSoumission: dayjs('2025-05-27T17:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
