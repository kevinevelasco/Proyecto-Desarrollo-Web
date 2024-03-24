import { Market } from "./market";
import { Product } from "./product";
import { Spacecraft } from "./spacecraft";
import { Star } from "./star";

export class Planet {
  constructor(
    public id: number,
    public name: string,
    public spacecrafts: Spacecraft[],
    public star: Star,
    public products: Product[],
    public markets: Market[]
  ){}
}
