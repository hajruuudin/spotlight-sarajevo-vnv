FROM ubuntu:latest
LABEL authors="hajrudin.imamovic"

# Stage 1: Installing node, getting the files and dependencies
# This is the first step and it builds the angular application.
# Node 20 is the version we are using and it is only used to get the files and stuff
# Also labeled as BUILD command
FROM node:20-alpine AS build

# Set working directory. SO everything within the /app folder is where the
# files are.
WORKDIR /app

# Copy package.json and package-lock.json first (caching npm install)
COPY package*.json ./

# Install dependencies that are labeled
RUN npm install

# Copy the rest of the source code
COPY . .

# Build the Angular app in production mode. This will create the dist folder and 
# then make all the files which browsers can read within /app/dist/<app-name>/browser
# The browser folder added after Angular 17.
RUN npm run build -- --configuration production

# Stage 2: Serve with NGINX.
# NGINX is the server / thing that serves the files from our app to the browser.
# After we are done wiht the dependencies we do not need node anymore, so now
# we just focus on nginx.
FROM nginx:alpine

# Copy built Angular files from Stage 1. SO everything that we just made
COPY --from=build /app/dist/spotlight-sarajevo-fe/browser /usr/share/nginx/html

# Expose port 80. By default this means the app is on HTTP, but because deploymeny
# is done on render, the traffic is automatically treated as HTTPS
# In the case of revealing port 443 for HTTPS, we'd have to get everything set up
# with .crt and .tls files, security keys and a whole bunch of stuff for local HTTPS
# Not worth it for now, might need it later if switching to AWS
EXPOSE 80

# Start NGINX server. So finnaly once everything is done with the setup
# the server can be started up.
# CMD just means this should be the command that runs at the end of the dockerfile
# The flag -g means it passes a global configuration directive to NGINX.
# THe "daemon off" means it keeps NGINX running in the foreground, instead of forking to the background.
# CRUCIAL as without this afterwards after the command is run the container just shuts down
CMD ["nginx", "-g", "daemon off;"]
