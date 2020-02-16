import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInventoryItem } from 'app/shared/model/inventory-item.model';

type EntityResponseType = HttpResponse<IInventoryItem>;
type EntityArrayResponseType = HttpResponse<IInventoryItem[]>;

@Injectable({ providedIn: 'root' })
export class InventoryItemService {
  public resourceUrl = SERVER_API_URL + 'api/inventory-items';

  constructor(protected http: HttpClient) {}

  create(inventoryItem: IInventoryItem): Observable<EntityResponseType> {
    return this.http.post<IInventoryItem>(this.resourceUrl, inventoryItem, { observe: 'response' });
  }

  update(inventoryItem: IInventoryItem): Observable<EntityResponseType> {
    return this.http.put<IInventoryItem>(this.resourceUrl, inventoryItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInventoryItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInventoryItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
