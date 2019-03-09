/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StartHack2019TestModule } from '../../../test.module';
import { CupDetailComponent } from 'app/entities/cup/cup-detail.component';
import { Cup } from 'app/shared/model/cup.model';

describe('Component Tests', () => {
    describe('Cup Management Detail Component', () => {
        let comp: CupDetailComponent;
        let fixture: ComponentFixture<CupDetailComponent>;
        const route = ({ data: of({ cup: new Cup(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [StartHack2019TestModule],
                declarations: [CupDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CupDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CupDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.cup).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
