import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInventoryItem, InventoryItem } from 'app/shared/model/inventory-item.model';
import { InventoryItemService } from './inventory-item.service';
import { InventoryItemComponent } from './inventory-item.component';
import { InventoryItemDetailComponent } from './inventory-item-detail.component';
import { InventoryItemUpdateComponent } from './inventory-item-update.component';

@Injectable({ providedIn: 'root' })
export class InventoryItemResolve implements Resolve<IInventoryItem> {
  constructor(private service: InventoryItemService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInventoryItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((inventoryItem: HttpResponse<InventoryItem>) => {
          if (inventoryItem.body) {
            return of(inventoryItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InventoryItem());
  }
}

export const inventoryItemRoute: Routes = [
  {
    path: '',
    component: InventoryItemComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'bizioApp.inventoryItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InventoryItemDetailComponent,
    resolve: {
      inventoryItem: InventoryItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.inventoryItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InventoryItemUpdateComponent,
    resolve: {
      inventoryItem: InventoryItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.inventoryItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InventoryItemUpdateComponent,
    resolve: {
      inventoryItem: InventoryItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.inventoryItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
