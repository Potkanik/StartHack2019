import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IHistory } from 'app/shared/model/history.model';
import { HistoryService } from './history.service';
import { ICup } from 'app/shared/model/cup.model';
import { CupService } from 'app/entities/cup';

@Component({
    selector: 'jhi-history-update',
    templateUrl: './history-update.component.html'
})
export class HistoryUpdateComponent implements OnInit {
    history: IHistory;
    isSaving: boolean;

    cups: ICup[];
    date: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private historyService: HistoryService,
        private cupService: CupService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ history }) => {
            this.history = history;
            this.date = this.history.date != null ? this.history.date.format(DATE_TIME_FORMAT) : null;
        });
        this.cupService.query().subscribe(
            (res: HttpResponse<ICup[]>) => {
                this.cups = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.history.date = this.date != null ? moment(this.date, DATE_TIME_FORMAT) : null;
        if (this.history.id !== undefined) {
            this.subscribeToSaveResponse(this.historyService.update(this.history));
        } else {
            this.subscribeToSaveResponse(this.historyService.create(this.history));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHistory>>) {
        result.subscribe((res: HttpResponse<IHistory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCupById(index: number, item: ICup) {
        return item.id;
    }
}
