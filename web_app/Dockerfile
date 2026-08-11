# --- Build stage ---
FROM node:20-alpine AS build

WORKDIR /build

COPY web_app/package*.json ./
RUN npm ci

COPY web_app/ .
RUN npm run build

# --- Run stage ---
FROM nginx:alpine

COPY --from=build /build/dist /usr/share/nginx/html

EXPOSE 80