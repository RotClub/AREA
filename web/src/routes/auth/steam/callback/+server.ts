export const GET = async ({ url }) => {
	try {
		const openidParams = url.searchParams;
		const claimedId = openidParams.get("openid.claimed_id");

		if (claimedId && claimedId.includes("https://steamcommunity.com/openid/id/")) {
			const steamId = claimedId.replace("https://steamcommunity.com/openid/id/", "");
			console.log("SteamID de l'utilisateur:", steamId);

			return new Response(null, {
				status: 302,
				headers: {
					Location: "/auth/steam?connected=true"
				}
			});
		} else {
			console.log("Claimed ID is invalid or not from Steam.");
			return new Response(null, {
				status: 302,
				headers: {
					Location: "/auth/steam?connected=false"
				}
			});
		}
	} catch (error) {
		console.error("Erreur lors du callback Steam:", error);
		return new Response(
			JSON.stringify({
				error: error
			}),
			{
				status: 500,
				headers: {
					"Content-Type": "application/json"
				}
			}
		);
	}
};
