#!/usr/bin/env bash

srun hostname -f | awk -F "." '{print $1" slots=8 max_slots=8"}' | tee hostfile
