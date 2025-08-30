import dayjs from 'dayjs/esm';

import { IParcours, NewParcours } from './parcours.model';

export const sampleWithRequiredData: IParcours = {
  id: 30489,
  etablissement: 'lorsque',
  specialisation: 'groin groin tsoin-tsoin snif',
  niveau: 'AUTRE',
  dateDebut: dayjs('2025-05-27T12:31'),
  dateFin: dayjs('2025-05-28T03:20'),
};

export const sampleWithPartialData: IParcours = {
  id: 19626,
  etablissement: 'vu que corps enseignant',
  specialisation: 'hors fournir adepte',
  niveau: 'AUTRE',
  dateDebut: dayjs('2025-05-27T14:40'),
  dateFin: dayjs('2025-05-27T23:13'),
};

export const sampleWithFullData: IParcours = {
  id: 472,
  etablissement: 'guide briller piquer',
  specialisation: 'en dehors de suivant jusqu’à ce que',
  niveau: 'BACCALAUREAT',
  commentaire: 'atchoum habiter euh',
  dateDebut: dayjs('2025-05-27T17:17'),
  dateFin: dayjs('2025-05-28T00:40'),
};

export const sampleWithNewData: NewParcours = {
  etablissement: 'extra mélancolique',
  specialisation: 'parfois',
  niveau: 'AUTRE',
  dateDebut: dayjs('2025-05-27T20:24'),
  dateFin: dayjs('2025-05-27T17:07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
