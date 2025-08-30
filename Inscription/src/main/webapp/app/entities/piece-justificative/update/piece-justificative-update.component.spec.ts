import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { PieceJustificativeService } from '../service/piece-justificative.service';
import { IPieceJustificative } from '../piece-justificative.model';
import { PieceJustificativeFormService } from './piece-justificative-form.service';

import { PieceJustificativeUpdateComponent } from './piece-justificative-update.component';

describe('PieceJustificative Management Update Component', () => {
  let comp: PieceJustificativeUpdateComponent;
  let fixture: ComponentFixture<PieceJustificativeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pieceJustificativeFormService: PieceJustificativeFormService;
  let pieceJustificativeService: PieceJustificativeService;
  let candidatService: CandidatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PieceJustificativeUpdateComponent],
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
      .overrideTemplate(PieceJustificativeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PieceJustificativeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pieceJustificativeFormService = TestBed.inject(PieceJustificativeFormService);
    pieceJustificativeService = TestBed.inject(PieceJustificativeService);
    candidatService = TestBed.inject(CandidatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Candidat query and add missing value', () => {
      const pieceJustificative: IPieceJustificative = { id: 456 };
      const candidat: ICandidat = { id: 17723 };
      pieceJustificative.candidat = candidat;

      const candidatCollection: ICandidat[] = [{ id: 20479 }];
      jest.spyOn(candidatService, 'query').mockReturnValue(of(new HttpResponse({ body: candidatCollection })));
      const additionalCandidats = [candidat];
      const expectedCollection: ICandidat[] = [...additionalCandidats, ...candidatCollection];
      jest.spyOn(candidatService, 'addCandidatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pieceJustificative });
      comp.ngOnInit();

      expect(candidatService.query).toHaveBeenCalled();
      expect(candidatService.addCandidatToCollectionIfMissing).toHaveBeenCalledWith(
        candidatCollection,
        ...additionalCandidats.map(expect.objectContaining),
      );
      expect(comp.candidatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pieceJustificative: IPieceJustificative = { id: 456 };
      const candidat: ICandidat = { id: 6915 };
      pieceJustificative.candidat = candidat;

      activatedRoute.data = of({ pieceJustificative });
      comp.ngOnInit();

      expect(comp.candidatsSharedCollection).toContain(candidat);
      expect(comp.pieceJustificative).toEqual(pieceJustificative);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPieceJustificative>>();
      const pieceJustificative = { id: 123 };
      jest.spyOn(pieceJustificativeFormService, 'getPieceJustificative').mockReturnValue(pieceJustificative);
      jest.spyOn(pieceJustificativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pieceJustificative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pieceJustificative }));
      saveSubject.complete();

      // THEN
      expect(pieceJustificativeFormService.getPieceJustificative).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pieceJustificativeService.update).toHaveBeenCalledWith(expect.objectContaining(pieceJustificative));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPieceJustificative>>();
      const pieceJustificative = { id: 123 };
      jest.spyOn(pieceJustificativeFormService, 'getPieceJustificative').mockReturnValue({ id: null });
      jest.spyOn(pieceJustificativeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pieceJustificative: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pieceJustificative }));
      saveSubject.complete();

      // THEN
      expect(pieceJustificativeFormService.getPieceJustificative).toHaveBeenCalled();
      expect(pieceJustificativeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPieceJustificative>>();
      const pieceJustificative = { id: 123 };
      jest.spyOn(pieceJustificativeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pieceJustificative });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pieceJustificativeService.update).toHaveBeenCalled();
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
