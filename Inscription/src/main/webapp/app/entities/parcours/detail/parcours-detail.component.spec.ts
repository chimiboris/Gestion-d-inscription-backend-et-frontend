import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParcoursDetailComponent } from './parcours-detail.component';

describe('Parcours Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParcoursDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ParcoursDetailComponent,
              resolve: { parcours: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ParcoursDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load parcours on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ParcoursDetailComponent);

      // THEN
      expect(instance.parcours).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
