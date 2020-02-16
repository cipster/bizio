import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContract } from 'app/shared/model/contract.model';
import { ContractService } from './contract.service';

@Component({
  templateUrl: './contract-delete-dialog.component.html'
})
export class ContractDeleteDialogComponent {
  contract?: IContract;

  constructor(protected contractService: ContractService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractService.delete(id).subscribe(() => {
      this.eventManager.broadcast('contractListModification');
      this.activeModal.close();
    });
  }
}
