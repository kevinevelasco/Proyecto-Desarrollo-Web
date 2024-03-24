import { Inventory } from "./inventory";
import { Planet } from "./planet";
import { Player } from "./player";
import { SpacecraftModel } from "./spacecraft-model";

export class Spacecraft {
  constructor(
  public id: number,
  public name: string, 
  public credit: number,
  public totalTime: number,
  public players : Player[],
  public planet: Planet,
  public spacecraftModel: SpacecraftModel,
  public inventories: Inventory[]
  ){}
}
