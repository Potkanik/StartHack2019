import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFinalView } from 'app/shared/model/final-view.model';
import { FinalViewService } from './final-view.service';

@Component({
    selector: 'jhi-final-view-delete-dialog',
    templateUrl: './final-view-delete-dialog.component.html'
})
export class FinalViewDeleteDialogComponent {
    finalView: IFinalView;

    constructor(private finalViewService: FinalViewService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.finalViewService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'finalViewListModification',
                content: 'Deleted an finalView'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-final-view-delete-popup',
    template: ''
})
export class FinalViewDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ finalView }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FinalViewDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.finalView = finalView;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
