import dayjs from 'dayjs/esm';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { IUser } from 'app/entities/user/user.model';
import { StatutDossier } from 'app/entities/enumerations/statut-dossier.model';

export interface IDossier {
  id: number;
  statut?: keyof typeof StatutDossier | null;
  dateSoumission?: dayjs.Dayjs | null;
  commentaire?: string | null;
  candidat?: Pick<ICandidat, 'id'> | null;
  agent?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewDossier = Omit<IDossier, 'id'> & { id: null };
