/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { StartHack2019TestModule } from '../../../test.module';
import { FinalViewComponent } from 'app/entities/final-view/final-view.component';
import { FinalViewService } from 'app/entities/final-view/final-view.service';
import { FinalView } from 'app/shared/model/final-view.model';

describe('Component Tests', () => {
    describe('FinalView Management Component', () => {
        let comp: FinalViewComponent;
        let fixture: ComponentFixture<FinalViewComponent>;
        let service: FinalViewService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StartHack2019TestModule],
                declarations: [FinalViewComponent],
                providers: []
            })
                .overrideTemplate(FinalViewComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FinalViewComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FinalViewService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new FinalView(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.finalViews[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
