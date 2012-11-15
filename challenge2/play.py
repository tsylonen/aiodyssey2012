#!/usr/bin/python2
from subprocess import *
import sys

def run(f1,f2):
	p1 = Popen('./'+f1, stdin=PIPE, stdout=PIPE)
	p2 = Popen('./'+f2, stdin=PIPE, stdout=PIPE)

	scores = [[2,10],[0,5]]

	moves = ['cooperate','defect']

	def move(f):
		m = f.readline().strip().lower()
		return moves.index(m)

	s1 = 0
	s2 = 0
	for i in xrange(100):
		m1 = move(p1.stdout)
		m2 = move(p2.stdout)
		s1 += scores[m1][m2]
		s2 += scores[m2][m1]
		p1.stdin.write(moves[m2]+'\n')
		p2.stdin.write(moves[m1]+'\n')
		p1.stdin.flush()
		p2.stdin.flush()

	p1.kill()
	p2.kill()
	return s1,s2

p1,p2 = sys.argv[1], sys.argv[2]
s1,s2 = run(p1, p2)
print 'Result of 100 games:'
print '%s: %d' % (p1,s1)
print '%s: %d' % (p2,s2)
