import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IParcours, NewParcours } from '../parcours.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParcours for edit and NewParcoursFormGroupInput for create.
 */
type ParcoursFormGroupInput = IParcours | PartialWithRequiredKeyOf<NewParcours>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IParcours | NewParcours> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

type ParcoursFormRawValue = FormValueOf<IParcours>;

type NewParcoursFormRawValue = FormValueOf<NewParcours>;

type ParcoursFormDefaults = Pick<NewParcours, 'id' | 'dateDebut' | 'dateFin'>;

type ParcoursFormGroupContent = {
  id: FormControl<ParcoursFormRawValue['id'] | NewParcours['id']>;
  etablissement: FormControl<ParcoursFormRawValue['etablissement']>;
  specialisation: FormControl<ParcoursFormRawValue['specialisation']>;
  niveau: FormControl<ParcoursFormRawValue['niveau']>;
  commentaire: FormControl<ParcoursFormRawValue['commentaire']>;
  dateDebut: FormControl<ParcoursFormRawValue['dateDebut']>;
  dateFin: FormControl<ParcoursFormRawValue['dateFin']>;
  candidat: FormControl<ParcoursFormRawValue['candidat']>;
};

export type ParcoursFormGroup = FormGroup<ParcoursFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParcoursFormService {
  createParcoursFormGroup(parcours: ParcoursFormGroupInput = { id: null }): ParcoursFormGroup {
    const parcoursRawValue = this.convertParcoursToParcoursRawValue({
      ...this.getFormDefaults(),
      ...parcours,
    });
    return new FormGroup<ParcoursFormGroupContent>({
      id: new FormControl(
        { value: parcoursRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      etablissement: new FormControl(parcoursRawValue.etablissement, {
        validators: [Validators.required],
      }),
      specialisation: new FormControl(parcoursRawValue.specialisation, {
        validators: [Validators.required],
      }),
      niveau: new FormControl(parcoursRawValue.niveau, {
        validators: [Validators.required],
      }),
      commentaire: new FormControl(parcoursRawValue.commentaire),
      dateDebut: new FormControl(parcoursRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(parcoursRawValue.dateFin, {
        validators: [Validators.required],
      }),
      candidat: new FormControl(parcoursRawValue.candidat, {
        validators: [Validators.required],
      }),
    });
  }

  getParcours(form: ParcoursFormGroup): IParcours | NewParcours {
    return this.convertParcoursRawValueToParcours(form.getRawValue() as ParcoursFormRawValue | NewParcoursFormRawValue);
  }

  resetForm(form: ParcoursFormGroup, parcours: ParcoursFormGroupInput): void {
    const parcoursRawValue = this.convertParcoursToParcoursRawValue({ ...this.getFormDefaults(), ...parcours });
    form.reset(
      {
        ...parcoursRawValue,
        id: { value: parcoursRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParcoursFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
    };
  }

  private convertParcoursRawValueToParcours(rawParcours: ParcoursFormRawValue | NewParcoursFormRawValue): IParcours | NewParcours {
    return {
      ...rawParcours,
      dateDebut: dayjs(rawParcours.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawParcours.dateFin, DATE_TIME_FORMAT),
    };
  }

  private convertParcoursToParcoursRawValue(
    parcours: IParcours | (Partial<NewParcours> & ParcoursFormDefaults),
  ): ParcoursFormRawValue | PartialWithRequiredKeyOf<NewParcoursFormRawValue> {
    return {
      ...parcours,
      dateDebut: parcours.dateDebut ? parcours.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: parcours.dateFin ? parcours.dateFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
