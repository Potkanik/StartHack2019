import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StartHack2019SharedModule } from 'app/shared';
import { StartHack2019AdminModule } from 'app/admin/admin.module';
import {
    CupComponent,
    CupDetailComponent,
    CupUpdateComponent,
    CupDeletePopupComponent,
    CupDeleteDialogComponent,
    cupRoute,
    cupPopupRoute
} from './';

const ENTITY_STATES = [...cupRoute, ...cupPopupRoute];

@NgModule({
    imports: [StartHack2019SharedModule, StartHack2019AdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CupComponent, CupDetailComponent, CupUpdateComponent, CupDeleteDialogComponent, CupDeletePopupComponent],
    entryComponents: [CupComponent, CupUpdateComponent, CupDeleteDialogComponent, CupDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StartHack2019CupModule {}
