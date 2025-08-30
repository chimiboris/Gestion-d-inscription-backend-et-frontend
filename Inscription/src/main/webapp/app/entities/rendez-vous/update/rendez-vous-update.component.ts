import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { statutRdv } from 'app/entities/enumerations/statut-rdv.model';
import { RendezVousService } from '../service/rendez-vous.service';
import { IRendezVous } from '../rendez-vous.model';
import { RendezVousFormService, RendezVousFormGroup } from './rendez-vous-form.service';

@Component({
  standalone: true,
  selector: 'jhi-rendez-vous-update',
  templateUrl: './rendez-vous-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RendezVousUpdateComponent implements OnInit {
  isSaving = false;
  rendezVous: IRendezVous | null = null;
  statutRdvValues = Object.keys(statutRdv);

  candidatsSharedCollection: ICandidat[] = [];

  editForm: RendezVousFormGroup = this.rendezVousFormService.createRendezVousFormGroup();

  constructor(
    protected rendezVousService: RendezVousService,
    protected rendezVousFormService: RendezVousFormService,
    protected candidatService: CandidatService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCandidat = (o1: ICandidat | null, o2: ICandidat | null): boolean => this.candidatService.compareCandidat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rendezVous }) => {
      this.rendezVous = rendezVous;
      if (rendezVous) {
        this.updateForm(rendezVous);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rendezVous = this.rendezVousFormService.getRendezVous(this.editForm);
    if (rendezVous.id !== null) {
      this.subscribeToSaveResponse(this.rendezVousService.update(rendezVous));
    } else {
      this.subscribeToSaveResponse(this.rendezVousService.create(rendezVous));
    }
    alert("Rendez-vous confirmer");
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRendezVous>>): void {
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

  protected updateForm(rendezVous: IRendezVous): void {
    this.rendezVous = rendezVous;
    this.rendezVousFormService.resetForm(this.editForm, rendezVous);

    this.candidatsSharedCollection = this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(
      this.candidatsSharedCollection,
      rendezVous.candidat,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.candidatService
      .query()
      .pipe(map((res: HttpResponse<ICandidat[]>) => res.body ?? []))
      .pipe(
        map((candidats: ICandidat[]) =>
          this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(candidats, this.rendezVous?.candidat),
        ),
      )
      .subscribe((candidats: ICandidat[]) => (this.candidatsSharedCollection = candidats));
  }
}
