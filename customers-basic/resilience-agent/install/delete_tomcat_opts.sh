TOMCAT_FOLDER_MASK=$1;
if [ "${TOMCAT_FOLDER_MASK}" == "" ]; then
	echo "TOMCAT_FOLDER_MASK cannot be empty";
	exit
fi
OPTION='CATALINA_OPTS="$CATALINA_OPTS -javaagent:$CATALINA_HOME/../applications/resilience-agent/lib/agent.jar"';
FILES=`ls $TOMCAT_FOLDER_MASK/bin/setenv.sh 2>/dev/null`
for file in $FILES
do
	AGENT_EXISTS=`cat $file | grep -c 'resilience-agent'`;
	if [ $AGENT_EXISTS -gt 0 ];
	then
		echo "Removing $OPTION from $file"
		sed -i -e ':a' -e 'N' -e '$!ba' -e "s|\n$OPTION||g" $file;
	fi;
done;
