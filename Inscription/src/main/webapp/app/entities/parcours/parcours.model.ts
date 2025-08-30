import dayjs from 'dayjs/esm';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

export interface IParcours {
  id: number;
  etablissement?: string | null;
  specialisation?: string | null;
  niveau?: keyof typeof NiveauEtude | null;
  commentaire?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  candidat?: Pick<ICandidat, 'id'> | null;
}

export type NewParcours = Omit<IParcours, 'id'> & { id: null };
