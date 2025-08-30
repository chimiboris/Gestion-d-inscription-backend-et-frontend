import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { ParcoursService } from '../service/parcours.service';
import { IParcours } from '../parcours.model';
import { ParcoursFormService } from './parcours-form.service';

import { ParcoursUpdateComponent } from './parcours-update.component';

describe('Parcours Management Update Component', () => {
  let comp: ParcoursUpdateComponent;
  let fixture: ComponentFixture<ParcoursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parcoursFormService: ParcoursFormService;
  let parcoursService: ParcoursService;
  let candidatService: CandidatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ParcoursUpdateComponent],
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
      .overrideTemplate(ParcoursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParcoursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parcoursFormService = TestBed.inject(ParcoursFormService);
    parcoursService = TestBed.inject(ParcoursService);
    candidatService = TestBed.inject(CandidatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Candidat query and add missing value', () => {
      const parcours: IParcours = { id: 456 };
      const candidat: ICandidat = { id: 16139 };
      parcours.candidat = candidat;

      const candidatCollection: ICandidat[] = [{ id: 23270 }];
      jest.spyOn(candidatService, 'query').mockReturnValue(of(new HttpResponse({ body: candidatCollection })));
      const additionalCandidats = [candidat];
      const expectedCollection: ICandidat[] = [...additionalCandidats, ...candidatCollection];
      jest.spyOn(candidatService, 'addCandidatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ parcours });
      comp.ngOnInit();

      expect(candidatService.query).toHaveBeenCalled();
      expect(candidatService.addCandidatToCollectionIfMissing).toHaveBeenCalledWith(
        candidatCollection,
        ...additionalCandidats.map(expect.objectContaining),
      );
      expect(comp.candidatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const parcours: IParcours = { id: 456 };
      const candidat: ICandidat = { id: 21634 };
      parcours.candidat = candidat;

      activatedRoute.data = of({ parcours });
      comp.ngOnInit();

      expect(comp.candidatsSharedCollection).toContain(candidat);
      expect(comp.parcours).toEqual(parcours);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParcours>>();
      const parcours = { id: 123 };
      jest.spyOn(parcoursFormService, 'getParcours').mockReturnValue(parcours);
      jest.spyOn(parcoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parcours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parcours }));
      saveSubject.complete();

      // THEN
      expect(parcoursFormService.getParcours).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parcoursService.update).toHaveBeenCalledWith(expect.objectContaining(parcours));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParcours>>();
      const parcours = { id: 123 };
      jest.spyOn(parcoursFormService, 'getParcours').mockReturnValue({ id: null });
      jest.spyOn(parcoursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parcours: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parcours }));
      saveSubject.complete();

      // THEN
      expect(parcoursFormService.getParcours).toHaveBeenCalled();
      expect(parcoursService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParcours>>();
      const parcours = { id: 123 };
      jest.spyOn(parcoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parcours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parcoursService.update).toHaveBeenCalled();
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
