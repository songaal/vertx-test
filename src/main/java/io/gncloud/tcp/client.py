# Copyright (c) Twisted Matrix Laboratories.
# See LICENSE for details.


"""
An example IRC log bot - logs a channel's events to a file.

If someone says the bot's name in the channel followed by a ':',
e.g.

    <foo> logbot: hello!

the bot will reply:

    <logbot> foo: I am a log bot

Run this script with two arguments, the channel name the bot should
connect to, and file to log to, e.g.:

    $ python ircLogBot.py test test.log

will log channel #test to the file 'test.log'.

To run the script:

    $ python ircLogBot.py <channel> <file>
"""


from __future__ import print_function

# twisted import
from twisted.internet import reactor, protocol
from twisted.python import log
from twisted.internet.protocol import Protocol

# system imports
import time, sys

from random import randint
from time import sleep
from threading import Thread

# sleep(randint(10,100))


class RandomMessageMan(Thread):
    def __init__(self, channel):
        self.channel = channel
        Thread.__init__(self)
        self.daemon = True
        self.start()
    def run(self):
        while True:
            print("thread wrote...")
            i = randint(1,10)
            sleep(i)
            self.channel.write("%d hi server" % i)


class MessageLogger:
    """
    An independent logger class (because separation of application
    and protocol logic is a good thing).
    """
    def __init__(self, file):
        self.file = file

    def log(self, message):
        """Write a message to the file."""
        timestamp = time.strftime("[%H:%M:%S]", time.localtime(time.time()))
        self.file.write('%s %s\n' % (timestamp, message))
        self.file.flush()

    def close(self):
        self.file.close()


class AgentProtocol(Protocol):

    def dataReceived(self, data):
        print("received > ", data)


    def connectionMade(self):
        self.logger = MessageLogger(open(self.factory.filename, "a"))
        self.logger.log("[connected at %s]" %
                        time.asctime(time.localtime(time.time())))
        self.transport.write("Connected!")
        RandomMessageMan(self.transport)

    def connectionLost(self, reason):
        self.logger.log("[disconnected at %s]" %
                        time.asctime(time.localtime(time.time())))
        self.logger.close()


class AgentFactory(protocol.ClientFactory):
    """A factory for LogBots.

    A new protocol instance will be created each time we connect to the server.
    """

    def __init__(self, channel, filename):
        self.channel = channel
        self.filename = filename
        print("_INIT_")

    def buildProtocol(self, addr):
        p = AgentProtocol()
        p.factory = self
        return p

    def clientConnectionLost(self, connector, reason):
        connector.connect()

    def clientConnectionFailed(self, connector, reason):
        print("connection failed:", reason)
        reactor.stop()


if __name__ == '__main__':
    # initialize logging
    log.startLogging(sys.stdout)
    # create factory protocol and application
    f = AgentFactory("a", "a")
    # connect factory to this host and port
    reactor.connectTCP("localhost", 10000, f)
    # run bot
    reactor.run()
