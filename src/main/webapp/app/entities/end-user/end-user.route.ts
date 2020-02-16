import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEndUser, EndUser } from 'app/shared/model/end-user.model';
import { EndUserService } from './end-user.service';
import { EndUserComponent } from './end-user.component';
import { EndUserDetailComponent } from './end-user-detail.component';
import { EndUserUpdateComponent } from './end-user-update.component';

@Injectable({ providedIn: 'root' })
export class EndUserResolve implements Resolve<IEndUser> {
  constructor(private service: EndUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEndUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((endUser: HttpResponse<EndUser>) => {
          if (endUser.body) {
            return of(endUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EndUser());
  }
}

export const endUserRoute: Routes = [
  {
    path: '',
    component: EndUserComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EndUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EndUserDetailComponent,
    resolve: {
      endUser: EndUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EndUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EndUserUpdateComponent,
    resolve: {
      endUser: EndUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EndUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EndUserUpdateComponent,
    resolve: {
      endUser: EndUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EndUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];
