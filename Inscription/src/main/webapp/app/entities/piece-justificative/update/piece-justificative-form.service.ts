import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPieceJustificative, NewPieceJustificative } from '../piece-justificative.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPieceJustificative for edit and NewPieceJustificativeFormGroupInput for create.
 */
type PieceJustificativeFormGroupInput = IPieceJustificative | PartialWithRequiredKeyOf<NewPieceJustificative>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPieceJustificative | NewPieceJustificative> = Omit<T, 'dateUpload'> & {
  dateUpload?: string | null;
};

type PieceJustificativeFormRawValue = FormValueOf<IPieceJustificative>;

type NewPieceJustificativeFormRawValue = FormValueOf<NewPieceJustificative>;

type PieceJustificativeFormDefaults = Pick<NewPieceJustificative, 'id' | 'dateUpload' | 'valide'>;

type PieceJustificativeFormGroupContent = {
  id: FormControl<PieceJustificativeFormRawValue['id'] | NewPieceJustificative['id']>;
  type: FormControl<PieceJustificativeFormRawValue['type']>;
  fichier: FormControl<PieceJustificativeFormRawValue['fichier']>;
  fichierContentType: FormControl<PieceJustificativeFormRawValue['fichierContentType']>;
  dateUpload: FormControl<PieceJustificativeFormRawValue['dateUpload']>;
  valide: FormControl<PieceJustificativeFormRawValue['valide']>;
  commentaire: FormControl<PieceJustificativeFormRawValue['commentaire']>;
  candidat: FormControl<PieceJustificativeFormRawValue['candidat']>;
};

export type PieceJustificativeFormGroup = FormGroup<PieceJustificativeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PieceJustificativeFormService {
  createPieceJustificativeFormGroup(pieceJustificative: PieceJustificativeFormGroupInput = { id: null }): PieceJustificativeFormGroup {
    const pieceJustificativeRawValue = this.convertPieceJustificativeToPieceJustificativeRawValue({
      ...this.getFormDefaults(),
      ...pieceJustificative,
    });
    return new FormGroup<PieceJustificativeFormGroupContent>({
      id: new FormControl(
        { value: pieceJustificativeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(pieceJustificativeRawValue.type, {
        validators: [Validators.required],
      }),
      fichier: new FormControl(pieceJustificativeRawValue.fichier, {
        validators: [Validators.required],
      }),
      fichierContentType: new FormControl(pieceJustificativeRawValue.fichierContentType),
      dateUpload: new FormControl(pieceJustificativeRawValue.dateUpload),
      valide: new FormControl(pieceJustificativeRawValue.valide),
      commentaire: new FormControl(pieceJustificativeRawValue.commentaire),
      candidat: new FormControl(pieceJustificativeRawValue.candidat, {
        validators: [Validators.required],
      }),
    });
  }

  getPieceJustificative(form: PieceJustificativeFormGroup): IPieceJustificative | NewPieceJustificative {
    return this.convertPieceJustificativeRawValueToPieceJustificative(
      form.getRawValue() as PieceJustificativeFormRawValue | NewPieceJustificativeFormRawValue,
    );
  }

  resetForm(form: PieceJustificativeFormGroup, pieceJustificative: PieceJustificativeFormGroupInput): void {
    const pieceJustificativeRawValue = this.convertPieceJustificativeToPieceJustificativeRawValue({
      ...this.getFormDefaults(),
      ...pieceJustificative,
    });
    form.reset(
      {
        ...pieceJustificativeRawValue,
        id: { value: pieceJustificativeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PieceJustificativeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateUpload: currentTime,
      valide: false,
    };
  }

  private convertPieceJustificativeRawValueToPieceJustificative(
    rawPieceJustificative: PieceJustificativeFormRawValue | NewPieceJustificativeFormRawValue,
  ): IPieceJustificative | NewPieceJustificative {
    return {
      ...rawPieceJustificative,
      dateUpload: dayjs(rawPieceJustificative.dateUpload, DATE_TIME_FORMAT),
    };
  }

  private convertPieceJustificativeToPieceJustificativeRawValue(
    pieceJustificative: IPieceJustificative | (Partial<NewPieceJustificative> & PieceJustificativeFormDefaults),
  ): PieceJustificativeFormRawValue | PartialWithRequiredKeyOf<NewPieceJustificativeFormRawValue> {
    return {
      ...pieceJustificative,
      dateUpload: pieceJustificative.dateUpload ? pieceJustificative.dateUpload.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
