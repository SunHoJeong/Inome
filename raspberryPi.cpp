#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <fcntl.h>
#include <stdint.h> //uint8_t definitions
#include <errno.h> //error output
  //wiring Pi
#include <wiringPi.h>
#include <wiringSerial.h>
#include <pthread.h>

#define REMOTECONTROL_WAIT_SIZE 7
#define REMOTECONTROL_RECV_SIZE 8+1
#define REMOTECONTROLSERVERPORT 15152

#define DOORLOCKOPEN_WAIT_SIZE 4
#define DOORLOCKOPEN_RECV_SIZE 4+1
#define PHOTO_SIZE 1024
#define DOORLOCKOPENSERVERPORT 15150
#define DOORLOCKOPENCLIENTPORT 15151

#define ADDIRINSTRUCTION_WAIT_SIZE 3
#define ADDIRINSTRUCTION_RECV_SIZE 3+1
#define IRINSTRUCTION_SIZE 8+1
#define ADDIRINSTRUCTIONSERVERPORT 15153
#define ADDIRINSTRUCTIONCLIENTPORT 15154

//----
char device[]= "/dev/ttyACM0";
int fd;
unsigned long baud = 9600;

//----

void* addIRInstruction(void*);
void* doorlockOpen(void*);
void* remoteControl(void*);


void* remoteControl(void*)
{
    int serv_sock_fd;
    int clnt_sock_fd;
    struct sockaddr_in serv_addr;
    struct sockaddr_in clnt_addr;
    socklen_t clnt_addr_size;
    char recv [REMOTECONTROL_RECV_SIZE];
	char recv2 [REMOTECONTROL_RECV_SIZE];
	int i = 0;

    clnt_addr_size = sizeof(clnt_addr);

    if ((serv_sock_fd = socket (AF_INET, SOCK_STREAM, IPPROTO_TCP)) == -1)
    {
        printf ("server socket fail\n");
        exit (1);
    }
    printf ("server socket success\n");

    bzero (&serv_addr, sizeof(serv_addr));  //memset (&serv_addr, 0, sizeof(serv_addr);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl (INADDR_ANY);
    serv_addr.sin_port = htons (REMOTECONTROLSERVERPORT);

    if (bind (serv_sock_fd, (struct sockaddr*) &serv_addr, sizeof(serv_addr)) == -1)
    {
        printf ("bind fail\n");
        exit (1);
    }
    printf ("bind success\n");

    if (listen (serv_sock_fd, 5) == -1)
    {
        printf ("listen fail\n");
        exit (1);
    }
    printf ("listen success\n");

    while (1)
    {
        //clnt_addr_size = sizeof(clnt_addr);

        if ((clnt_sock_fd = accept (serv_sock_fd, (struct sockaddr*) &clnt_addr, &clnt_addr_size)) == -1)
        {
            printf ("accept fail\n");
            exit (1);
        }
        printf ("accept success\n");


    memset (recv, 0x00, sizeof(recv));

        while (read (clnt_sock_fd, recv, sizeof(recv)) < REMOTECONTROL_WAIT_SIZE)
        {
            //sleep (1);
        }       
        printf ("read success\n");

        printf ("%s\n", recv);

        serialPuts (fd, recv);

	memset (recv2, 0x00, sizeof(recv2));
        
	while (serialDataAvail (fd) < REMOTECONTROL_WAIT_SIZE)
	{
		;
	}

	i = 0;

	sleep (1);
	
	while (serialDataAvail (fd) != 0)
	{
		recv2[i] = serialGetchar (fd);
		printf ("%c\n", recv2[i]);
		i++;
	}

	printf ("%s\n", recv2);
    }
}

void* doorlockOpen(void*)
{
    int serv_sock_fd;
    int clnt_sock_fd;
    struct sockaddr_in serv_addr;
    struct sockaddr_in clnt_addr;
    socklen_t clnt_addr_size;
    char recv[DOORLOCKOPEN_RECV_SIZE];

    int serv_sock_fd2;
    struct sockaddr_in serv_addr2;
    int clnt_addr_size2;
    int photo_fd;
    char photo[PHOTO_SIZE];




    clnt_addr_size = sizeof(clnt_addr);

    if ((serv_sock_fd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) == -1)
    {
        printf("server socket fail\n");
        exit(1);
    }

    printf("server socket success\n");

    bzero(&serv_addr, sizeof(serv_addr));  //memset (&serv_addr, 0, sizeof(serv_addr);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(DOORLOCKOPENSERVERPORT);

    if (bind(serv_sock_fd, (struct sockaddr*) &serv_addr, sizeof(serv_addr)) == -1)
    {
        printf("bind fail\n");
        exit(1);
    }

    printf("bind success\n");

    if (listen(serv_sock_fd, 5) == -1)
    {
        printf("listen fail\n");
        exit(1);
    }

    printf("listen success\n");

    while (1)
    {
        //clnt_addr_size = sizeof(clnt_addr);

        if ((clnt_sock_fd = accept(serv_sock_fd, (struct sockaddr*) &clnt_addr, &clnt_addr_size)) == -1)
        {
            printf("accept fail\n");
            exit(1);
        }

        printf("accept success\n");

        memset(recv, 0x00, sizeof(recv));

        while (read(clnt_sock_fd, recv, sizeof(recv)) < DOORLOCKOPEN_WAIT_SIZE)
        {
            ;
        }

        if (strcmp(recv, "open") == 0)  //strncmp
        {
            if ((serv_sock_fd2 = socket(AF_INET, SOCK_STREAM, 0)) == -1)
            {
                printf("client socket fail\n");
                exit(1);
            }

            printf("socket success\n");

            serv_addr2.sin_family = AF_INET;
            serv_addr2.sin_addr.s_addr = inet_addr("192.168.137.14");
            serv_addr2.sin_port = htons(DOORLOCKOPENCLIENTPORT);

            clnt_addr_size2 = sizeof(serv_addr2);

            if (connect(serv_sock_fd2, (struct sockaddr *) &serv_addr2, clnt_addr_size2) == -1)
            {
                printf("connect fail\n");
                exit(1);
            }

            printf("connect success\n");

            memset(photo, 0x00, sizeof(photo));
            
            serialPuts (fd, recv);

            if ((photo_fd = open("testImage1.jpg", O_RDONLY)) == -1)
            {
                printf("photo open fail\n");
                exit(1);
            }

            printf("open success\n");

            if (read(photo_fd, photo, sizeof(photo)) == -1)
            {
                printf("photo read fail\n");
                exit(1);
            }

            printf("read success\n");

            if (write(serv_sock_fd2, photo, sizeof(photo)) <= 0)
            {
                printf("write fail\n");
                exit(1);
            }

            printf("write success\n");

            close(serv_sock_fd2);
            close(photo_fd);
        }

        close(clnt_sock_fd);
    }
}

