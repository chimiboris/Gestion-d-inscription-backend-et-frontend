import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contact-urgence.test-samples';

import { ContactUrgenceFormService } from './contact-urgence-form.service';

describe('ContactUrgence Form Service', () => {
  let service: ContactUrgenceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContactUrgenceFormService);
  });

  describe('Service methods', () => {
    describe('createContactUrgenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContactUrgenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomComplet: expect.any(Object),
            lienParente: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });

      it('passing IContactUrgence should create a new form with FormGroup', () => {
        const formGroup = service.createContactUrgenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomComplet: expect.any(Object),
            lienParente: expect.any(Object),
            telephone: expect.any(Object),
            email: expect.any(Object),
            candidat: expect.any(Object),
          }),
        );
      });
    });

    describe('getContactUrgence', () => {
      it('should return NewContactUrgence for default ContactUrgence initial value', () => {
        const formGroup = service.createContactUrgenceFormGroup(sampleWithNewData);

        const contactUrgence = service.getContactUrgence(formGroup) as any;

        expect(contactUrgence).toMatchObject(sampleWithNewData);
      });

      it('should return NewContactUrgence for empty ContactUrgence initial value', () => {
        const formGroup = service.createContactUrgenceFormGroup();

        const contactUrgence = service.getContactUrgence(formGroup) as any;

        expect(contactUrgence).toMatchObject({});
      });

      it('should return IContactUrgence', () => {
        const formGroup = service.createContactUrgenceFormGroup(sampleWithRequiredData);

        const contactUrgence = service.getContactUrgence(formGroup) as any;

        expect(contactUrgence).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContactUrgence should not enable id FormControl', () => {
        const formGroup = service.createContactUrgenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContactUrgence should disable id FormControl', () => {
        const formGroup = service.createContactUrgenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
