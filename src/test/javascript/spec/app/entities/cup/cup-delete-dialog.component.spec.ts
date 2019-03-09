/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { StartHack2019TestModule } from '../../../test.module';
import { CupDeleteDialogComponent } from 'app/entities/cup/cup-delete-dialog.component';
import { CupService } from 'app/entities/cup/cup.service';

describe('Component Tests', () => {
    describe('Cup Management Delete Component', () => {
        let comp: CupDeleteDialogComponent;
        let fixture: ComponentFixture<CupDeleteDialogComponent>;
        let service: CupService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StartHack2019TestModule],
                declarations: [CupDeleteDialogComponent]
            })
                .overrideTemplate(CupDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CupDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CupService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
