#!/usr/bin/env python

import subprocess
import os
import sys

class Run():
    def __init__(self):
        self.path = 'java/'
        self.java_files = ['ProcessFile.java',
                           'SpellCorrector.java']
        self.start_class = 'ProcessFile'


    def compile_java(self, filename, classpath):
        cmd = 'javac -classpath ' + classpath + ' ' + classpath + filename
        proc = subprocess.Popen(cmd, shell=True)
        print "Compiling " + filename
        subprocess.Popen.wait(proc)


    def run_java(self, java_class, classpath, arg):
        cmd2 = 'java -classpath ' + classpath + ' ' + java_class + ' ' + arg
        print cmd2
        proc = subprocess.Popen(cmd2, shell=True)
        print "Running " + java_class
        subprocess.Popen.wait(proc)


    def do(self, arg='train'):
        ''' Deletes all the class file
        '''
        for filename in os.listdir(self.path):
            if filename.endswith(".class"):
                print "Deleting " + filename + "..."
                os.remove(self.path + filename)

        ''' Checks all the files exists in data
        '''
        for filename in self.java_files:
            if os.access(self.path + filename, os.R_OK):
                print "Checking " + filename + "...Yes"
            else:
                print "Checking " + filename + "...No"
                return

        ''' Compile ProcessFile
        '''
        for filename in self.java_files:
            self.compile_java(filename, self.path)

        ''' Runs ProcessFile
        '''
        self.run_java(self.start_class, self.path, arg)


if __name__ == '__main__':
  run = Run()
  if sys.argv and len(sys.argv) == 2:
    run.do(sys.argv[1])
  else:
    run.do()

