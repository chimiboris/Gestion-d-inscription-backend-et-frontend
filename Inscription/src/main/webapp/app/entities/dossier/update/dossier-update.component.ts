import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { StatutDossier } from 'app/entities/enumerations/statut-dossier.model';
import { DossierService } from '../service/dossier.service';
import { IDossier } from '../dossier.model';
import { DossierFormService, DossierFormGroup } from './dossier-form.service';

@Component({
  standalone: true,
  selector: 'jhi-dossier-update',
  templateUrl: './dossier-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DossierUpdateComponent implements OnInit {
  isSaving = false;
  dossier: IDossier | null = null;
  statutDossierValues = Object.keys(StatutDossier);

  candidatsCollection: ICandidat[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: DossierFormGroup = this.dossierFormService.createDossierFormGroup();

  constructor(
    protected dossierService: DossierService,
    protected dossierFormService: DossierFormService,
    protected candidatService: CandidatService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCandidat = (o1: ICandidat | null, o2: ICandidat | null): boolean => this.candidatService.compareCandidat(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossier }) => {
      this.dossier = dossier;
      if (dossier) {
        this.updateForm(dossier);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dossier = this.dossierFormService.getDossier(this.editForm);
    if (dossier.id !== null) {
      this.subscribeToSaveResponse(this.dossierService.update(dossier));
    } else {
      this.subscribeToSaveResponse(this.dossierService.create(dossier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossier>>): void {
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

  protected updateForm(dossier: IDossier): void {
    this.dossier = dossier;
    this.dossierFormService.resetForm(this.editForm, dossier);

    this.candidatsCollection = this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(this.candidatsCollection, dossier.candidat);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, dossier.agent);
  }

  protected loadRelationshipsOptions(): void {
    this.candidatService
      .query({ filter: 'n-is-null' })
      .pipe(map((res: HttpResponse<ICandidat[]>) => res.body ?? []))
      .pipe(
        map((candidats: ICandidat[]) =>
          this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(candidats, this.dossier?.candidat),
        ),
      )
      .subscribe((candidats: ICandidat[]) => (this.candidatsCollection = candidats));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.dossier?.agent)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
