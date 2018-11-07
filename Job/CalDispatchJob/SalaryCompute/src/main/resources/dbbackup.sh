#!/usr/bin/env bash

FOLDER="/home/mysqlbackup/"
USER_NAME=root
PWD="ciicit!#%"
DB_NAME=gtobusinessdb
DB_HOST=172.16.9.53


#如果文件夹不存在，创建文件夹
#if [ ! -d "$FOLDER" ];then
    #mkdir -p $FOLDER
#fi

#备份数据库
mysqldump -u${USER_NAME} -p${PWD} -h ${DB_HOST} ${DB_NAME} | gzip > ${FOLDER}`date +%Y-%m-%d_%H%M%S`.sql.gz

#删除10天前的备份文件
#cd $FOLDER
#rm -rf `find /home/mysqlbackup -name '*.sql.gz' -mtime 10`

rm -rf `find /home/mysqlbackup -type d -name '*.sql.gz' -mtime 10`


# sudo chmod +x dbbackup.sh

# sudo  crontab -e

# 0 2 * * * /home/mysqlbackup/dbbackup.sh //每天 02:00 执行任务

# systemctl restart crond //重启服务