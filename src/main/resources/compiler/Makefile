CC = C:\MinGW\bin\gcc
CFLAGS = \
	-Wredundant-decls     \
	-Wformat-security     \
	-Wformat-signedness   \
	-Werror               \
	-Wpedantic            \
	-pedantic-errors      \
	-Wall                 \
	-Wpointer-arith       \
	-Wshadow              \
	-Wdouble-promotion    \
	-Wfloat-equal         \
	-Wextra
NAME = UNKOWN
OUT = prog

.PHONY: build
build: main.o
	$(CC) $(CFLAGS) -o "$(OUT)" main.o

main.o : main.c
	$(CC) $(CFLAGS) -c main.c -DNAME='"$(NAME)"'