void* addIRInstruction(void*)
{
    int serv_sock_fd;
    int clnt_sock_fd;
    struct sockaddr_in serv_addr;
    struct sockaddr_in clnt_addr;
    socklen_t clnt_addr_size;
    char recv[ADDIRINSTRUCTION_RECV_SIZE];

    int serv_sock_fd2;
    struct sockaddr_in serv_addr2;
    int clnt_addr_size2;
    char IRInstruction[IRINSTRUCTION_SIZE];
	int i = 0;

    clnt_addr_size = sizeof(clnt_addr);

    if ((serv_sock_fd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) == -1)
    {
        printf("server socket fail\n");
        exit(1);
    }

    printf("server socket success\n");

    bzero(&serv_addr, sizeof(serv_addr));  //memset (&serv_addr, 0, sizeof(serv_addr);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(ADDIRINSTRUCTIONSERVERPORT);

    if (bind(serv_sock_fd, (struct sockaddr*) &serv_addr, sizeof(serv_addr)) == -1)
    {
        printf("bind fail\n");
        exit(1);
    }

    printf("bind success\n");

    if (listen(serv_sock_fd, 5) == -1)
    {
        printf("listen fail\n");
        exit(1);
    }

    printf("listen success\n");

    while (1)
    {
        //clnt_addr_size = sizeof(clnt_addr);

        if ((clnt_sock_fd = accept(serv_sock_fd, (struct sockaddr*) &clnt_addr, &clnt_addr_size)) == -1)
        {
            printf("accept fail\n");
            exit(1);
        }

        printf("accept success\n");

        memset(recv, 0x00, sizeof(recv));

        while (read(clnt_sock_fd, recv, sizeof(recv)) < ADDIRINSTRUCTION_WAIT_SIZE)
        {
            ;
        }

        if (strcmp(recv, "add") == 0)  //strncmp
        {
            if ((serv_sock_fd2 = socket(AF_INET, SOCK_STREAM, 0)) == -1)
            {
                printf("client socket fail\n");
                exit(1);
            }

            printf("socket success\n");

            serv_addr2.sin_family = AF_INET;
            //serv_addr2.sin_addr.s_addr = inet_addr("192.168.137.14");
            serv_addr2.sin_addr.s_addr = inet_addr("192.168.0.74");
            serv_addr2.sin_port = htons(ADDIRINSTRUCTIONCLIENTPORT);

            clnt_addr_size2 = sizeof(serv_addr2);

            if (connect(serv_sock_fd2, (struct sockaddr *) &serv_addr2, clnt_addr_size2) == -1)
            {
                printf("connect fail\n");
                exit(1);
            }

            printf("connect success\n");

            memset(IRInstruction, 0x00, sizeof(IRInstruction));

		serialPuts (fd, recv);

		while (serialDataAvail (fd) < REMOTECONTROL_WAIT_SIZE)
		{
 			;
		}

		i = 0;

		sleep (1);

		while (serialDataAvail (fd) != 0)
		{
			IRInstruction[i] = serialGetchar (fd);
			printf ("%c\n", IRInstruction[i]);
			i++;
		}

		printf ("%s\n", IRInstruction);
		//
		//fflush(stdout);

            if (write(serv_sock_fd2, IRInstruction, sizeof(IRInstruction)) <= 0)
            {
                printf("write fail\n");
                exit(1);
            }

            printf("write success\n");

            close(serv_sock_fd2);
        }

        close(clnt_sock_fd);
    }
}

void setUp(){
    if ((fd = serialOpen (device, baud)) < 0){
            fprintf (stderr, "Unable to open serial device: %s\n", strerror (errno)) ;
            exit(1); //error
        }
                 
        //setup GPIO in wiringPi mode
        if (wiringPiSetup () == -1){
            fprintf (stdout, "Unable to start wiringPi: %s\n", strerror (errno)) ;
            exit(1); //error
         }
}

int main(void)
{
    pthread_t p_thread[3];


	setUp();
        
    if (pthread_create (&p_thread[0], NULL, remoteControl, NULL) < 0)
    {
        printf("remoteControl thread error\n");
        exit(1);
    }

    if (pthread_create (&p_thread[1], NULL, doorlockOpen, NULL) < 0)
    {
        printf("doorlockOpen thread error\n");
        exit(1);
    }

    if (pthread_create (&p_thread[2], NULL, addIRInstruction, NULL) < 0)
    {
        printf("addIRInstruction thread error\n");
        exit(1);
    }

    pthread_join (p_thread[0], NULL);
    pthread_join (p_thread[1], NULL);
    pthread_join (p_thread[2], NULL);
    
    return 0;

}

