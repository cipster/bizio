export interface IInventoryItem {
  id?: number;
  name?: string;
  value?: number;
}

export class InventoryItem implements IInventoryItem {
  constructor(public id?: number, public name?: string, public value?: number) {}
}
