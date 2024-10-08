export const GET = async () => {
	return new Response(
		JSON.stringify({
			" client ": {
				" host ": "10.101.53.35"
			},
			" server ": {
				" current_time ": 1531680780,
				" services ": [
					{
						" name ": " facebook ",
						" actions ": [
							{
								" name ": " new_message_in_group ",
								" description ": " A new message is posted in the group "
							},
							{
								" name ": " new_message_inbox ",
								" description ": " A new private message is received by the user "
							},
							{
								" name ": " new_like ",
								" description ":
									" The user gains a like from one of their messages "
							}
						],
						" reactions ": [
							{
								" name ": " like_message ",
								" description ": " The user likes a message "
							}
						]
					}
				]
			}
		}),
		{
			status: 200,
			headers: {
				"Content-Type": "application/json"
			}
		}
	);
};
