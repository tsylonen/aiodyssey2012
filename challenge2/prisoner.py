#!/usr/bin/python

import sys
import os


coop = "cooperate"
defe = "defect"

##################
# strategy detectors
#
def detect_reverse(history):
    last_enemy, last_own =  history[0]
    for enemy, own in history[1:]:
        if enemy == last_own:
            return False
        last_enemy, last_own = enemy, own
    return True

# test if opponents response is uniquely
# determined from our last move
def detect_random(history):
    last_enemy, last_own = history[0]
    cc = cd = dd = dc = False # cooperate -> cooperate, cooperate -> defect...
    for enemy, own in history[1:]:
        if last_own == coop and enemy == coop:
            cc = True
        elif last_own == coop and enemy == defe:
            cd = True
        elif last_own == defe and enemy == coop:
            dc = True
        else:
            dd = True
    # a given response can yield different responses
    if cc and cd:
        return True
    if dc and dd:
        return True
    
    return False


##################
# elementary strategies
#
def defect(history, rounds=100):
    for i in range(rounds):
        print(defe)
        enemy = input()
        history.append((defe, enemy))

def tittat(history, rounds=100):
    move = coop
    for i in range(rounds):
        print(move)
        enemymove = input()
        
        history.append((enemymove, move))

        if enemymove == coop:
            move = coop
            
        else:
            move = defe

            
    sys.stdout.flush()


if __name__=="__main__":
    history = []

    # an extra initial cooperate so we don't
    # get stuck with bully tit for tat
    move = coop
    print(move)
    enemymove = input()
    
    history.append((enemymove, move))
    
    #first run a test period to find out enemy strategy
    tittat(history, 9)

    #if enemy plays reverse just defect
    if detect_reverse(history) or detect_random(history):
        defect(history)

    #default to tit for tat
    tittat(history)

    
