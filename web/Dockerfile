FROM node:lts-buster AS builder

WORKDIR /build

COPY . .

RUN npm install

RUN npm run build


FROM node:lts-buster AS vercel

WORKDIR /app

# Copy the build files and node_modules for Vercel
COPY --from=builder /build/vercel.json .
COPY --from=builder /build/.vercel .
COPY --from=builder /build/node_modules ./node_modules
COPY --from=builder /build/package*.json .

# Expose 24678 for Vite's HMR and 8081 for the server
EXPOSE 24678
EXPOSE 8081

CMD npx vercel dev --token=$VERCEL_TOKEN --yes


FROM node:lts-buster AS node_runner

WORKDIR /app

# Copy the build files and node_modules
COPY --from=builder /build/build ./build
COPY --from=builder /build/node_modules ./node_modules
COPY --from=builder /build/package*.json .

# Expose 24678 for Vite's HMR and 8081 for the server
EXPOSE 24678
EXPOSE 8081

CMD npm run preview -- --port 8081 --host