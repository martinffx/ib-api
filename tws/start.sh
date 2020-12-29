#!/usr/bin/env bash
set -euo pipefail

# add login details from ENV VAR's to IBC config
cat /home/$USER/ibc/config.ini.tpl | \
    sed "s|TRADING_MODE|${TRADING_MODE}|g" | \
    sed "s|IB_USERNAME|${IB_USERNAME}|g" | \
    sed "s|IB_PASSWORD|${IB_PASSWORD}|g" > /home/$USER/ibc/config.ini

/bin/bash /opt/ibc/twsstart.sh -inline
# /bin/bash /opt/ibc/gatewaystart.sh -inline
wait
