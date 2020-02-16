import { ILedgerItem } from 'app/shared/model/ledger-item.model';
import { IContract } from 'app/shared/model/contract.model';

export interface IClient {
  id?: number;
  name?: string;
  ledgerItems?: ILedgerItem[];
  contracts?: IContract[];
}

export class Client implements IClient {
  constructor(public id?: number, public name?: string, public ledgerItems?: ILedgerItem[], public contracts?: IContract[]) {}
}
