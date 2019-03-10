import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { interval, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IFinalView } from 'app/shared/model/final-view.model';
import { Principal } from 'app/core';
import { FinalViewService } from './final-view.service';

@Component({
    selector: 'jhi-final-view',
    templateUrl: './final-view.component.html'
})
export class FinalViewComponent implements OnInit, OnDestroy {
    finalViews: IFinalView[];
    currentAccount: any;
    eventSubscriber: Subscription;
    subscription: Subscription;
    source = interval(1000);

    public pieChartLabels = ['InUse', 'Recycled', 'ReturnedByOther'];
    public pieChartData = [120, 150, 180];
    public pieChartType = 'pie';

    constructor(
        private finalViewService: FinalViewService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
        this.subscription = this.source.subscribe(val => this.loadAll());
    }

    loadAll() {
        this.finalViewService.query().subscribe(
            (res: HttpResponse<IFinalView[]>) => {
                this.finalViews = res.body;
                this.pieChartData = res.body[0].chartValues;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInFinalViews();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFinalView) {
        return item.id;
    }

    registerChangeInFinalViews() {
        this.eventSubscriber = this.eventManager.subscribe('finalViewListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
