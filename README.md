# vertx-test

TCP socket으로 string, int 전송테스트.

서버는 Java vertx 로 작성하고, 클라이언트는 python 으로 작성.

자바는 기본적으로 빅엔디안, 파이썬은 리틀엔디안이므로 데이터를 pack 할때 big endian 을 사용하도록 ">" 을 추가한다.

