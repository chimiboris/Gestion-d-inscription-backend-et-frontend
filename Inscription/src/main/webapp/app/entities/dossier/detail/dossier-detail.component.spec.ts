import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DossierDetailComponent } from './dossier-detail.component';

describe('Dossier Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DossierDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DossierDetailComponent,
              resolve: { dossier: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DossierDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load dossier on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DossierDetailComponent);

      // THEN
      expect(instance.dossier).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
