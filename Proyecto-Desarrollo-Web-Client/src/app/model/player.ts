import { PlayerType } from "./player-type";
import { Spacecraft } from "./spacecraft";

export class Player {
  constructor(
    public id: number,
    public userName: string,
    public password: string,
    public type: PlayerType,
    public spacecraft?: Spacecraft
  ){}
}
