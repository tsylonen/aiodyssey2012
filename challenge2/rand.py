#!/usr/bin/python3

import sys
import random

response = "cooperate"

coop = "cooperate"
defe = "defect"

while(True):
    print(random.choice([coop,defe]))
    sys.stdout.flush()

    enemyresponse = input()
