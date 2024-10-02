import { redirect } from '@sveltejs/kit';

export const actions = {
  login: async ({ request }) => {
    throw redirect(302, '/auth/steam');
  }
};
