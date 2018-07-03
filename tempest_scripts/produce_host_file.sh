#!/usr/bin/env bash

srun hostname -f | awk -F "." '{print $1}' | tee hostfile
