import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICup } from 'app/shared/model/cup.model';

@Component({
    selector: 'jhi-cup-detail',
    templateUrl: './cup-detail.component.html'
})
export class CupDetailComponent implements OnInit {
    cup: ICup;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cup }) => {
            this.cup = cup;
        });
    }

    previousState() {
        window.history.back();
    }
}
