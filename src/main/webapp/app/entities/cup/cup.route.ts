import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Cup } from 'app/shared/model/cup.model';
import { CupService } from './cup.service';
import { CupComponent } from './cup.component';
import { CupDetailComponent } from './cup-detail.component';
import { CupUpdateComponent } from './cup-update.component';
import { CupDeletePopupComponent } from './cup-delete-dialog.component';
import { ICup } from 'app/shared/model/cup.model';

@Injectable({ providedIn: 'root' })
export class CupResolve implements Resolve<ICup> {
    constructor(private service: CupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Cup> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Cup>) => response.ok),
                map((cup: HttpResponse<Cup>) => cup.body)
            );
        }
        return of(new Cup());
    }
}

export const cupRoute: Routes = [
    {
        path: 'cup',
        component: CupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.cup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cup/:id/view',
        component: CupDetailComponent,
        resolve: {
            cup: CupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.cup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cup/new',
        component: CupUpdateComponent,
        resolve: {
            cup: CupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.cup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cup/:id/edit',
        component: CupUpdateComponent,
        resolve: {
            cup: CupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.cup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cupPopupRoute: Routes = [
    {
        path: 'cup/:id/delete',
        component: CupDeletePopupComponent,
        resolve: {
            cup: CupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.cup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
