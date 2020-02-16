import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILedgerItem } from 'app/shared/model/ledger-item.model';

@Component({
  selector: 'jhi-ledger-item-detail',
  templateUrl: './ledger-item-detail.component.html'
})
export class LedgerItemDetailComponent implements OnInit {
  ledgerItem: ILedgerItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ledgerItem }) => (this.ledgerItem = ledgerItem));
  }

  previousState(): void {
    window.history.back();
  }
}
