import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IPieceJustificative } from 'app/entities/piece-justificative/piece-justificative.model';
import { IParcours } from 'app/entities/parcours/parcours.model';
import { IContactUrgence } from 'app/entities/contact-urgence/contact-urgence.model';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { IRendezVous } from 'app/entities/rendez-vous/rendez-vous.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { TypePieceIdentite } from 'app/entities/enumerations/type-piece-identite.model';

export interface ICandidat {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  sexe?: keyof typeof Sexe | null;
  nationalite?: string | null;
  typePieceIdentite?: keyof typeof TypePieceIdentite | null;
  email?: string | null;
  dateNaiss?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  piecejustificatives?: Pick<IPieceJustificative, 'id'>[] | null;
  parcours?: Pick<IParcours, 'id'>[] | null;
  contacturgence?: Pick<IContactUrgence, 'id'> | null;
  n?: Pick<IDossier, 'id'> | null;
  rendezVous?: Pick<IRendezVous, 'id'>[] | null;
}

export type NewCandidat = Omit<ICandidat, 'id'> & { id: null };
