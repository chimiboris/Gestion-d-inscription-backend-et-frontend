import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { TypePieceIdentite } from 'app/entities/enumerations/type-piece-identite.model';
import { CandidatService } from '../service/candidat.service';
import { ICandidat } from '../candidat.model';
import { CandidatFormService, CandidatFormGroup } from './candidat-form.service';

@Component({
  standalone: true,
  selector: 'jhi-candidat-update',
  templateUrl: './candidat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CandidatUpdateComponent implements OnInit {
  isSaving = false;
  candidat: ICandidat | null = null;
  sexeValues = Object.keys(Sexe);
  typePieceIdentiteValues = Object.keys(TypePieceIdentite);

  usersSharedCollection: IUser[] = [];

  editForm: CandidatFormGroup = this.candidatFormService.createCandidatFormGroup();

  constructor(
    protected candidatService: CandidatService,
    protected candidatFormService: CandidatFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidat }) => {
      this.candidat = candidat;
      if (candidat) {
        this.updateForm(candidat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidat = this.candidatFormService.getCandidat(this.editForm);
    if (candidat.id !== null) {
      this.subscribeToSaveResponse(this.candidatService.update(candidat));
    } else {
      this.subscribeToSaveResponse(this.candidatService.create(candidat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidat>>): void {
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

  protected updateForm(candidat: ICandidat): void {
    this.candidat = candidat;
    this.candidatFormService.resetForm(this.editForm, candidat);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, candidat.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.candidat?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
