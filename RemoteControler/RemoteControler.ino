#include <IRremote.h>
#include <string.h>


#define SAMSUNG_BITS  32 
#define LG_BITS 28
int RECV_PIN = 11;
IRsend irsend;
IRrecv irrecv(RECV_PIN);
decode_results results;


char check;

int len;
int recv[11];
char *rec;


void setup() {
 Serial.begin(9600);
 pinMode(11,OUTPUT);
 pinMode (3, OUTPUT);  //output as used in library
 irrecv.enableIRIn();
}

String readSerial()   
{   
   String str = "";   
   char ch;   
   
    while( Serial.available() > 0 )   
    {   
      ch = Serial.read();   
      str.concat(ch);   
      delay(10);  
    }   
    return str;     
}   


void loop() {
  // put your main code here, to run repeatedly:
 String str;
 
 long code;
 while(!Serial.available());
 str = readSerial();
 
 
 if(str=="add"){
    
    while(!(irrecv.decode(&results))){
   ;// Serial.println("1");
    }
    Serial.println(results.value, HEX);
    irrecv.resume(); // Receive the next value
    // delay(1000);
 
    /*
    if (irrecv.decode(&results)) {
         Serial.println(results.value, HEX);
         irrecv.resume(); // Receive the next value
      }
    */
  }
//String str = "21gg";
  else{
       //Serial.println(str);
       //delay(2000);
       int strLen = str.length();
       
       char result[strLen+1];
       str.toCharArray(result, strLen+1);
        Serial.println(result);
       char *stop;
       unsigned long result2 = strtoul(result, &stop, 16);
       //Serial.println(result2);
       
       
       if(strLen==8){
       irsend.sendSAMSUNG(result2, SAMSUNG_BITS);
       
       delay(1000);
       irsend.sendSAMSUNG(result2, SAMSUNG_BITS);
       delay(1000);
       }
       
       delay(1000);
       if(strLen==7){ 
       irsend.sendLG(result2, LG_BITS);
       delay(1000);
       irsend.sendLG(result2, LG_BITS);  
       }
       delay(1000);
   }
 }

