import { IClient } from 'app/shared/model/client.model';

export interface IContract {
  id?: number;
  identifier?: string;
  client?: IClient;
}

export class Contract implements IContract {
  constructor(public id?: number, public identifier?: string, public client?: IClient) {}
}
