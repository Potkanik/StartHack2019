/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { StartHack2019TestModule } from '../../../test.module';
import { CupUpdateComponent } from 'app/entities/cup/cup-update.component';
import { CupService } from 'app/entities/cup/cup.service';
import { Cup } from 'app/shared/model/cup.model';

describe('Component Tests', () => {
    describe('Cup Management Update Component', () => {
        let comp: CupUpdateComponent;
        let fixture: ComponentFixture<CupUpdateComponent>;
        let service: CupService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StartHack2019TestModule],
                declarations: [CupUpdateComponent]
            })
                .overrideTemplate(CupUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CupUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CupService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Cup(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cup = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Cup();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cup = entity;
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
