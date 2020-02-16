import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BizioSharedModule } from 'app/shared/shared.module';
import { LedgerItemComponent } from './ledger-item.component';
import { LedgerItemDetailComponent } from './ledger-item-detail.component';
import { LedgerItemUpdateComponent } from './ledger-item-update.component';
import { LedgerItemDeleteDialogComponent } from './ledger-item-delete-dialog.component';
import { ledgerItemRoute } from './ledger-item.route';

@NgModule({
  imports: [BizioSharedModule, RouterModule.forChild(ledgerItemRoute)],
  declarations: [LedgerItemComponent, LedgerItemDetailComponent, LedgerItemUpdateComponent, LedgerItemDeleteDialogComponent],
  entryComponents: [LedgerItemDeleteDialogComponent]
})
export class BizioLedgerItemModule {}
