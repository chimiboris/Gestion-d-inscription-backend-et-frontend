import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rendez-vous.test-samples';

import { RendezVousFormService } from './rendez-vous-form.service';

describe('RendezVous Form Service', () => {
  let service: RendezVousFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RendezVousFormService);
  });

  describe('Service methods', () => {
    describe('createRendezVousFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRendezVousFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateRdv: expect.any(Object),
            motif: expect.any(Object),
            statut: expect.any(Object),
            commentaire: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });

      it('passing IRendezVous should create a new form with FormGroup', () => {
        const formGroup = service.createRendezVousFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateRdv: expect.any(Object),
            motif: expect.any(Object),
            statut: expect.any(Object),
            commentaire: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });
    });

    describe('getRendezVous', () => {
      it('should return NewRendezVous for default RendezVous initial value', () => {
        const formGroup = service.createRendezVousFormGroup(sampleWithNewData);

        const rendezVous = service.getRendezVous(formGroup) as any;

        expect(rendezVous).toMatchObject(sampleWithNewData);
      });

      it('should return NewRendezVous for empty RendezVous initial value', () => {
        const formGroup = service.createRendezVousFormGroup();

        const rendezVous = service.getRendezVous(formGroup) as any;

        expect(rendezVous).toMatchObject({});
      });

      it('should return IRendezVous', () => {
        const formGroup = service.createRendezVousFormGroup(sampleWithRequiredData);

        const rendezVous = service.getRendezVous(formGroup) as any;

        expect(rendezVous).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRendezVous should not enable id FormControl', () => {
        const formGroup = service.createRendezVousFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRendezVous should disable id FormControl', () => {
        const formGroup = service.createRendezVousFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
