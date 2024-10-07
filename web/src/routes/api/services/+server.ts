import { json, redirect } from '@sveltejs/kit';
import { Strategy as SteamStrategy } from 'passport-steam';
import passport from 'passport';
import dotenv from 'dotenv';
import type { RequestEvent } from '@sveltejs/kit';

dotenv.config();

passport.use(new SteamStrategy({
  returnURL: process.env.STEAM_RETURN_URL || '',
  realm: process.env.STEAM_REALM || '',
  apiKey: process.env.STEAM_API_KEY || ''
  },
  (identifier: string, profile: any, done: any) => {
  return done(null, profile);
  }
));

passport.serializeUser((user, done) => {
  done(null, user);
});

passport.deserializeUser((obj, done) => {
  done(null, obj);
});

export const GET = async ({ request, locals }: RequestEvent) => {
  const url = new URL(request.url);
  if (url.pathname === '/auth/steam') {
  return passport.authenticate('steam')(request, locals);
  } else if (url.pathname === '/auth/steam/return') {
  return passport.authenticate('steam', { failureRedirect: '/' })(request, locals, () => {
    return redirect(302, '/profile');
  });
  } else if (url.pathname === '/profile') {
  if (/*locals.user*/) {
    return json({ message: `Logged in as ${/*locals.user.displayName*/}` });
  } else {
    return redirect(302, '/');
  }
  }
  return json({ message: 'Not Found' }, { status: 404 });
};
