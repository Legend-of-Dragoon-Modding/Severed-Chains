#!/bin/bash

for i in {1..15}
do
  TYPE=$(xxd -p -l1 -s $((i*8192)) $1)

  if [ $((16#$TYPE)) -eq 83 ]
  then
    echo Extracting save $i
    dd bs=1 skip=$((i*8192)) count=1920 if=$1 of="$((i-1)).dsav"
  fi
done
