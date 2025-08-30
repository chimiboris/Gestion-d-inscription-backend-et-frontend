import dayjs from 'dayjs/esm';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { Type } from 'app/entities/enumerations/type.model';

export interface IPieceJustificative {
  id: number;
  type?: keyof typeof Type | null;
  fichier?: string | null;
  fichierContentType?: string | null;
  dateUpload?: dayjs.Dayjs | null;
  valide?: boolean | null;
  commentaire?: string | null;
  candidat?: Pick<ICandidat, 'id'> | null;
}

export type NewPieceJustificative = Omit<IPieceJustificative, 'id'> & { id: null };
