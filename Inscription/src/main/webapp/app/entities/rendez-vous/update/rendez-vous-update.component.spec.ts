import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { RendezVousService } from '../service/rendez-vous.service';
import { IRendezVous } from '../rendez-vous.model';
import { RendezVousFormService } from './rendez-vous-form.service';

import { RendezVousUpdateComponent } from './rendez-vous-update.component';

describe('RendezVous Management Update Component', () => {
  let comp: RendezVousUpdateComponent;
  let fixture: ComponentFixture<RendezVousUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rendezVousFormService: RendezVousFormService;
  let rendezVousService: RendezVousService;
  let candidatService: CandidatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RendezVousUpdateComponent],
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
      .overrideTemplate(RendezVousUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RendezVousUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rendezVousFormService = TestBed.inject(RendezVousFormService);
    rendezVousService = TestBed.inject(RendezVousService);
    candidatService = TestBed.inject(CandidatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Candidat query and add missing value', () => {
      const rendezVous: IRendezVous = { id: 456 };
      const candidat: ICandidat = { id: 9758 };
      rendezVous.candidat = candidat;

      const candidatCollection: ICandidat[] = [{ id: 24714 }];
      jest.spyOn(candidatService, 'query').mockReturnValue(of(new HttpResponse({ body: candidatCollection })));
      const additionalCandidats = [candidat];
      const expectedCollection: ICandidat[] = [...additionalCandidats, ...candidatCollection];
      jest.spyOn(candidatService, 'addCandidatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rendezVous });
      comp.ngOnInit();

      expect(candidatService.query).toHaveBeenCalled();
      expect(candidatService.addCandidatToCollectionIfMissing).toHaveBeenCalledWith(
        candidatCollection,
        ...additionalCandidats.map(expect.objectContaining),
      );
      expect(comp.candidatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rendezVous: IRendezVous = { id: 456 };
      const candidat: ICandidat = { id: 3988 };
      rendezVous.candidat = candidat;

      activatedRoute.data = of({ rendezVous });
      comp.ngOnInit();

      expect(comp.candidatsSharedCollection).toContain(candidat);
      expect(comp.rendezVous).toEqual(rendezVous);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRendezVous>>();
      const rendezVous = { id: 123 };
      jest.spyOn(rendezVousFormService, 'getRendezVous').mockReturnValue(rendezVous);
      jest.spyOn(rendezVousService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rendezVous });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rendezVous }));
      saveSubject.complete();

      // THEN
      expect(rendezVousFormService.getRendezVous).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rendezVousService.update).toHaveBeenCalledWith(expect.objectContaining(rendezVous));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRendezVous>>();
      const rendezVous = { id: 123 };
      jest.spyOn(rendezVousFormService, 'getRendezVous').mockReturnValue({ id: null });
      jest.spyOn(rendezVousService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rendezVous: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rendezVous }));
      saveSubject.complete();

      // THEN
      expect(rendezVousFormService.getRendezVous).toHaveBeenCalled();
      expect(rendezVousService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRendezVous>>();
      const rendezVous = { id: 123 };
      jest.spyOn(rendezVousService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rendezVous });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rendezVousService.update).toHaveBeenCalled();
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
