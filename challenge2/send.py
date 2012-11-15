#!/usr/bin/python2
from sh import chmod, zip as zzip, echo, rm
import mechanize

studentnum="013599676"
passwd="TUtzKkuhtI8ik6p"

#set scripts runnable and make the zip
chmod("+x", "./run.sh", "./compile.sh")
zzip("prisoner.zip", "run.sh", "compile.sh", "tittat.py")

#navigate to the login page
br = mechanize.Browser()
br.open("http://t-avihavai.users.cs.helsinki.fi/ai-challenges/index.htm")
br.select_form(nr=0)
br["studentNumber"] = studentnum
br["password"] = passwd
br.submit()

br.select_form(nr=0)
br.form.add_file(open("prisoner.zip"), 'text/plain', "prisoner.zip")
br.submit()

rm("prisoner.zip")









