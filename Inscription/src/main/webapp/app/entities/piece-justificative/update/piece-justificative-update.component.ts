import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { Type } from 'app/entities/enumerations/type.model';
import { PieceJustificativeService } from '../service/piece-justificative.service';
import { IPieceJustificative } from '../piece-justificative.model';
import { PieceJustificativeFormService, PieceJustificativeFormGroup } from './piece-justificative-form.service';

@Component({
  standalone: true,
  selector: 'jhi-piece-justificative-update',
  templateUrl: './piece-justificative-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PieceJustificativeUpdateComponent implements OnInit {
  isSaving = false;
  pieceJustificative: IPieceJustificative | null = null;
  typeValues = Object.keys(Type);

  candidatsSharedCollection: ICandidat[] = [];

  editForm: PieceJustificativeFormGroup = this.pieceJustificativeFormService.createPieceJustificativeFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected pieceJustificativeService: PieceJustificativeService,
    protected pieceJustificativeFormService: PieceJustificativeFormService,
    protected candidatService: CandidatService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCandidat = (o1: ICandidat | null, o2: ICandidat | null): boolean => this.candidatService.compareCandidat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pieceJustificative }) => {
      this.pieceJustificative = pieceJustificative;
      if (pieceJustificative) {
        this.updateForm(pieceJustificative);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('inscriptionApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pieceJustificative = this.pieceJustificativeFormService.getPieceJustificative(this.editForm);
    if (pieceJustificative.id !== null) {
      this.subscribeToSaveResponse(this.pieceJustificativeService.update(pieceJustificative));
    } else {
      this.subscribeToSaveResponse(this.pieceJustificativeService.create(pieceJustificative));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPieceJustificative>>): void {
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

  protected updateForm(pieceJustificative: IPieceJustificative): void {
    this.pieceJustificative = pieceJustificative;
    this.pieceJustificativeFormService.resetForm(this.editForm, pieceJustificative);

    this.candidatsSharedCollection = this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(
      this.candidatsSharedCollection,
      pieceJustificative.candidat,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.candidatService
      .query()
      .pipe(map((res: HttpResponse<ICandidat[]>) => res.body ?? []))
      .pipe(
        map((candidats: ICandidat[]) =>
          this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(candidats, this.pieceJustificative?.candidat),
        ),
      )
      .subscribe((candidats: ICandidat[]) => (this.candidatsSharedCollection = candidats));
  }
}
