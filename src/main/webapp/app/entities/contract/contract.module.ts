import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BizioSharedModule } from 'app/shared/shared.module';
import { ContractComponent } from './contract.component';
import { ContractDetailComponent } from './contract-detail.component';
import { ContractUpdateComponent } from './contract-update.component';
import { ContractDeleteDialogComponent } from './contract-delete-dialog.component';
import { contractRoute } from './contract.route';

@NgModule({
  imports: [BizioSharedModule, RouterModule.forChild(contractRoute)],
  declarations: [ContractComponent, ContractDetailComponent, ContractUpdateComponent, ContractDeleteDialogComponent],
  entryComponents: [ContractDeleteDialogComponent]
})
export class BizioContractModule {}
