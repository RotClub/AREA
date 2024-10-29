FROM node:lts-buster AS vercel_runner

WORKDIR /app

COPY . .

ARG VERCEL_TOKEN

# Install Vercel CLI
RUN npm install --global vercel@latest

# Link the project to Vercel
RUN npm run link_vercel

# Pull the project settings from Vercel
RUN vercel pull --yes --environment=preview --token=${VERCEL_TOKEN}

# Set the environment to preview
RUN npm run env:preview

# Install dependencies
RUN npm install

# Remove rootDirectory from project.json to avoid Vercel build error
RUN sed -i.bak '/rootDirectory/d' .vercel/project.json

# Build the project
RUN vercel build --yes --token=${VERCEL_TOKEN}

# Expose 24678 for Vite's HMR and 8081 for the server
EXPOSE 24678
EXPOSE 4173

# Run the project
CMD npm run preview


FROM node:lts-buster AS node_runner

WORKDIR /app

COPY . .

ARG VERCEL_TOKEN

# Install Vercel CLI
RUN npm install --global vercel@latest

# Link the project to Vercel
RUN npm run link_vercel

# Set the environment to preview
RUN npm run env:preview

# Install dependencies
RUN npm install

# Build the project
RUN npm run build

# Expose 24678 for Vite's HMR and 3000 for the server
EXPOSE 24678
EXPOSE 4173

# Run the project
CMD npm run preview