import binascii
import sys
import socket
import struct
import sys

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

values = ('abdf', 27)
packer = struct.Struct('>4s i')
packed_data = packer.pack(*values)

sock.connect( ('localhost', 10000) )

try:

    # Send data
    print >>sys.stderr, 'sending "%s"' % binascii.hexlify(packed_data), values
    sock.sendall(packed_data)

finally:
    print >>sys.stderr, 'closing socket'
    sock.close()