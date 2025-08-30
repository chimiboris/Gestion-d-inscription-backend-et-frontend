import { IContactUrgence, NewContactUrgence } from './contact-urgence.model';

export const sampleWithRequiredData: IContactUrgence = {
  id: 1846,
  nomComplet: 'diplomate',
  lienParente: 'secouriste',
  telephone: '0482135076',
};

export const sampleWithPartialData: IContactUrgence = {
  id: 12532,
  nomComplet: 'malade mairie neutre',
  lienParente: 'pschitt',
  telephone: '0480411898',
};

export const sampleWithFullData: IContactUrgence = {
  id: 1899,
  nomComplet: 'sale biathlète',
  lienParente: 'timide',
  telephone: '+33 781931104',
  email: 'Aminte97@gmail.com',
};

export const sampleWithNewData: NewContactUrgence = {
  nomComplet: 'aimable céans',
  lienParente: 'près de',
  telephone: '+33 472301723',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
