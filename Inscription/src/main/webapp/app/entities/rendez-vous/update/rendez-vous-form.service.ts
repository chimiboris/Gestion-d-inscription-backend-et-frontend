import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRendezVous, NewRendezVous } from '../rendez-vous.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRendezVous for edit and NewRendezVousFormGroupInput for create.
 */
type RendezVousFormGroupInput = IRendezVous | PartialWithRequiredKeyOf<NewRendezVous>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRendezVous | NewRendezVous> = Omit<T, 'dateRdv'> & {
  dateRdv?: string | null;
};

type RendezVousFormRawValue = FormValueOf<IRendezVous>;

type NewRendezVousFormRawValue = FormValueOf<NewRendezVous>;

type RendezVousFormDefaults = Pick<NewRendezVous, 'id' | 'dateRdv'>;

type RendezVousFormGroupContent = {
  id: FormControl<RendezVousFormRawValue['id'] | NewRendezVous['id']>;
  dateRdv: FormControl<RendezVousFormRawValue['dateRdv']>;
  motif: FormControl<RendezVousFormRawValue['motif']>;
  statut: FormControl<RendezVousFormRawValue['statut']>;
  commentaire: FormControl<RendezVousFormRawValue['commentaire']>;
  candidat: FormControl<RendezVousFormRawValue['candidat']>;
};

export type RendezVousFormGroup = FormGroup<RendezVousFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RendezVousFormService {
  createRendezVousFormGroup(rendezVous: RendezVousFormGroupInput = { id: null }): RendezVousFormGroup {
    const rendezVousRawValue = this.convertRendezVousToRendezVousRawValue({
      ...this.getFormDefaults(),
      ...rendezVous,
    });
    return new FormGroup<RendezVousFormGroupContent>({
      id: new FormControl(
        { value: rendezVousRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateRdv: new FormControl(rendezVousRawValue.dateRdv, {
        validators: [Validators.required],
      }),
      motif: new FormControl(rendezVousRawValue.motif, {
        validators: [Validators.required],
      }),
      statut: new FormControl(rendezVousRawValue.statut),
      commentaire: new FormControl(rendezVousRawValue.commentaire),
      candidat: new FormControl(rendezVousRawValue.candidat, {
        validators: [Validators.required],
      }),
    });
  }

  getRendezVous(form: RendezVousFormGroup): IRendezVous | NewRendezVous {
    return this.convertRendezVousRawValueToRendezVous(form.getRawValue() as RendezVousFormRawValue | NewRendezVousFormRawValue);
  }

  resetForm(form: RendezVousFormGroup, rendezVous: RendezVousFormGroupInput): void {
    const rendezVousRawValue = this.convertRendezVousToRendezVousRawValue({ ...this.getFormDefaults(), ...rendezVous });
    form.reset(
      {
        ...rendezVousRawValue,
        id: { value: rendezVousRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RendezVousFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateRdv: currentTime,
    };
  }

  private convertRendezVousRawValueToRendezVous(
    rawRendezVous: RendezVousFormRawValue | NewRendezVousFormRawValue,
  ): IRendezVous | NewRendezVous {
    return {
      ...rawRendezVous,
      dateRdv: dayjs(rawRendezVous.dateRdv, DATE_TIME_FORMAT),
    };
  }

  private convertRendezVousToRendezVousRawValue(
    rendezVous: IRendezVous | (Partial<NewRendezVous> & RendezVousFormDefaults),
  ): RendezVousFormRawValue | PartialWithRequiredKeyOf<NewRendezVousFormRawValue> {
    return {
      ...rendezVous,
      dateRdv: rendezVous.dateRdv ? rendezVous.dateRdv.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
