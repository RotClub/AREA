import { redirect } from "@sveltejs/kit";
import { config } from "dotenv";

config();

const apiKey = process.env.STEAM_API_KEY;
const steamId = process.env.STEAM_ID;

export async function fetchSteamProfile() {
    const url = `https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=${apiKey}&steamids=${steamId}`;
    
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Erreur ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        const playerProfile = data.response.players[0];
        console.log(playerProfile);
        return playerProfile;
    } catch (error) {
        console.error('Erreur fetching profile:', error);
        return null;
    }
}

export async function allGamesProfile() {
    const url = `https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=${apiKey}&steamid=${steamId}&include_appinfo=true&include_played_free_games=true`;
    
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Erreur ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        const games = data.response.games;
        console.log(games);
        return games;
    } catch (error) {
        console.error('Erreur fetching games:', error);
        return null;
    }
}

export async function GameSuccess(gameId: any) {
    const url = `https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=${gameId}&key=${apiKey}&steamid=${steamId}`;
    
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Erreur ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        console.log(data);
        return data;
    } catch (error) {
        console.error('Erreur fetching games:', error);
        return null;
    }
}

export async function StatusUtilisateur() {
    const url = `https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=${apiKey}&steamids=${steamId}`;
    
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Erreur ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        const StatusUtilisateur = data.response.players[0].profilestate;
        console.log(StatusUtilisateur);
        return StatusUtilisateur;
    } catch (error) {
        console.error('Erreur fetching profile:', error);
        return null;
    }
}

export async function FriendsList() {
    const url = `https://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=${apiKey}&steamid=${steamId}&relationship=friend`

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`Erreur ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        const FriendsList = data.friendslist.friends;
        console.log(FriendsList);
        return FriendsList;
    } catch (error) {
        console.error('Erreur fetching friends:', error);
        return null;
    }
}