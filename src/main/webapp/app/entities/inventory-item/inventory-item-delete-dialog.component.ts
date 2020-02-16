import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInventoryItem } from 'app/shared/model/inventory-item.model';
import { InventoryItemService } from './inventory-item.service';

@Component({
  templateUrl: './inventory-item-delete-dialog.component.html'
})
export class InventoryItemDeleteDialogComponent {
  inventoryItem?: IInventoryItem;

  constructor(
    protected inventoryItemService: InventoryItemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inventoryItemService.delete(id).subscribe(() => {
      this.eventManager.broadcast('inventoryItemListModification');
      this.activeModal.close();
    });
  }
}
