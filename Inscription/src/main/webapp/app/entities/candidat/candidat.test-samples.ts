import dayjs from 'dayjs/esm';

import { ICandidat, NewCandidat } from './candidat.model';

export const sampleWithRequiredData: ICandidat = {
  id: 31945,
  nom: 'enfin après rédaction',
  prenom: 'avant que drelin de crainte que',
  sexe: 'M',
  nationalite: 'attarder crac magnifique',
  typePieceIdentite: 'PASSEPORT',
  email: 'Patrice_Renault90@gmail.com',
  dateNaiss: dayjs('2025-05-27T13:50'),
};

export const sampleWithPartialData: ICandidat = {
  id: 1096,
  nom: 'drelin pff chez',
  prenom: 'excuser quand',
  sexe: 'F',
  nationalite: 'miam',
  typePieceIdentite: 'PASSEPORT',
  email: 'Honorine.Leclercq@hotmail.fr',
  dateNaiss: dayjs('2025-05-27T20:14'),
};

export const sampleWithFullData: ICandidat = {
  id: 10472,
  nom: 'jusqu’à ce que partenaire',
  prenom: 'plic vivace',
  sexe: 'F',
  nationalite: 'si',
  typePieceIdentite: 'ACTENAISSANCE',
  email: 'Chloe_Robin97@hotmail.fr',
  dateNaiss: dayjs('2025-05-27T12:52'),
};

export const sampleWithNewData: NewCandidat = {
  nom: 'du côté de hi soudain',
  prenom: 'même sous couleur de antagoniste',
  sexe: 'F',
  nationalite: 'capter concurrence concernant',
  typePieceIdentite: 'PASSEPORT',
  email: 'Debora.Laine@yahoo.fr',
  dateNaiss: dayjs('2025-05-28T04:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
