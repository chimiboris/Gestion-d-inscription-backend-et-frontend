import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContactUrgence, NewContactUrgence } from '../contact-urgence.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContactUrgence for edit and NewContactUrgenceFormGroupInput for create.
 */
type ContactUrgenceFormGroupInput = IContactUrgence | PartialWithRequiredKeyOf<NewContactUrgence>;

type ContactUrgenceFormDefaults = Pick<NewContactUrgence, 'id'>;

type ContactUrgenceFormGroupContent = {
  id: FormControl<IContactUrgence['id'] | NewContactUrgence['id']>;
  nomComplet: FormControl<IContactUrgence['nomComplet']>;
  lienParente: FormControl<IContactUrgence['lienParente']>;
  telephone: FormControl<IContactUrgence['telephone']>;
  email: FormControl<IContactUrgence['email']>;
  candidat: FormControl<IContactUrgence['candidat']>;
};

export type ContactUrgenceFormGroup = FormGroup<ContactUrgenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContactUrgenceFormService {
  createContactUrgenceFormGroup(contactUrgence: ContactUrgenceFormGroupInput = { id: null }): ContactUrgenceFormGroup {
    const contactUrgenceRawValue = {
      ...this.getFormDefaults(),
      ...contactUrgence,
    };
    return new FormGroup<ContactUrgenceFormGroupContent>({
      id: new FormControl(
        { value: contactUrgenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomComplet: new FormControl(contactUrgenceRawValue.nomComplet, {
        validators: [Validators.required],
      }),
      lienParente: new FormControl(contactUrgenceRawValue.lienParente, {
        validators: [Validators.required],
      }),
      telephone: new FormControl(contactUrgenceRawValue.telephone, {
        validators: [Validators.required],
      }),
      email: new FormControl(contactUrgenceRawValue.email),
      candidat: new FormControl(contactUrgenceRawValue.candidat, {
        validators: [Validators.required],
      }),
    });
  }

  getContactUrgence(form: ContactUrgenceFormGroup): IContactUrgence | NewContactUrgence {
    return form.getRawValue() as IContactUrgence | NewContactUrgence;
  }

  resetForm(form: ContactUrgenceFormGroup, contactUrgence: ContactUrgenceFormGroupInput): void {
    const contactUrgenceRawValue = { ...this.getFormDefaults(), ...contactUrgence };
    form.reset(
      {
        ...contactUrgenceRawValue,
        id: { value: contactUrgenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContactUrgenceFormDefaults {
    return {
      id: null,
    };
  }
}
