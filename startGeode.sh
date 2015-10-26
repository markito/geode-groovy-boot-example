#!/bin/bash

## change for your environment
export GEODE_HOME=/Users/wmarkito/Pivotal/ASF/incubator-geode/gemfire-assembly/build/install/apache-geode

export PATH=$GEODE_HOME/bin:$PATH

gfsh start locator --name=locator1
gfsh -e "connect" -e "start server --name=server1"

gfsh -e "connect" -e "create region --name='Person' --type='PARTITION'"

