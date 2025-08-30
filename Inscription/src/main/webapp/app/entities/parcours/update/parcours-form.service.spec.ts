import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../parcours.test-samples';

import { ParcoursFormService } from './parcours-form.service';

describe('Parcours Form Service', () => {
  let service: ParcoursFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParcoursFormService);
  });

  describe('Service methods', () => {
    describe('createParcoursFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParcoursFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etablissement: expect.any(Object),
            specialisation: expect.any(Object),
            niveau: expect.any(Object),
            commentaire: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });

      it('passing IParcours should create a new form with FormGroup', () => {
        const formGroup = service.createParcoursFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etablissement: expect.any(Object),
            specialisation: expect.any(Object),
            niveau: expect.any(Object),
            commentaire: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });
    });

    describe('getParcours', () => {
      it('should return NewParcours for default Parcours initial value', () => {
        const formGroup = service.createParcoursFormGroup(sampleWithNewData);

        const parcours = service.getParcours(formGroup) as any;

        expect(parcours).toMatchObject(sampleWithNewData);
      });

      it('should return NewParcours for empty Parcours initial value', () => {
        const formGroup = service.createParcoursFormGroup();

        const parcours = service.getParcours(formGroup) as any;

        expect(parcours).toMatchObject({});
      });

      it('should return IParcours', () => {
        const formGroup = service.createParcoursFormGroup(sampleWithRequiredData);

        const parcours = service.getParcours(formGroup) as any;

        expect(parcours).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParcours should not enable id FormControl', () => {
        const formGroup = service.createParcoursFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParcours should disable id FormControl', () => {
        const formGroup = service.createParcoursFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
