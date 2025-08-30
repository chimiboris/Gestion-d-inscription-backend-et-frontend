import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RendezVousDetailComponent } from './rendez-vous-detail.component';

describe('RendezVous Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RendezVousDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RendezVousDetailComponent,
              resolve: { rendezVous: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RendezVousDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load rendezVous on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RendezVousDetailComponent);

      // THEN
      expect(instance.rendezVous).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
