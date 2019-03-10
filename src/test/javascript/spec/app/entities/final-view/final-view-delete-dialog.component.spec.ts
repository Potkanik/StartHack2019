/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { StartHack2019TestModule } from '../../../test.module';
import { FinalViewDeleteDialogComponent } from 'app/entities/final-view/final-view-delete-dialog.component';
import { FinalViewService } from 'app/entities/final-view/final-view.service';

describe('Component Tests', () => {
    describe('FinalView Management Delete Component', () => {
        let comp: FinalViewDeleteDialogComponent;
        let fixture: ComponentFixture<FinalViewDeleteDialogComponent>;
        let service: FinalViewService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StartHack2019TestModule],
                declarations: [FinalViewDeleteDialogComponent]
            })
                .overrideTemplate(FinalViewDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FinalViewDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FinalViewService);
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
