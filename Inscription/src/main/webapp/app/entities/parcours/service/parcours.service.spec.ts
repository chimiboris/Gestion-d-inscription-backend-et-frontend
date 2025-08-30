import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParcours } from '../parcours.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../parcours.test-samples';

import { ParcoursService, RestParcours } from './parcours.service';

const requireRestSample: RestParcours = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
  dateFin: sampleWithRequiredData.dateFin?.toJSON(),
};

describe('Parcours Service', () => {
  let service: ParcoursService;
  let httpMock: HttpTestingController;
  let expectedResult: IParcours | IParcours[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParcoursService);
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

    it('should create a Parcours', () => {
      const parcours = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(parcours).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Parcours', () => {
      const parcours = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(parcours).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Parcours', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Parcours', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Parcours', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addParcoursToCollectionIfMissing', () => {
      it('should add a Parcours to an empty array', () => {
        const parcours: IParcours = sampleWithRequiredData;
        expectedResult = service.addParcoursToCollectionIfMissing([], parcours);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parcours);
      });

      it('should not add a Parcours to an array that contains it', () => {
        const parcours: IParcours = sampleWithRequiredData;
        const parcoursCollection: IParcours[] = [
          {
            ...parcours,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParcoursToCollectionIfMissing(parcoursCollection, parcours);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Parcours to an array that doesn't contain it", () => {
        const parcours: IParcours = sampleWithRequiredData;
        const parcoursCollection: IParcours[] = [sampleWithPartialData];
        expectedResult = service.addParcoursToCollectionIfMissing(parcoursCollection, parcours);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parcours);
      });

      it('should add only unique Parcours to an array', () => {
        const parcoursArray: IParcours[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const parcoursCollection: IParcours[] = [sampleWithRequiredData];
        expectedResult = service.addParcoursToCollectionIfMissing(parcoursCollection, ...parcoursArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parcours: IParcours = sampleWithRequiredData;
        const parcours2: IParcours = sampleWithPartialData;
        expectedResult = service.addParcoursToCollectionIfMissing([], parcours, parcours2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parcours);
        expect(expectedResult).toContain(parcours2);
      });

      it('should accept null and undefined values', () => {
        const parcours: IParcours = sampleWithRequiredData;
        expectedResult = service.addParcoursToCollectionIfMissing([], null, parcours, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parcours);
      });

      it('should return initial array if no Parcours is added', () => {
        const parcoursCollection: IParcours[] = [sampleWithRequiredData];
        expectedResult = service.addParcoursToCollectionIfMissing(parcoursCollection, undefined, null);
        expect(expectedResult).toEqual(parcoursCollection);
      });
    });

    describe('compareParcours', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParcours(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareParcours(entity1, entity2);
        const compareResult2 = service.compareParcours(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareParcours(entity1, entity2);
        const compareResult2 = service.compareParcours(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareParcours(entity1, entity2);
        const compareResult2 = service.compareParcours(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
