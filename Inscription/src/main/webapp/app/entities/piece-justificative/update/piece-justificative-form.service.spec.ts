import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../piece-justificative.test-samples';

import { PieceJustificativeFormService } from './piece-justificative-form.service';

describe('PieceJustificative Form Service', () => {
  let service: PieceJustificativeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PieceJustificativeFormService);
  });

  describe('Service methods', () => {
    describe('createPieceJustificativeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPieceJustificativeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            fichier: expect.any(Object),
            dateUpload: expect.any(Object),
            valide: expect.any(Object),
            commentaire: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });

      it('passing IPieceJustificative should create a new form with FormGroup', () => {
        const formGroup = service.createPieceJustificativeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            fichier: expect.any(Object),
            dateUpload: expect.any(Object),
            valide: expect.any(Object),
            commentaire: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });
    });

    describe('getPieceJustificative', () => {
      it('should return NewPieceJustificative for default PieceJustificative initial value', () => {
        const formGroup = service.createPieceJustificativeFormGroup(sampleWithNewData);

        const pieceJustificative = service.getPieceJustificative(formGroup) as any;

        expect(pieceJustificative).toMatchObject(sampleWithNewData);
      });

      it('should return NewPieceJustificative for empty PieceJustificative initial value', () => {
        const formGroup = service.createPieceJustificativeFormGroup();

        const pieceJustificative = service.getPieceJustificative(formGroup) as any;

        expect(pieceJustificative).toMatchObject({});
      });

      it('should return IPieceJustificative', () => {
        const formGroup = service.createPieceJustificativeFormGroup(sampleWithRequiredData);

        const pieceJustificative = service.getPieceJustificative(formGroup) as any;

        expect(pieceJustificative).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPieceJustificative should not enable id FormControl', () => {
        const formGroup = service.createPieceJustificativeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPieceJustificative should disable id FormControl', () => {
        const formGroup = service.createPieceJustificativeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
