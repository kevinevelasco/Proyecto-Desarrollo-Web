import { Product } from "./product";
import { Spacecraft } from "./spacecraft";

export class Inventory {
  constructor(
    public id: number,
    public spacecraft: Spacecraft,
    public product: Product,
    private quantity: number
  ){}
}
