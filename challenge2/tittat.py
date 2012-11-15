#!/usr/bin/python

import sys
response = "cooperate"

while(True):
    enemyresponse = input()

    print(response)
    response = enemyresponse
    sys.stdout.flush()
