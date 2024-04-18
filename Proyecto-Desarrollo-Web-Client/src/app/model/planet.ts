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
    public markets: Market[],
    public position: number,
    public ring: boolean,
    public size: number,
    public texture: number,
    public character: number,
    public animation: number
  ){}
}
