FROM node:lts-buster

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

CMD npm run preview