#!/usr/bin/python3

import sys
response = "cooperate"

coop = "cooperate"
defe = "defect"

while(True):
    print(response)
    sys.stdout.flush()

    enemyresponse = input()

    if enemyresponse == coop:
        response = defe
    else:
        response = coop
