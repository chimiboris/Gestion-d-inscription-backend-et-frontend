import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IParcours } from '../parcours.model';
import { ParcoursService } from '../service/parcours.service';

@Component({
  standalone: true,
  templateUrl: './parcours-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ParcoursDeleteDialogComponent {
  parcours?: IParcours;

  constructor(
    protected parcoursService: ParcoursService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parcoursService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
