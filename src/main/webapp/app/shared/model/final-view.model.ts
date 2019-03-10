export interface IFinalView {
    id?: number;
    name?: string;
    money?: number;
    countOfCups?: number;
    countOfReturnedCups?: number;
    countOfReturnedCupsByOthers?: number;
}

export class FinalView implements IFinalView {
    constructor(
        public id?: number,
        public name?: string,
        public money?: number,
        public countOfCups?: number,
        public countOfReturnedCups?: number,
        public countOfReturnedCupsByOthers?: number
    ) {}
}
