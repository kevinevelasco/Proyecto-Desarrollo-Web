import { Inventory } from "./inventory";
import { Market } from "./market";
import { Planet } from "./planet";

export class Product {
  constructor(
    public id: number,
    public size: number,
    public name: string,
    public planets: Planet[],
    public markets: Market[],
    public inventories: Inventory[]
  ){}
}
