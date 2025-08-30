import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { ParcoursService } from '../service/parcours.service';
import { IParcours } from '../parcours.model';
import { ParcoursFormService, ParcoursFormGroup } from './parcours-form.service';

@Component({
  standalone: true,
  selector: 'jhi-parcours-update',
  templateUrl: './parcours-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParcoursUpdateComponent implements OnInit {
  isSaving = false;
  parcours: IParcours | null = null;
  niveauEtudeValues = Object.keys(NiveauEtude);

  candidatsSharedCollection: ICandidat[] = [];

  editForm: ParcoursFormGroup = this.parcoursFormService.createParcoursFormGroup();

  constructor(
    protected parcoursService: ParcoursService,
    protected parcoursFormService: ParcoursFormService,
    protected candidatService: CandidatService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCandidat = (o1: ICandidat | null, o2: ICandidat | null): boolean => this.candidatService.compareCandidat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parcours }) => {
      this.parcours = parcours;
      if (parcours) {
        this.updateForm(parcours);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parcours = this.parcoursFormService.getParcours(this.editForm);
    if (parcours.id !== null) {
      this.subscribeToSaveResponse(this.parcoursService.update(parcours));
    } else {
      this.subscribeToSaveResponse(this.parcoursService.create(parcours));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParcours>>): void {
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

  protected updateForm(parcours: IParcours): void {
    this.parcours = parcours;
    this.parcoursFormService.resetForm(this.editForm, parcours);

    this.candidatsSharedCollection = this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(
      this.candidatsSharedCollection,
      parcours.candidat,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.candidatService
      .query()
      .pipe(map((res: HttpResponse<ICandidat[]>) => res.body ?? []))
      .pipe(
        map((candidats: ICandidat[]) =>
          this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(candidats, this.parcours?.candidat),
        ),
      )
      .subscribe((candidats: ICandidat[]) => (this.candidatsSharedCollection = candidats));
  }
}
