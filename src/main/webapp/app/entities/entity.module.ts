import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { StartHack2019HistoryModule } from './history/history.module';
import { StartHack2019CupModule } from './cup/cup.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        StartHack2019HistoryModule,
        StartHack2019CupModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StartHack2019EntityModule {}
