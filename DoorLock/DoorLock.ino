#include <Servo.h>
#include <string.h>

#define analogPin 0 
#define magneticDoorLock 12 //3

//사용하는 변수 선언 및 초기화
int keyCount = 0; // 키패드 통한 비밀번호 입력 갯수
int next_go = 1;   // 다음번 비밀번호 입력 수행 플래그 '1':수행가능 , '0':수행불가능(대기)
int i,secOpen,secError;
char pwdCorrect [5]= {'1','2','3','4'};

//  keypad 셋팅
const int numRows = 4;
const int numCols = 4;
const int debounceTime = 20;  //스위치가 안정화되는데 필요한 시간
const char keymap[numRows][numCols] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};

const int rowPins[numRows] = {9,8,7,6}; //행 0~3
const int colPins[numCols] = {5,4,3,2}; //열 0~3
//int flag =1;
//LED 포트 연결
int ledClose = 13; // 빨강
int ledInput = 12; // 노랑
int ledOpen = 11; //초록

//모듈에서 데이터 받는 변수
int doorClose; //마그네틱도어센서 값 
               //'HIGH' : 자석 감지 , 'LOW' : 자석 X
char key; //키패드 값


//사용하는 string 변수
char password[5];   // 키패드에서 입력받은 4자리 정수 비밀번호
char roomNumber[5]= "82"; // 객체(방)번호  -> 삭제

Servo locker; ///서브 모터

void setup() {
  // put your setup code here, to run once:
  pinMode(ledClose, OUTPUT);
  pinMode(ledOpen, OUTPUT);
  //pinMode(ledInput, OUTPUT);
  Serial.begin(9600);
  locker.attach(10);  ///모터 10번핀
  pinMode(magneticDoorLock,INPUT);
   for(int row = 0; row < numRows; row++)
  {
    pinMode(rowPins[row],INPUT);  // 행 핀을 입력으로 설정
    digitalWrite(rowPins[row], HIGH); // 풀업 저항을 작동
  }
  
  for(int col = 0; col < numCols; col++)
  {
    pinMode(colPins[col],OUTPUT); // 열 핀을 출력으로 설정
    digitalWrite(colPins[col], HIGH);
  }
}


// open


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
  
      while(!Serial.available()){
        key = getKey();  // 키패드에서 눌린 문자 확인
                
        if(next_go==1&& key != 0){

               //Serial.println(key); 
                password[keyCount++]= key;
               // Serial.println(keyCount);
                while(keyCount<4){  // 비밀번호는 4자리 수
                
                  key = getKey();  // 키패드에서 눌린 문자 확인
                  
                  if(key != 0){ 
                   
                        password[keyCount++]= key;  //패스워드 4자리 합침.
               //         Serial.println(key);
                 //       Serial.println(keyCount);
                        //digitalWrite(ledInput, HIGH); // 키패드 하나 눌릴때 마다 YellowLED 밝힘
                       // delay(100);
                       // digitalWrite(ledInput, LOW);
                        delay(100);
                      } 
                  }
              
              
        
              //변수 초기화 ( 비밀번호 입력된 갯수, 다음 비밀번호 입력 수행 플래그, 시리얼통신으로 전송할 데이터)
              keyCount=0;
              next_go = 0;
              
        
            if(!(strcmp(pwdCorrect,password))){
                 Serial.println("OK");
                 digitalWrite(ledOpen, HIGH); // 파란색 등(지금 핑크)
                locker.write(180); //걸쇠 오픈
                delay(3000);
                digitalWrite(ledOpen, LOW);
                while((doorClose = digitalRead(12))!=HIGH); // 문의 자석이 감지될 때까지 대기
                locker.write(90); //자석 감지되면 도어락 잠금
                delay(1000);
                next_go = 1;
               }
            //delay();
          
           else 
           {
               Serial.println("No");
               digitalWrite(ledClose, HIGH);
               locker.write(90);
               ///
               delay(1000);
               digitalWrite(ledClose, LOW);
               next_go = 1;
             }
     }
 }

      str = readSerial();
      
      if(str=="open"){
        
        Serial.println("OK");
        digitalWrite(ledOpen, HIGH);
        locker.write(180); //걸쇠 오픈
        delay(3000);
        digitalWrite(ledOpen, LOW);
        while((doorClose = digitalRead(12))!=HIGH); // 문의 자석이 감지될 때까지 대기vccccn
        locker.write(90); //자석 감지되면 도어락 잠금
        delay(1000);
        next_go = 1;
      }
       
  }


char getKey()
{
  char key = 0;
  for(int col = 0; col < numCols; col++)
  {
    digitalWrite(colPins[col], LOW);
    for(int row = 0; row < numRows; row++)
    {
      if(digitalRead(rowPins[row]) == LOW)
      {
        delay(debounceTime);
        while(digitalRead(rowPins[row]) == LOW)
        ;
        key = keymap[row][col];
      }
    }
    digitalWrite(colPins[col],HIGH);
  }
  return key;
}
