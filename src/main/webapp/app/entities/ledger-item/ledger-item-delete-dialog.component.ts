import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILedgerItem } from 'app/shared/model/ledger-item.model';
import { LedgerItemService } from './ledger-item.service';

@Component({
  templateUrl: './ledger-item-delete-dialog.component.html'
})
export class LedgerItemDeleteDialogComponent {
  ledgerItem?: ILedgerItem;

  constructor(
    protected ledgerItemService: LedgerItemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ledgerItemService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ledgerItemListModification');
      this.activeModal.close();
    });
  }
}
