import { PlayerType } from "../../model/player-type";

export interface JwtAuthenticationResponse {
    id: number,
    token: string, 
    username: string, 
    role: PlayerType
}
