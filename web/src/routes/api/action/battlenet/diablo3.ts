const API_BASE_URL = 'https://eu.api.blizzard.com/d3/profile';

export const getDiablo3Profile = async (battleTag: string, accessToken: string) => {
    const formattedBattleTag = battleTag.replace('#', '-');
    console.log('formattedBattleTag', formattedBattleTag);
    console.log('accessToken', accessToken);
    console.log('API_BASE_URL', API_BASE_URL);
    const url = `${API_BASE_URL}/${formattedBattleTag}/?locale=fr_FR&access_token=${accessToken}`;
    console.log('url', url);

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error fetching D3 profile: ${response.status} ${response.statusText} - ${errorText}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching D3 profile:', error);
        throw error;
    }
};
