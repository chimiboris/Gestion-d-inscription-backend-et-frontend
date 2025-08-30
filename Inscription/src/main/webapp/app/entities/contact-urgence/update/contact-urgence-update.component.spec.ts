import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { ContactUrgenceService } from '../service/contact-urgence.service';
import { IContactUrgence } from '../contact-urgence.model';
import { ContactUrgenceFormService } from './contact-urgence-form.service';

import { ContactUrgenceUpdateComponent } from './contact-urgence-update.component';

describe('ContactUrgence Management Update Component', () => {
  let comp: ContactUrgenceUpdateComponent;
  let fixture: ComponentFixture<ContactUrgenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactUrgenceFormService: ContactUrgenceFormService;
  let contactUrgenceService: ContactUrgenceService;
  let candidatService: CandidatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ContactUrgenceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ContactUrgenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUrgenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactUrgenceFormService = TestBed.inject(ContactUrgenceFormService);
    contactUrgenceService = TestBed.inject(ContactUrgenceService);
    candidatService = TestBed.inject(CandidatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call candidat query and add missing value', () => {
      const contactUrgence: IContactUrgence = { id: 456 };
      const candidat: ICandidat = { id: 15388 };
      contactUrgence.candidat = candidat;

      const candidatCollection: ICandidat[] = [{ id: 24896 }];
      jest.spyOn(candidatService, 'query').mockReturnValue(of(new HttpResponse({ body: candidatCollection })));
      const expectedCollection: ICandidat[] = [candidat, ...candidatCollection];
      jest.spyOn(candidatService, 'addCandidatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contactUrgence });
      comp.ngOnInit();

      expect(candidatService.query).toHaveBeenCalled();
      expect(candidatService.addCandidatToCollectionIfMissing).toHaveBeenCalledWith(candidatCollection, candidat);
      expect(comp.candidatsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contactUrgence: IContactUrgence = { id: 456 };
      const candidat: ICandidat = { id: 12005 };
      contactUrgence.candidat = candidat;

      activatedRoute.data = of({ contactUrgence });
      comp.ngOnInit();

      expect(comp.candidatsCollection).toContain(candidat);
      expect(comp.contactUrgence).toEqual(contactUrgence);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactUrgence>>();
      const contactUrgence = { id: 123 };
      jest.spyOn(contactUrgenceFormService, 'getContactUrgence').mockReturnValue(contactUrgence);
      jest.spyOn(contactUrgenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUrgence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactUrgence }));
      saveSubject.complete();

      // THEN
      expect(contactUrgenceFormService.getContactUrgence).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactUrgenceService.update).toHaveBeenCalledWith(expect.objectContaining(contactUrgence));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactUrgence>>();
      const contactUrgence = { id: 123 };
      jest.spyOn(contactUrgenceFormService, 'getContactUrgence').mockReturnValue({ id: null });
      jest.spyOn(contactUrgenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUrgence: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactUrgence }));
      saveSubject.complete();

      // THEN
      expect(contactUrgenceFormService.getContactUrgence).toHaveBeenCalled();
      expect(contactUrgenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactUrgence>>();
      const contactUrgence = { id: 123 };
      jest.spyOn(contactUrgenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUrgence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactUrgenceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCandidat', () => {
      it('Should forward to candidatService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(candidatService, 'compareCandidat');
        comp.compareCandidat(entity, entity2);
        expect(candidatService.compareCandidat).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
