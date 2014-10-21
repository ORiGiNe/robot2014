// ##############################
char NAME[] = "Arduino_Uno_1";
// ##############################

//==== CONSTANTS DEFINITION ====

// Ready code for Arduino detection (ArduinoSerialConnection)
byte READY_CODE = 42;

// Bit rate for Serial communication
int BIT_RATE = 9600;

// CommandType enum
byte PIN_MODE = 1;
byte DIGITAL_READ = 2;
byte DIGITAL_WRITE = 3;
byte ANALOG_READ = 4;
byte ANALOG_WRITE = 5;
byte PULSE_IN = 6;
byte PULSE_OUT = 7;
byte ATTACH_INTERRUPT = 8;
byte GET_COUNT_ATTACHE = 9;

// IoMode enum
byte IoModeINPUT = 0;
byte IoModeOUTPUT = 1;

// IoLevel enum
byte IoLevelHIGH = 255;
byte IoLevelLOW = 0;

//===== VARIABLES =====

byte commandType = 0;
byte pinId = 0;
byte parameter = 0;

long count0 = 0;
long count1 = 0;

void setup() {
  Serial.begin(BIT_RATE);
  
  Serial.write(READY_CODE);
  sendString(NAME);
}

void loop() { 
  if(Serial.available() >= 3) {
    commandType = Serial.read();
    pinId = Serial.read();
    parameter = Serial.read();
    
    if(commandType == PIN_MODE) {
      pinMode(pinId, parameter);
    } else if (commandType == DIGITAL_WRITE) {
      digitalWrite(pinId, parameter);
    } else if (commandType == ANALOG_WRITE) {
      analogWrite(pinId, parameter);
    } else if (commandType == DIGITAL_READ) {
      Serial.write((byte) digitalRead(pinId));
    } else if (commandType == ANALOG_READ) {
      Serial.write((byte) (analogRead(pinId) / 4));  
    } else if (commandType == PULSE_IN) {
      sendLong(pulseIn(pinId, parameter, receiveLong()));
    } else if (commandType == PULSE_OUT) {
      digitalWrite(pinId, 1-parameter); 
      delayMicroseconds(2); 
      digitalWrite(pinId, parameter); 
      delayMicroseconds(10); 
      digitalWrite(pinId, 1-parameter);
    } else if (commandType == ATTACH_INTERRUPT) {
      if(pinId == 0) {
        attachInterrupt(0, trigger0, parameter);
        count0 = 0; 
      } else {
        attachInterrupt(1, trigger1, parameter);
        count1 = 0;
      }
    } else if (commandType == GET_COUNT_ATTACHE) {
      if(pinId==0) {
        sendLong(count0);
        count0=0;
      } else {
        sendLong(count1);
        count1 = 1;
      } 
    }
  } else {
    delay(10);
  }
}

void sendString(char* s) {
  Serial.write(strlen(s));
  Serial.print(s); 
}

byte strlen(char* s) {
  byte size = 0;
  while(*(s+size)!='\0') { size++; }
  return size;  
}

void trigger0() {
  count0 ++; 
}

void trigger1() {
  count1 ++; 
}

/** ATTENTION : les long de arduino sont des int en JAVA **/

long receiveLong() {
  static long r= 0;
  while(Serial.available()<4) {
    delay(5);
  }
  r=0;
  r += ((long) Serial.read()) << 24;
  r += ((long) Serial.read()) << 16;
  r += ((long) Serial.read()) << 8;
  r += ((long) Serial.read());
  return r;
}

void sendLong(long value) {
  char* s = (char*) (long*) &value;
  for(byte i=0; i<4; ++i) {
     Serial.write(*(s + 3 - i)); 
  }
}


