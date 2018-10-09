#!/usr/bin/env bash
# 端口号
PORTS=(8084 8083 8085 8086)
# 系统模块
MODULES=(salary system user act)
# 系统模块名称
MODULE_NAMES=(薪酬管理 系统模块 用户模块 活动模块)
# jar包数组
JARS=(salarymanagementwebservice-host.jar system-1.0.1-SNAPSHOT.jar user-1.0.1-SNAPSHOT.jar act-1.0.1-SNAPSHOT.jar)
# jar包路径
JAR_PATH='/usr/local/beta'
# 日志路径
LOG_PATH='/usr/local/beta'
start() {
    MODULE=
    MODULE_NAME=
    JAR_NAME=
    command="$1"
    commandOk=0
    count=0
    okCount=0
    port=0
    for((i=0;i<${#MODULES[@]};i++))
    do
        MODULE=${MODULES[$i]}
        MODULE_NAME=${MODULE_NAMES[$i]}
        JAR_NAME=${JARS[$i]}
        PORT=${PORTS[$i]}
        if [ "$command" == "all" ] || [ "$command" == "$MODULE" ];then
            commandOk=1
            count=0
            PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
            if [ -n "$PID" ];then
                echo "$MODULE---$MODULE_NAME:已经运行,PID=$PID"
            else
                exec nohup java -jar $JAR_PATH/$JAR_NAME >> $LOG_PATH/nohup.out &
                PID=`netstat -apn | grep $PORT | awk '{print $7}' | cut -d/ -f 1`
                while [ -z "$PID" ]
                do
                if (($count == 30));then
                    echo "$MODULE---$MODULE_NAME:$(expr $count \* 10)秒内未启动,请检查!"
                    break
                fi
                count=$(($count+1))
                echo "${MODULE_NAME}启动中.................."
                sleep 10s
                PID=`netstat -apn | grep $PORT | awk '{print $7}' | cut -d/ -f 1`
                done
                okCount=$(($okCount+1))
                echo "$MODULE---$MODULE_NAME:已经启动成功,PID=$PID"
            fi
        fi
        done
        if(($commandOk == 0));then
            echo "第二个参数请输入:mcht|system|user|act"
        else
            echo "............本次共启动:${okCount}个服务..........."
        fi
    }

   stop() {
    MODULE=
    MODULE_NAME=
    JAR_NAME=
    command="$1"
    commandOk=0
    okCount=0
   for((i=0;i<${#MODULES[@]};i++))
   do
       MODULE=${MODULES[$i]}
       MODULE_NAME=${MODULE_NAMES[$i]}
       JAR_NAME=${JARS[$i]}
       if [ "$command" = "all" ] || [ "$command" = "$MODULE" ];then
           commandOk=1
           PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
           if [ -n "$PID" ];then
               echo "$MODULE---$MODULE_NAME:准备结束,PID=$PID"
               kill -9 $PID
               PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
               while [ -n "$PID" ]
               do
                sleep 3s
                PID=`ps -ef |grep $(echo $JAR_NAME | awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`
               done
               echo "$MODULE---$MODULE_NAME:成功结束"
               okCount=$(($okCount+1))
           else
                echo "$MODULE---$MODULE_NAME:未运行"
           fi
      fi
 done
 if (($commandOk == 0));then
    echo "第二个参数请输入:mcht|system|user|act"
 else
    echo "............本次共停止:${okCount}个服务............"
 fi
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
    echo "第一个参数请输入:start|stop|restart"
    exit 1
  ;;
esac

