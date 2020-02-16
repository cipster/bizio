import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IContract, Contract } from 'app/shared/model/contract.model';
import { ContractService } from './contract.service';
import { ContractComponent } from './contract.component';
import { ContractDetailComponent } from './contract-detail.component';
import { ContractUpdateComponent } from './contract-update.component';

@Injectable({ providedIn: 'root' })
export class ContractResolve implements Resolve<IContract> {
  constructor(private service: ContractService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContract> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((contract: HttpResponse<Contract>) => {
          if (contract.body) {
            return of(contract.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Contract());
  }
}

export const contractRoute: Routes = [
  {
    path: '',
    component: ContractComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'bizioApp.contract.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContractDetailComponent,
    resolve: {
      contract: ContractResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.contract.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContractUpdateComponent,
    resolve: {
      contract: ContractResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.contract.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContractUpdateComponent,
    resolve: {
      contract: ContractResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'bizioApp.contract.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
