import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICandidat, NewCandidat } from '../candidat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICandidat for edit and NewCandidatFormGroupInput for create.
 */
type CandidatFormGroupInput = ICandidat | PartialWithRequiredKeyOf<NewCandidat>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICandidat | NewCandidat> = Omit<T, 'dateNaiss'> & {
  dateNaiss?: string | null;
};

type CandidatFormRawValue = FormValueOf<ICandidat>;

type NewCandidatFormRawValue = FormValueOf<NewCandidat>;

type CandidatFormDefaults = Pick<NewCandidat, 'id' | 'dateNaiss'>;

type CandidatFormGroupContent = {
  id: FormControl<CandidatFormRawValue['id'] | NewCandidat['id']>;
  nom: FormControl<CandidatFormRawValue['nom']>;
  prenom: FormControl<CandidatFormRawValue['prenom']>;
  sexe: FormControl<CandidatFormRawValue['sexe']>;
  nationalite: FormControl<CandidatFormRawValue['nationalite']>;
  typePieceIdentite: FormControl<CandidatFormRawValue['typePieceIdentite']>;
  email: FormControl<CandidatFormRawValue['email']>;
  dateNaiss: FormControl<CandidatFormRawValue['dateNaiss']>;
  user: FormControl<CandidatFormRawValue['user']>;
};

export type CandidatFormGroup = FormGroup<CandidatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CandidatFormService {
  createCandidatFormGroup(candidat: CandidatFormGroupInput = { id: null }): CandidatFormGroup {
    const candidatRawValue = this.convertCandidatToCandidatRawValue({
      ...this.getFormDefaults(),
      ...candidat,
    });
    return new FormGroup<CandidatFormGroupContent>({
      id: new FormControl(
        { value: candidatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(candidatRawValue.nom, {
        validators: [Validators.required],
      }),
      prenom: new FormControl(candidatRawValue.prenom, {
        validators: [Validators.required],
      }),
      sexe: new FormControl(candidatRawValue.sexe, {
        validators: [Validators.required],
      }),
      nationalite: new FormControl(candidatRawValue.nationalite, {
        validators: [Validators.required],
      }),
      typePieceIdentite: new FormControl(candidatRawValue.typePieceIdentite, {
        validators: [Validators.required],
      }),
      email: new FormControl(candidatRawValue.email, {
        validators: [Validators.required],
      }),
      dateNaiss: new FormControl(candidatRawValue.dateNaiss, {
        validators: [Validators.required],
      }),
      user: new FormControl(candidatRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getCandidat(form: CandidatFormGroup): ICandidat | NewCandidat {
    return this.convertCandidatRawValueToCandidat(form.getRawValue() as CandidatFormRawValue | NewCandidatFormRawValue);
  }

  resetForm(form: CandidatFormGroup, candidat: CandidatFormGroupInput): void {
    const candidatRawValue = this.convertCandidatToCandidatRawValue({ ...this.getFormDefaults(), ...candidat });
    form.reset(
      {
        ...candidatRawValue,
        id: { value: candidatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CandidatFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateNaiss: currentTime,
    };
  }

  private convertCandidatRawValueToCandidat(rawCandidat: CandidatFormRawValue | NewCandidatFormRawValue): ICandidat | NewCandidat {
    return {
      ...rawCandidat,
      dateNaiss: dayjs(rawCandidat.dateNaiss, DATE_TIME_FORMAT),
    };
  }

  private convertCandidatToCandidatRawValue(
    candidat: ICandidat | (Partial<NewCandidat> & CandidatFormDefaults),
  ): CandidatFormRawValue | PartialWithRequiredKeyOf<NewCandidatFormRawValue> {
    return {
      ...candidat,
      dateNaiss: candidat.dateNaiss ? candidat.dateNaiss.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
