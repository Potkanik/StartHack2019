import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FinalView, IFinalView } from 'app/shared/model/final-view.model';
import { FinalViewService } from './final-view.service';
import { FinalViewComponent } from './final-view.component';
import { FinalViewDetailComponent } from './final-view-detail.component';
import { FinalViewUpdateComponent } from './final-view-update.component';
import { FinalViewDeletePopupComponent } from './final-view-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class FinalViewResolve implements Resolve<IFinalView> {
    constructor(private service: FinalViewService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<FinalView> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<FinalView>) => response.ok),
                map((finalView: HttpResponse<FinalView>) => finalView.body)
            );
        }
        return of(new FinalView());
    }
}

export const finalViewRoute: Routes = [
    {
        path: 'final-view',
        component: FinalViewComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.finalView.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'final-view/:id/view',
        component: FinalViewDetailComponent,
        resolve: {
            finalView: FinalViewResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.finalView.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'final-view/new',
        component: FinalViewUpdateComponent,
        resolve: {
            finalView: FinalViewResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.finalView.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'final-view/:id/edit',
        component: FinalViewUpdateComponent,
        resolve: {
            finalView: FinalViewResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.finalView.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const finalViewPopupRoute: Routes = [
    {
        path: 'final-view/:id/delete',
        component: FinalViewDeletePopupComponent,
        resolve: {
            finalView: FinalViewResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'startHack2019App.finalView.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
