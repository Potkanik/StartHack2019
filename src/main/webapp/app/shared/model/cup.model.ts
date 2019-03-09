import { IHistory } from 'app/shared/model//history.model';

export const enum CupStatus {
    InStore = 'InStore',
    InUse = 'InUse',
    Recycled = 'Recycled',
    Lost = 'Lost'
}

export interface ICup {
    id?: number;
    qrCode?: string;
    status?: CupStatus;
    histories?: IHistory[];
    userCupLogin?: string;
    userCupId?: number;
}

export class Cup implements ICup {
    constructor(
        public id?: number,
        public qrCode?: string,
        public status?: CupStatus,
        public histories?: IHistory[],
        public userCupLogin?: string,
        public userCupId?: number
    ) {}
}
