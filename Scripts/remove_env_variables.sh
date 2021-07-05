#!/bin/bash

envFile=/etc/profile.d/env_variables_onboardexp.sh

if [ -e "$envFile" ];
then
    rm --force ${envFile}
    echo "The environment variables was deleted successful."
fi