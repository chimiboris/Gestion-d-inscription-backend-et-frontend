import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { IContactUrgence } from '../contact-urgence.model';
import { ContactUrgenceService } from '../service/contact-urgence.service';
import { ContactUrgenceFormService, ContactUrgenceFormGroup } from './contact-urgence-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contact-urgence-update',
  templateUrl: './contact-urgence-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContactUrgenceUpdateComponent implements OnInit {
  isSaving = false;
  contactUrgence: IContactUrgence | null = null;

  candidatsCollection: ICandidat[] = [];

  editForm: ContactUrgenceFormGroup = this.contactUrgenceFormService.createContactUrgenceFormGroup();

  constructor(
    protected contactUrgenceService: ContactUrgenceService,
    protected contactUrgenceFormService: ContactUrgenceFormService,
    protected candidatService: CandidatService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCandidat = (o1: ICandidat | null, o2: ICandidat | null): boolean => this.candidatService.compareCandidat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactUrgence }) => {
      this.contactUrgence = contactUrgence;
      if (contactUrgence) {
        this.updateForm(contactUrgence);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactUrgence = this.contactUrgenceFormService.getContactUrgence(this.editForm);
    if (contactUrgence.id !== null) {
      this.subscribeToSaveResponse(this.contactUrgenceService.update(contactUrgence));
    } else {
      this.subscribeToSaveResponse(this.contactUrgenceService.create(contactUrgence));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactUrgence>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contactUrgence: IContactUrgence): void {
    this.contactUrgence = contactUrgence;
    this.contactUrgenceFormService.resetForm(this.editForm, contactUrgence);

    this.candidatsCollection = this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(
      this.candidatsCollection,
      contactUrgence.candidat,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.candidatService
      .query({ filter: 'contacturgence-is-null' })
      .pipe(map((res: HttpResponse<ICandidat[]>) => res.body ?? []))
      .pipe(
        map((candidats: ICandidat[]) =>
          this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(candidats, this.contactUrgence?.candidat),
        ),
      )
      .subscribe((candidats: ICandidat[]) => (this.candidatsCollection = candidats));
  }
}
