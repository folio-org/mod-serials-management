## This script logs into OKAPI and then registers and enables the module
BASEDIR=$(dirname "$0")

MODULE_PORT=8076

# This script generates the module descriptor for mod-rs and posts it to a secured OKAPI control interface
# the script is controlled by a ~/.okapirc file where you need to specify the supertenant username (ST_UN)
# supertnent password (ST_PW) and the OKAPI_URL (For the rancher-desktop install, likely http://localhost:30100)

if [ -f .okapirc ]; then
  . .okapirc
elif [ -f $HOME/.okapirc ]; then
  . $HOME/.okapirc
else
  echo You must configure \$HOME/.okapirc
  echo export IS_SECURE_SUPERTENANT=Y
  echo export ST_UN=sysadm
  echo export ST_PW=PASSWORD_FROM_LOCAL_okapi_commander_cfg.json
  echo export OKAPI_URL=http://localhost:30100
  exit 0
fi

echo $BASEDIR
pushd "$BASEDIR/../service"

DIR="$BASEDIR/../"

echo "Using directory: $DIR"

# Check for decriptor target directory.

DESCRIPTORDIR="../service/build/resources/main/okapi"

if [ ! -d "$DESCRIPTORDIR" ]; then
    echo "No descriptors found. Let's try building them."
    
    ./gradlew generateDescriptors
fi

# DEP_DESC=`cat ${DESCRIPTORDIR}/DeploymentDescriptor.json | jq -c ".url=\"$2\""`
DEP_DESC=`cat ${DESCRIPTORDIR}/DeploymentDescriptor.json | jq -c ".url=\"http://192.168.5.2:$MODULE_PORT/\""`
SVC_ID=`echo $DEP_DESC | jq -rc '.srvcId'`
INS_ID=`echo $DEP_DESC | jq -rc '.instId'`

AUTH_TOKEN=`../scripts/okapi-login -u $ST_UN -p $ST_PW -t supertenant`

echo "AUTH_TOKEN: $AUTH_TOKEN"
echo "SUPERTENANT UN: $ST_UN, PASSWORD: $ST_PW"

echo Remove any existing module ${SVC_ID}/${INS_ID}
echo Waiting for curl -XDELETE "${OKAPI_URL}/_/proxy/tenants/${TENANT_NAME}/modules/${SVC_ID}"
curl -XDELETE -H "X-Okapi-Token: $AUTH_TOKEN" "${OKAPI_URL}/_/proxy/tenants/${TENANT_NAME}/modules/${SVC_ID}"
echo

echo Waiting for curl -XDELETE -H "X-Okapi-Token: $AUTH_TOKEN" "${OKAPI_URL}/_/discovery/modules/${SVC_ID}/${INS_ID}"
curl -XDELETE -H "X-Okapi-Token: $AUTH_TOKEN" "${OKAPI_URL}/_/discovery/modules/${SVC_ID}/${INS_ID}"
echo

echo Waiting for curl -XDELETE -H "X-Okapi-Token: $AUTH_TOKEN" "${OKAPI_URL}/_/proxy/modules/${SVC_ID}"
curl -XDELETE -H "X-Okapi-Token: $AUTH_TOKEN" "${OKAPI_URL}/_/proxy/modules/${SVC_ID}"
echo

# ./gradlew clean generateDescriptors
echo Install latest module ${SVC_ID}/${INS_ID} 
curl -XPOST -H "X-Okapi-Token: $AUTH_TOKEN" ${OKAPI_URL}/_/proxy/modules -d @"${DESCRIPTORDIR}/ModuleDescriptor.json"
echo

echo -e "\n\nPOSTING DEPLOYMENT DESCRIPTOR:"
curl -XPOST -H "X-Okapi-Token: $AUTH_TOKEN" "${OKAPI_URL}/_/discovery/modules" -d "$DEP_DESC"
echo

popd
