import dayjs from 'dayjs/esm';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { statutRdv } from 'app/entities/enumerations/statut-rdv.model';

export interface IRendezVous {
  id: number;
  dateRdv?: dayjs.Dayjs | null;
  motif?: string | null;
  statut?: keyof typeof statutRdv | null;
  commentaire?: string | null;
  candidat?: Pick<ICandidat, 'id'> | null;
}

export type NewRendezVous = Omit<IRendezVous, 'id'> & { id: null };
