import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'inventory-item',
        loadChildren: () => import('./inventory-item/inventory-item.module').then(m => m.BizioInventoryItemModule)
      },
      {
        path: 'ledger-item',
        loadChildren: () => import('./ledger-item/ledger-item.module').then(m => m.BizioLedgerItemModule)
      },
      {
        path: 'client',
        loadChildren: () => import('./client/client.module').then(m => m.BizioClientModule)
      },
      {
        path: 'contract',
        loadChildren: () => import('./contract/contract.module').then(m => m.BizioContractModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class BizioEntityModule {}
