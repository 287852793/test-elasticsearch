#!/bin/bash
cd `dirname $0`

docker logs -f --tail 200 es
