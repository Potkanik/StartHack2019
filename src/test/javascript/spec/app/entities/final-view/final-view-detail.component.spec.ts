/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StartHack2019TestModule } from '../../../test.module';
import { FinalViewDetailComponent } from 'app/entities/final-view/final-view-detail.component';
import { FinalView } from 'app/shared/model/final-view.model';

describe('Component Tests', () => {
    describe('FinalView Management Detail Component', () => {
        let comp: FinalViewDetailComponent;
        let fixture: ComponentFixture<FinalViewDetailComponent>;
        const route = ({ data: of({ finalView: new FinalView(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StartHack2019TestModule],
                declarations: [FinalViewDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FinalViewDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FinalViewDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.finalView).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
