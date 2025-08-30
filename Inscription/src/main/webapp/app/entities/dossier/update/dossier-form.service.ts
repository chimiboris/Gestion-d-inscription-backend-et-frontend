import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDossier, NewDossier } from '../dossier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDossier for edit and NewDossierFormGroupInput for create.
 */
type DossierFormGroupInput = IDossier | PartialWithRequiredKeyOf<NewDossier>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDossier | NewDossier> = Omit<T, 'dateSoumission'> & {
  dateSoumission?: string | null;
};

type DossierFormRawValue = FormValueOf<IDossier>;

type NewDossierFormRawValue = FormValueOf<NewDossier>;

type DossierFormDefaults = Pick<NewDossier, 'id' | 'dateSoumission'>;

type DossierFormGroupContent = {
  id: FormControl<DossierFormRawValue['id'] | NewDossier['id']>;
  statut: FormControl<DossierFormRawValue['statut']>;
  dateSoumission: FormControl<DossierFormRawValue['dateSoumission']>;
  commentaire: FormControl<DossierFormRawValue['commentaire']>;
  candidat: FormControl<DossierFormRawValue['candidat']>;
  agent: FormControl<DossierFormRawValue['agent']>;
};

export type DossierFormGroup = FormGroup<DossierFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DossierFormService {
  createDossierFormGroup(dossier: DossierFormGroupInput = { id: null }): DossierFormGroup {
    const dossierRawValue = this.convertDossierToDossierRawValue({
      ...this.getFormDefaults(),
      ...dossier,
    });
    return new FormGroup<DossierFormGroupContent>({
      id: new FormControl(
        { value: dossierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      statut: new FormControl(dossierRawValue.statut, {
        validators: [Validators.required],
      }),
      dateSoumission: new FormControl(dossierRawValue.dateSoumission, {
        validators: [Validators.required],
      }),
      commentaire: new FormControl(dossierRawValue.commentaire),
      candidat: new FormControl(dossierRawValue.candidat, {
        validators: [Validators.required],
      }),
      agent: new FormControl(dossierRawValue.agent),
    });
  }

  getDossier(form: DossierFormGroup): IDossier | NewDossier {
    return this.convertDossierRawValueToDossier(form.getRawValue() as DossierFormRawValue | NewDossierFormRawValue);
  }

  resetForm(form: DossierFormGroup, dossier: DossierFormGroupInput): void {
    const dossierRawValue = this.convertDossierToDossierRawValue({ ...this.getFormDefaults(), ...dossier });
    form.reset(
      {
        ...dossierRawValue,
        id: { value: dossierRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DossierFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateSoumission: currentTime,
    };
  }

  private convertDossierRawValueToDossier(rawDossier: DossierFormRawValue | NewDossierFormRawValue): IDossier | NewDossier {
    return {
      ...rawDossier,
      dateSoumission: dayjs(rawDossier.dateSoumission, DATE_TIME_FORMAT),
    };
  }

  private convertDossierToDossierRawValue(
    dossier: IDossier | (Partial<NewDossier> & DossierFormDefaults),
  ): DossierFormRawValue | PartialWithRequiredKeyOf<NewDossierFormRawValue> {
    return {
      ...dossier,
      dateSoumission: dossier.dateSoumission ? dossier.dateSoumission.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
