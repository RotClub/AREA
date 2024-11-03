import { Nodes } from "$lib/services";

export const GET = async (event) => {
	const accessibleServices = Nodes.map((node) => {
		return {
			name: node.displayName,
			actions: node.actions.map((action) => {
				return {
					name: action.id,
					description: action.displayName
				};
			}),
			reactions: node.reactions.map((reaction) => {
				return {
					name: reaction.id,
					description: reaction.displayName
				};
			})
		};
	});
	const data = {
		client: {
			host: event.getClientAddress()
		},
		server: {
			current_time: Date.now(),
			services: accessibleServices
		}
	};

	return new Response(JSON.stringify(data), {
		status: 200,
		headers: {
			"Content-Type": "application/json"
		}
	});
};
