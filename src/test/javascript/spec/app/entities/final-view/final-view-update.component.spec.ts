/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { StartHack2019TestModule } from '../../../test.module';
import { FinalViewUpdateComponent } from 'app/entities/final-view/final-view-update.component';
import { FinalViewService } from 'app/entities/final-view/final-view.service';
import { FinalView } from 'app/shared/model/final-view.model';

describe('Component Tests', () => {
    describe('FinalView Management Update Component', () => {
        let comp: FinalViewUpdateComponent;
        let fixture: ComponentFixture<FinalViewUpdateComponent>;
        let service: FinalViewService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StartHack2019TestModule],
                declarations: [FinalViewUpdateComponent]
            })
                .overrideTemplate(FinalViewUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FinalViewUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FinalViewService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new FinalView(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.finalView = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new FinalView();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.finalView = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
