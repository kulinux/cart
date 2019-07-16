ID=`docker ps | grep -v CONTAINER | awk '{print $1}'`
docker exec -it $ID cqlsh

