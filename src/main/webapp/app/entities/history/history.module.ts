import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StartHack2019SharedModule } from 'app/shared';
import {
    HistoryComponent,
    HistoryDetailComponent,
    HistoryUpdateComponent,
    HistoryDeletePopupComponent,
    HistoryDeleteDialogComponent,
    historyRoute,
    historyPopupRoute
} from './';

const ENTITY_STATES = [...historyRoute, ...historyPopupRoute];

@NgModule({
    imports: [StartHack2019SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HistoryComponent,
        HistoryDetailComponent,
        HistoryUpdateComponent,
        HistoryDeleteDialogComponent,
        HistoryDeletePopupComponent
    ],
    entryComponents: [HistoryComponent, HistoryUpdateComponent, HistoryDeleteDialogComponent, HistoryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StartHack2019HistoryModule {}
