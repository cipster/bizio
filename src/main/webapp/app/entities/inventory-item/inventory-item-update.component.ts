import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInventoryItem, InventoryItem } from 'app/shared/model/inventory-item.model';
import { InventoryItemService } from './inventory-item.service';

@Component({
  selector: 'jhi-inventory-item-update',
  templateUrl: './inventory-item-update.component.html'
})
export class InventoryItemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    value: [null, [Validators.required]]
  });

  constructor(protected inventoryItemService: InventoryItemService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItem }) => {
      this.updateForm(inventoryItem);
    });
  }

  updateForm(inventoryItem: IInventoryItem): void {
    this.editForm.patchValue({
      id: inventoryItem.id,
      name: inventoryItem.name,
      value: inventoryItem.value
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventoryItem = this.createFromForm();
    if (inventoryItem.id !== undefined) {
      this.subscribeToSaveResponse(this.inventoryItemService.update(inventoryItem));
    } else {
      this.subscribeToSaveResponse(this.inventoryItemService.create(inventoryItem));
    }
  }

  private createFromForm(): IInventoryItem {
    return {
      ...new InventoryItem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      value: this.editForm.get(['value'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventoryItem>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
