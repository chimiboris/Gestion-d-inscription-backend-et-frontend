import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPieceJustificative } from '../piece-justificative.model';
import { PieceJustificativeService } from '../service/piece-justificative.service';

@Component({
  standalone: true,
  templateUrl: './piece-justificative-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PieceJustificativeDeleteDialogComponent {
  pieceJustificative?: IPieceJustificative;

  constructor(
    protected pieceJustificativeService: PieceJustificativeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pieceJustificativeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
