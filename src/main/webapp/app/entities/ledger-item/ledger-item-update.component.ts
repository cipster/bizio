import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILedgerItem, LedgerItem } from 'app/shared/model/ledger-item.model';
import { LedgerItemService } from './ledger-item.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client/client.service';

@Component({
  selector: 'jhi-ledger-item-update',
  templateUrl: './ledger-item-update.component.html'
})
export class LedgerItemUpdateComponent implements OnInit {
  isSaving = false;
  clients: IClient[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    document: [null, [Validators.required]],
    explanation: [null, [Validators.required]],
    type: [null, [Validators.required]],
    value: [null, [Validators.required]],
    year: [null, [Validators.required]],
    client: []
  });

  constructor(
    protected ledgerItemService: LedgerItemService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ledgerItem }) => {
      this.updateForm(ledgerItem);

      this.clientService.query().subscribe((res: HttpResponse<IClient[]>) => (this.clients = res.body || []));
    });
  }

  updateForm(ledgerItem: ILedgerItem): void {
    this.editForm.patchValue({
      id: ledgerItem.id,
      date: ledgerItem.date,
      document: ledgerItem.document,
      explanation: ledgerItem.explanation,
      type: ledgerItem.type,
      value: ledgerItem.value,
      year: ledgerItem.year,
      client: ledgerItem.client
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ledgerItem = this.createFromForm();
    if (ledgerItem.id !== undefined) {
      this.subscribeToSaveResponse(this.ledgerItemService.update(ledgerItem));
    } else {
      this.subscribeToSaveResponse(this.ledgerItemService.create(ledgerItem));
    }
  }

  private createFromForm(): ILedgerItem {
    return {
      ...new LedgerItem(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      document: this.editForm.get(['document'])!.value,
      explanation: this.editForm.get(['explanation'])!.value,
      type: this.editForm.get(['type'])!.value,
      value: this.editForm.get(['value'])!.value,
      year: this.editForm.get(['year'])!.value,
      client: this.editForm.get(['client'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILedgerItem>>): void {
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

  trackById(index: number, item: IClient): any {
    return item.id;
  }
}
