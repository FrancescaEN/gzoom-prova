#     ______________________
#    / Build artifact Stage \
#  _/________________________\_______________________________________
# /                                                                  \
FROM  node:18.20.2-buster-slim AS builder

COPY app/ /app
WORKDIR /app 
RUN npm install -g npm@10.8.2
RUN npm install -g @angular/cli@17.3.5
RUN npm install
RUN ng build --base-href /gzoom2/ --configuration production
# \                                                                  /
#  ------------------------------------------------------------------

#     ___________________
#    / Build Image Stage \
#  _/_____________________\__________________________________________
# /                                                                  \
FROM  nginx:1.27
COPY --from=builder /app/dist /usr/share/nginx/html
COPY docker/nginx.conf /etc/nginx/nginx.conf

# \                                                                  /
#  ------------------------------------------------------------------
