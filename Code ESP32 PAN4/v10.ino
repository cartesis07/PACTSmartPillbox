//esp 32 ici

#define GG_DEBUG

//stockage
#define compartiment_n 20
byte roue[compartiment_n] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //les pilules, 0 si vide
int total_medicament = 0;
byte pilule_prise_balance = 0;

unsigned long time_delivre_medicament;
#define max_time_delivre 30000

//MP3
void play(unsigned char Track);
void volume( unsigned char vol);
#include <SoftwareSerial.h>

SoftwareSerial swSer(13, 12, false, 256);
unsigned char order[4] = {0xAA, 0x06, 0x00, 0xB0};

#define initialisation 0x01
#define fin_initialisation 0x02
#define avertisement_prise 0x03
#define prise_medicament 0x04
#define bug 0x05


//arduino nano
// Include Arduino Wire library for I2C
#include <Wire.h>

// Define Slave I2C Address
#define SLAVE_ADDR 9

// Define Slave answer size
#define ANSWERSIZE 5

byte tab_message[2];

//partie message
#define temps_max_envoil 6000 //6ms
#define tcp_confirmation 90

//message envoiyé
#define presence 1
#define renisialisation 3
//delivre (11 -30)

//message reception
#define presence_ok 1
#define delivre 2
#define reinitialisation_ok 3
#define not_delivre 4
#define not_reinitialisation 5
#define prise_medicament 8
byte demande = 0;
/*
   dans le document mp3
*/

//client serveur
#include <WiFi.h>

const char* ssid = "freebox";
const char* password =  "aaaaaaaa";

const uint16_t port = 15003;
const char * ip = "192.168.43.11";

bool clientActive = false;
char incomingByte;
WiFiClient client;

unsigned long ping_time = 0;
unsigned long mesage_time = 60000;

//syste
int etat_bug = 0;

void setup() {
  //systeme
  Serial.begin(115200);
  Serial.println("Start setup");


  //MP3
  Serial.print("MP3 : ");
  swSer.begin(9600);
  volume(0x10);//Volume settings 0x00-0x1E
  Serial.println("OK");
  play(initialisation);

  //nano
  Serial.print("nano : ");
  Wire.begin();
  message_to_send(1);
  Serial.print("message go : ");
  message_to_send(3);


  //client serveur
  Serial.print("client serveur : ");
  Serial.println("OK");

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("...");
  }

  Serial.print("WiFi connected with IP: ");
  Serial.println(WiFi.localIP());



  client.connect(ip, port); // Connects to a specified IP address and port
  //client.print("patatz");
  client.println("e");


  Serial.println("go loop");


}


void loop() {


  if (client.connected())   // Returns true if the client is connected, false if not
  {

    if ( Wire.read() == prise_medicament && demande == 1) {
      demande = 0;
      play(prise_medicament);
      client.println(2);
      Serial.println("medicament pris!!");
      //alors tout est bon et peux diminue les stock

    }
    if (demande == 1) {
      play(avertisement_prise);
      delay(600);
    }

    if (client.available())
    { // Returns the number of bytes available for reading
      delay(10);
      incomingByte = client.read(); // Read the next byte received from the server the client is connected to
      Serial.print("From client: \"");
      Serial.print((char)incomingByte);
      //Serial.println(incomingByte);
      Serial.println("\"");
      if (incomingByte >= '1') {
        Serial.println("faire une prise");
        delivre_pilule(incomingByte);
        mesage_time = millis();
      }


    }

    if (( millis() - ping_time) >= 10000) {
      Serial.println("Ping fonction");
      if (millis() - mesage_time <= 60000 || demande == 1) {

      } else {
        client.println("p");
        Serial.println("ping p go");
      }

      ping_time = millis();
    }
  }

  //test fonctionnement
  if (Serial.available() > 0) {
    int inChar = Serial.parseInt();
    Serial.print("message reçu Serial : ");
    Serial.println(inChar);
    if (inChar == 5) {
      // convert the incoming byte to a char and add it to the string:
      Serial.println("test medicament 100");
      delivre_pilule(100);
    }
  }


}
