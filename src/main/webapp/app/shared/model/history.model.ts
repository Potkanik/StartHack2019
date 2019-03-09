import { Moment } from 'moment';

export const enum CupAction {
    Created = 'Created',
    Taken = 'Taken',
    Returned = 'Returned',
    Lost = 'Lost'
}

export interface IHistory {
    id?: number;
    action?: CupAction;
    date?: Moment;
    kupId?: number;
}

export class History implements IHistory {
    constructor(public id?: number, public action?: CupAction, public date?: Moment, public kupId?: number) {}
}
