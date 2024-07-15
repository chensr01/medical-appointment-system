FROM nginx

RUN apt update && apt install nodejs npm -y

WORKDIR /usr/src/app
COPY ./ /usr/src/app

# Install the dependencies and build the app
RUN npm install
RUN npm run build

EXPOSE 80

# Copy the build folder to the nginx server
COPY ./build /usr/share/nginx/html
