import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContactUrgenceDetailComponent } from './contact-urgence-detail.component';

describe('ContactUrgence Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContactUrgenceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ContactUrgenceDetailComponent,
              resolve: { contactUrgence: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContactUrgenceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load contactUrgence on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContactUrgenceDetailComponent);

      // THEN
      expect(instance.contactUrgence).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
