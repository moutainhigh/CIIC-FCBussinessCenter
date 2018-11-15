#!/usr/bin/env bash
# 微服务端口号数组
PORTS=(7035 7033 7034)
# 微服务名称数组
SERVICE_NAMES=(薪酬服务 薪酬计算同步任务 薪酬计算任务)
# 微服务jar包数组
JARS=(salarymanagementwebservice-host.jar salary-data-sync-task.jar salary-compute-task.jar)
# jar包路径
JAR_PATH='/home/pkgs'
# 日志路径
LOG_PATH='/home/logs'

ENV=dev

start() {
    SERVICE_NAME=
    JAR_NAME=
    command="$1"
    count=0
    okCount=0
    port=0
    for((i=0;i<${#SERVICE_NAMES[@]};i++))
    do
        SERVICE_NAME=${SERVICE_NAMES[$i]}
        JAR_NAME=${JARS[$i]}
        PORT=${PORTS[$i]}
        if [ "$command" == "all" ];then
            count=0
            PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
            if [ -n "$PID" ];then
                echo "$SERVICE_NAME:已经运行,PID=$PID"
            else
                #exec nohup java -Xms512m -Xmx512m -jar -Dspring.profiles.active=$ENV $JAR_PATH/$JAR_NAME >> $LOG_PATH/$SERVICE_NAME.log &
                java -Xms512m -Xmx512m -jar -Dspring.profiles.active=$ENV $JAR_PATH/$JAR_NAME > $LOG_PATH/$SERVICE_NAME.log 2>&1 &
                PID=`netstat -apn | grep $PORT | awk '{print $7}' | cut -d/ -f 1`
                while [ -z "$PID" ]
                do
                if (($count == 30));then
                    echo "$SERVICE_NAME:$(expr $count \* 10)秒内未启动,请检查!"
                    break
                fi
                count=$(($count+1))
                echo "${SERVICE_NAME}启动中.................."
                sleep 12s
                PID=`netstat -apn | grep $PORT | awk '{print $7}' | cut -d/ -f 1`
                done
                okCount=$(($okCount+1))
                echo "$SERVICE_NAME:已经启动成功,PID=$PID"
            fi
        fi
    done

    echo "............本次共启动:${okCount}个服务..........."

    }

   stop() {
    SERVICE_NAME=
    JAR_NAME=
    command="$1"
    okCount=0
   for((i=0;i<${#SERVICE_NAMES[@]};i++))
   do
       SERVICE_NAME=${SERVICE_NAMES[$i]}
       JAR_NAME=${JARS[$i]}
       if [ "$command" = "all" ];then
           PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
           if [ -n "$PID" ];then
               echo "$SERVICE_NAME:准备结束,PID=$PID"
               kill -9 $PID
               PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
               while [ -n "$PID" ]
               do
                sleep 3s
                PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
               done
               echo "$SERVICE_NAME:成功结束"
               okCount=$(($okCount+1))
           else
                echo "$SERVICE_NAME:未运行"
           fi
       fi
   done

   echo "............本次共停止:${okCount}个服务............"
}

case "$1" in
    start)
    start "$2"
    ;;

    stop)
    stop "$2"
    ;;

    restart)
    stop "$2"
    sleep 3s
    start "$2"
    ;;

  *)
    exit 1
  ;;
esac

