import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IFinalView } from 'app/shared/model/final-view.model';
import { FinalViewService } from './final-view.service';

@Component({
    selector: 'jhi-final-view-update',
    templateUrl: './final-view-update.component.html'
})
export class FinalViewUpdateComponent implements OnInit {
    finalView: IFinalView;
    isSaving: boolean;

    constructor(private finalViewService: FinalViewService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ finalView }) => {
            this.finalView = finalView;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.finalView.id !== undefined) {
            this.subscribeToSaveResponse(this.finalViewService.update(this.finalView));
        } else {
            this.subscribeToSaveResponse(this.finalViewService.create(this.finalView));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFinalView>>) {
        result.subscribe((res: HttpResponse<IFinalView>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
