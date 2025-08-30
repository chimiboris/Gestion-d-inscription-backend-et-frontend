import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICandidat } from '../candidat.model';
import { CandidatService } from '../service/candidat.service';

@Component({
  standalone: true,
  templateUrl: './candidat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CandidatDeleteDialogComponent {
  candidat?: ICandidat;

  constructor(
    protected candidatService: CandidatService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.candidatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
