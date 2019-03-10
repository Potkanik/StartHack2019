import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StartHack2019SharedModule } from 'app/shared';
import {
    FinalViewComponent,
    FinalViewDeleteDialogComponent,
    FinalViewDeletePopupComponent,
    FinalViewDetailComponent,
    finalViewPopupRoute,
    finalViewRoute,
    FinalViewUpdateComponent
} from './';
import { ChartsModule } from 'ng2-charts';
import { BrowserModule } from '@angular/platform-browser';

const ENTITY_STATES = [...finalViewRoute, ...finalViewPopupRoute];

@NgModule({
    imports: [StartHack2019SharedModule, RouterModule.forChild(ENTITY_STATES), ChartsModule, BrowserModule],
    declarations: [
        FinalViewComponent,
        FinalViewDetailComponent,
        FinalViewUpdateComponent,
        FinalViewDeleteDialogComponent,
        FinalViewDeletePopupComponent
    ],
    entryComponents: [FinalViewComponent, FinalViewUpdateComponent, FinalViewDeleteDialogComponent, FinalViewDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StartHack2019FinalViewModule {}
