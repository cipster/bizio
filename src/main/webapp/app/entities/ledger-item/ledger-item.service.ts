import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILedgerItem } from 'app/shared/model/ledger-item.model';

type EntityResponseType = HttpResponse<ILedgerItem>;
type EntityArrayResponseType = HttpResponse<ILedgerItem[]>;

@Injectable({ providedIn: 'root' })
export class LedgerItemService {
  public resourceUrl = SERVER_API_URL + 'api/ledger-items';

  constructor(protected http: HttpClient) {}

  create(ledgerItem: ILedgerItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ledgerItem);
    return this.http
      .post<ILedgerItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ledgerItem: ILedgerItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ledgerItem);
    return this.http
      .put<ILedgerItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILedgerItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILedgerItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(ledgerItem: ILedgerItem): ILedgerItem {
    const copy: ILedgerItem = Object.assign({}, ledgerItem, {
      date: ledgerItem.date && ledgerItem.date.isValid() ? ledgerItem.date.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ledgerItem: ILedgerItem) => {
        ledgerItem.date = ledgerItem.date ? moment(ledgerItem.date) : undefined;
      });
    }
    return res;
  }
}
