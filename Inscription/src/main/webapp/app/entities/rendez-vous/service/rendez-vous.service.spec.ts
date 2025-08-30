import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRendezVous } from '../rendez-vous.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rendez-vous.test-samples';

import { RendezVousService, RestRendezVous } from './rendez-vous.service';

const requireRestSample: RestRendezVous = {
  ...sampleWithRequiredData,
  dateRdv: sampleWithRequiredData.dateRdv?.toJSON(),
};

describe('RendezVous Service', () => {
  let service: RendezVousService;
  let httpMock: HttpTestingController;
  let expectedResult: IRendezVous | IRendezVous[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RendezVousService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a RendezVous', () => {
      const rendezVous = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rendezVous).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RendezVous', () => {
      const rendezVous = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rendezVous).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RendezVous', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RendezVous', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RendezVous', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRendezVousToCollectionIfMissing', () => {
      it('should add a RendezVous to an empty array', () => {
        const rendezVous: IRendezVous = sampleWithRequiredData;
        expectedResult = service.addRendezVousToCollectionIfMissing([], rendezVous);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rendezVous);
      });

      it('should not add a RendezVous to an array that contains it', () => {
        const rendezVous: IRendezVous = sampleWithRequiredData;
        const rendezVousCollection: IRendezVous[] = [
          {
            ...rendezVous,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRendezVousToCollectionIfMissing(rendezVousCollection, rendezVous);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RendezVous to an array that doesn't contain it", () => {
        const rendezVous: IRendezVous = sampleWithRequiredData;
        const rendezVousCollection: IRendezVous[] = [sampleWithPartialData];
        expectedResult = service.addRendezVousToCollectionIfMissing(rendezVousCollection, rendezVous);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rendezVous);
      });

      it('should add only unique RendezVous to an array', () => {
        const rendezVousArray: IRendezVous[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rendezVousCollection: IRendezVous[] = [sampleWithRequiredData];
        expectedResult = service.addRendezVousToCollectionIfMissing(rendezVousCollection, ...rendezVousArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rendezVous: IRendezVous = sampleWithRequiredData;
        const rendezVous2: IRendezVous = sampleWithPartialData;
        expectedResult = service.addRendezVousToCollectionIfMissing([], rendezVous, rendezVous2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rendezVous);
        expect(expectedResult).toContain(rendezVous2);
      });

      it('should accept null and undefined values', () => {
        const rendezVous: IRendezVous = sampleWithRequiredData;
        expectedResult = service.addRendezVousToCollectionIfMissing([], null, rendezVous, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rendezVous);
      });

      it('should return initial array if no RendezVous is added', () => {
        const rendezVousCollection: IRendezVous[] = [sampleWithRequiredData];
        expectedResult = service.addRendezVousToCollectionIfMissing(rendezVousCollection, undefined, null);
        expect(expectedResult).toEqual(rendezVousCollection);
      });
    });

    describe('compareRendezVous', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRendezVous(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRendezVous(entity1, entity2);
        const compareResult2 = service.compareRendezVous(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRendezVous(entity1, entity2);
        const compareResult2 = service.compareRendezVous(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRendezVous(entity1, entity2);
        const compareResult2 = service.compareRendezVous(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
