import { ICandidat } from 'app/entities/candidat/candidat.model';

export interface IContactUrgence {
  id: number;
  nomComplet?: string | null;
  lienParente?: string | null;
  telephone?: string | null;
  email?: string | null;
  candidat?: Pick<ICandidat, 'id'> | null;
}

export type NewContactUrgence = Omit<IContactUrgence, 'id'> & { id: null };
