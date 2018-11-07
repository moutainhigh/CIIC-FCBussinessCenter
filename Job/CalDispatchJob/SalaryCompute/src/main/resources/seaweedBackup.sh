#!/usr/bin/env bash

DIR=/home/seaweedfs_backup
SUBDIR=`date +%Y-%m-%d_%H%M%S`
COMPOSE_DIR=$DIR/$SUBDIR
SEAWEED_MASTER_SERVER=172.16.9.19:9333

if [ ! -d "$COMPOSE_DIR" ];then
    sudo mkdir -p $COMPOSE_DIR
fi

for((i=1; i<7; i++))
do
    ./weed backup -dir=$COMPOSE_DIR -volumeId=$i -server=$SEAWEED_MASTER_SERVER
    sleep 1s
    echo "备份卷成功,volumeId=$i"
done

