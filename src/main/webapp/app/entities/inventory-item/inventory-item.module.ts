import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BizioSharedModule } from 'app/shared/shared.module';
import { InventoryItemComponent } from './inventory-item.component';
import { InventoryItemDetailComponent } from './inventory-item-detail.component';
import { InventoryItemUpdateComponent } from './inventory-item-update.component';
import { InventoryItemDeleteDialogComponent } from './inventory-item-delete-dialog.component';
import { inventoryItemRoute } from './inventory-item.route';

@NgModule({
  imports: [BizioSharedModule, RouterModule.forChild(inventoryItemRoute)],
  declarations: [InventoryItemComponent, InventoryItemDetailComponent, InventoryItemUpdateComponent, InventoryItemDeleteDialogComponent],
  entryComponents: [InventoryItemDeleteDialogComponent]
})
export class BizioInventoryItemModule {}
