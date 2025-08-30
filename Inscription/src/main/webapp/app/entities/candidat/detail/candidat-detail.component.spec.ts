import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CandidatDetailComponent } from './candidat-detail.component';

describe('Candidat Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidatDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CandidatDetailComponent,
              resolve: { candidat: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CandidatDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load candidat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CandidatDetailComponent);

      // THEN
      expect(instance.candidat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
