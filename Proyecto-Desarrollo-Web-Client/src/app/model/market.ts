import { Planet } from './planet';
import { Product } from './product';
export class Market {
  constructor(
    public id: number,
    public planet: Planet,
    public product: Product,
    public stock: number,
    public demandFactor: number,
    public supplyFactor: number,
    public buyPrice: number,
    public sellPrice: number
  ){}
}
