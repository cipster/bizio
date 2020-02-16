import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInventoryItem } from 'app/shared/model/inventory-item.model';

@Component({
  selector: 'jhi-inventory-item-detail',
  templateUrl: './inventory-item-detail.component.html'
})
export class InventoryItemDetailComponent implements OnInit {
  inventoryItem: IInventoryItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItem }) => (this.inventoryItem = inventoryItem));
  }

  previousState(): void {
    window.history.back();
  }
}
