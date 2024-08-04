#!/bin/bash

declare -i saveIndex=0

for file in "$@"
do
  for i in {1..15}
  do
    TYPE=$(xxd -p -l1 -s $((i*8192)) "$file")

    if [ $((16#$TYPE)) -eq 83 ]
    then
      echo Extracting $file save $i
      dd bs=1 skip=$((i*8192)) count=1920 if="$file" of="$((saveIndex)).dsav"
    fi

    ((saveIndex++))
  done
done
