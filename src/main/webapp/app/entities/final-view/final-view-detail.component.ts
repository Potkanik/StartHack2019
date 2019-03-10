import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinalView } from 'app/shared/model/final-view.model';

@Component({
    selector: 'jhi-final-view-detail',
    templateUrl: './final-view-detail.component.html'
})
export class FinalViewDetailComponent implements OnInit {
    finalView: IFinalView;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ finalView }) => {
            this.finalView = finalView;
        });
    }

    previousState() {
        window.history.back();
    }
}
