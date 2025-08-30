import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContactUrgence } from '../contact-urgence.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contact-urgence.test-samples';

import { ContactUrgenceService } from './contact-urgence.service';

const requireRestSample: IContactUrgence = {
  ...sampleWithRequiredData,
};

describe('ContactUrgence Service', () => {
  let service: ContactUrgenceService;
  let httpMock: HttpTestingController;
  let expectedResult: IContactUrgence | IContactUrgence[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContactUrgenceService);
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

    it('should create a ContactUrgence', () => {
      const contactUrgence = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contactUrgence).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactUrgence', () => {
      const contactUrgence = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contactUrgence).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContactUrgence', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContactUrgence', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContactUrgence', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContactUrgenceToCollectionIfMissing', () => {
      it('should add a ContactUrgence to an empty array', () => {
        const contactUrgence: IContactUrgence = sampleWithRequiredData;
        expectedResult = service.addContactUrgenceToCollectionIfMissing([], contactUrgence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactUrgence);
      });

      it('should not add a ContactUrgence to an array that contains it', () => {
        const contactUrgence: IContactUrgence = sampleWithRequiredData;
        const contactUrgenceCollection: IContactUrgence[] = [
          {
            ...contactUrgence,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContactUrgenceToCollectionIfMissing(contactUrgenceCollection, contactUrgence);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactUrgence to an array that doesn't contain it", () => {
        const contactUrgence: IContactUrgence = sampleWithRequiredData;
        const contactUrgenceCollection: IContactUrgence[] = [sampleWithPartialData];
        expectedResult = service.addContactUrgenceToCollectionIfMissing(contactUrgenceCollection, contactUrgence);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactUrgence);
      });

      it('should add only unique ContactUrgence to an array', () => {
        const contactUrgenceArray: IContactUrgence[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contactUrgenceCollection: IContactUrgence[] = [sampleWithRequiredData];
        expectedResult = service.addContactUrgenceToCollectionIfMissing(contactUrgenceCollection, ...contactUrgenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactUrgence: IContactUrgence = sampleWithRequiredData;
        const contactUrgence2: IContactUrgence = sampleWithPartialData;
        expectedResult = service.addContactUrgenceToCollectionIfMissing([], contactUrgence, contactUrgence2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactUrgence);
        expect(expectedResult).toContain(contactUrgence2);
      });

      it('should accept null and undefined values', () => {
        const contactUrgence: IContactUrgence = sampleWithRequiredData;
        expectedResult = service.addContactUrgenceToCollectionIfMissing([], null, contactUrgence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactUrgence);
      });

      it('should return initial array if no ContactUrgence is added', () => {
        const contactUrgenceCollection: IContactUrgence[] = [sampleWithRequiredData];
        expectedResult = service.addContactUrgenceToCollectionIfMissing(contactUrgenceCollection, undefined, null);
        expect(expectedResult).toEqual(contactUrgenceCollection);
      });
    });

    describe('compareContactUrgence', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContactUrgence(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContactUrgence(entity1, entity2);
        const compareResult2 = service.compareContactUrgence(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContactUrgence(entity1, entity2);
        const compareResult2 = service.compareContactUrgence(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContactUrgence(entity1, entity2);
        const compareResult2 = service.compareContactUrgence(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
