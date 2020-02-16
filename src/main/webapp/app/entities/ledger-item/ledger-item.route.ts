import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILedgerItem, LedgerItem } from 'app/shared/model/ledger-item.model';
import { LedgerItemService } from './ledger-item.service';
import { LedgerItemComponent } from './ledger-item.component';
import { LedgerItemDetailComponent } from './ledger-item-detail.component';
import { LedgerItemUpdateComponent } from './ledger-item-update.component';

@Injectable({ providedIn: 'root' })
export class LedgerItemResolve implements Resolve<ILedgerItem> {
  constructor(private service: LedgerItemService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILedgerItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ledgerItem: HttpResponse<LedgerItem>) => {
          if (ledgerItem.body) {
            return of(ledgerItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LedgerItem());
  }
}

export const ledgerItemRoute: Routes = [
  {
    path: '',
    component: LedgerItemComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.ledgerItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LedgerItemDetailComponent,
    resolve: {
      ledgerItem: LedgerItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.ledgerItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LedgerItemUpdateComponent,
    resolve: {
      ledgerItem: LedgerItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.ledgerItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LedgerItemUpdateComponent,
    resolve: {
      ledgerItem: LedgerItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.ledgerItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
