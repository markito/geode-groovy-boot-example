#!/bin/bash

## change for your environment
export GEODE_HOME=/Users/wmarkito/Pivotal/ASF/incubator-geode/gemfire-assembly/build/install/apache-geode

export PATH=$GEODE_HOME/bin:$PATH

gfsh stop server --dir=server1
gfsh stop locator --dir=locator1

