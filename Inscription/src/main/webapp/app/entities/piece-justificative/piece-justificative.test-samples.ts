import dayjs from 'dayjs/esm';

import { IPieceJustificative, NewPieceJustificative } from './piece-justificative.model';

export const sampleWithRequiredData: IPieceJustificative = {
  id: 6792,
  type: 'ACTENAISSANCE',
  fichier: '../fake-data/blob/hipster.png',
  fichierContentType: 'unknown',
};

export const sampleWithPartialData: IPieceJustificative = {
  id: 27695,
  type: 'DIPLOME',
  fichier: '../fake-data/blob/hipster.png',
  fichierContentType: 'unknown',
  dateUpload: dayjs('2025-05-27T22:02'),
};

export const sampleWithFullData: IPieceJustificative = {
  id: 32091,
  type: 'CNI_RECTO',
  fichier: '../fake-data/blob/hipster.png',
  fichierContentType: 'unknown',
  dateUpload: dayjs('2025-05-27T15:48'),
  valide: true,
  commentaire: 'plutôt coller',
};

export const sampleWithNewData: NewPieceJustificative = {
  type: 'ACTENAISSANCE',
  fichier: '../fake-data/blob/hipster.png',
  fichierContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
