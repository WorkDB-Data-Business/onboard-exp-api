#!/bin/bash

file=.env
envFile=/etc/profile.d/env_variables_onboardexp.sh

if [ -e "$file" ];
then
    if [ ! -e "$envFile" ];
        then
            > ${envFile}
        else 
            rm --force ${envFile}
    fi
    while read p; do
        echo "export ${p}" >> ${envFile} 
        echo "The environment variables was created successful."
    done <${file}
else
    echo ".env does not exist."
fi