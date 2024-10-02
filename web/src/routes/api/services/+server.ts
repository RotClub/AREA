import express from 'express';
import passport from 'passport';
import { Strategy as SteamStrategy } from 'passport-steam';
import session from 'express-session';
import dotenv from 'dotenv';

dotenv.config();

const app = express();

app.use(session({
  secret: 'your_secret',
  resave: false,
  saveUninitialized: true,
}));

passport.use(new SteamStrategy({
    returnURL: process.env.STEAM_RETURN_URL,
    realm: process.env.STEAM_REALM,
    apiKey: process.env.STEAM_API_KEY
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

app.use(passport.initialize());
app.use(passport.session());

app.get('/auth/steam', passport.authenticate('steam'));

app.get('/auth/steam/return', passport.authenticate('steam', { failureRedirect: '/' }),
  (req, res) => {
    res.redirect('/profile');
  }
);

app.get('/profile', (req, res) => {
  if (req.isAuthenticated()) {
    res.send(`Logged in as ${req.user.displayName}`);
  } else {
    res.redirect('/');
  }
});
