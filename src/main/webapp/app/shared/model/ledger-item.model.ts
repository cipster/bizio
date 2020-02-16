import { Moment } from 'moment';
import { IClient } from 'app/shared/model/client.model';
import { LedgerType } from 'app/shared/model/enumerations/ledger-type.model';

export interface ILedgerItem {
  id?: number;
  date?: Moment;
  document?: string;
  explanation?: string;
  type?: LedgerType;
  value?: number;
  year?: number;
  client?: IClient;
}

export class LedgerItem implements ILedgerItem {
  constructor(
    public id?: number,
    public date?: Moment,
    public document?: string,
    public explanation?: string,
    public type?: LedgerType,
    public value?: number,
    public year?: number,
    public client?: IClient
  ) {}
}
