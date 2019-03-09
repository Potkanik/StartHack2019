import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICup } from 'app/shared/model/cup.model';
import { CupService } from './cup.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-cup-update',
    templateUrl: './cup-update.component.html'
})
export class CupUpdateComponent implements OnInit {
    cup: ICup;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private cupService: CupService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cup }) => {
            this.cup = cup;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cup.id !== undefined) {
            this.subscribeToSaveResponse(this.cupService.update(this.cup));
        } else {
            this.subscribeToSaveResponse(this.cupService.create(this.cup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICup>>) {
        result.subscribe((res: HttpResponse<ICup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
