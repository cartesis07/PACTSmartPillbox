//on est sur la nano

//esp32
// Include Arduino Wire library for I2C
#include <Wire.h>

// Define Slave I2C Address
#define SLAVE_ADDR 9

// Define Slave answer size
byte reponse = 1;

int message;
byte a, b;

//message reception
#define presence 1
#define renisialisation 3
//delivre (11 -30)

//message reponse
#define presence_ok 1
#define delivre 2
#define renisialisation_ok 3
#define not_delivre 4
#define not_renisialisation 5
#define prise_medicament 8

//buffer
byte position_beffer = 0;
int tab_beffer[4] = {0, 0, 0, 0};

//time
unsigned long  g_time = 0;

//roue
#define temps_max_initialisation 5000//30000

//capteur effet hall
#define pin_hall A2

//moteur pas à pas
#include <Stepper.h>
double stepsPerRevolution = 2048;
Stepper myStepper(stepsPerRevolution, 8, 10, 9, 11);  // Pin inversion to make the library work

#define v_compartiment 250 //on trouve la bonne valeur
byte position_roue = 0;// en compartiment

//serial
int incomingByte = 0; // for incoming serial data

//servo moteur
#include <Servo.h>
#define possition_ferme 20
#define possition_ouvert 80
static const int servoPin_bas = 7;
static const int servoPin_haut = 6;
Servo servo1_bas;
Servo servo1_haut;

//balance
#include "HX711.h"

#define DOUT  A1
#define CLK  A0

HX711 scale;

float calibration_factor = -30000; //-7050 worked for my 440lb max scale setup
float set_up_poid = 0;
float poid ;
byte verife_poid = 0 ;
float poid_enregistre = 0;

void setup() {
  //serial
  Serial.begin(115200);
  //espe32
  // Initialize I2C communications as Slave
  Wire.begin(SLAVE_ADDR);
  Wire.onRequest(requestEvent);
  Wire.onReceive(receiveEvent);

  //moteur pas à pas
  myStepper.setSpeed(10);
  //servo moteur
  Serial.print("servo : ");
  servo1_bas.attach(servoPin_bas);
  servo1_haut.attach(servoPin_haut);
  Serial.println("OK");


  //   Effet hall
  Serial.print("hall : ");
  pinMode(pin_hall, INPUT);
  Serial.println("OK");

  initialisation_roue();

  //balance
  scale.begin(DOUT, CLK);
  scale.set_scale();
  scale.tare(); //Reset the scale to 0

  long zero_factor = scale.read_average(); //Get a baseline reading
  Serial.print("Zero factor: "); //This can be used to remove the need to tare the scale. Useful in permanent scale projects.
  Serial.println(zero_factor);
  scale.set_scale(calibration_factor);
  set_up_poid = scale.get_units();
  verife_poid = 0;
  Serial.println("go loop");

}

void loop() {

  if (  verife_poid == 1) {
    poid_test();
  }

//partie test systeme
  if (Serial.available() > 0) {
    int inChar = Serial.parseInt();
    Serial.print("message recue Serial : ");
    Serial.println(inChar);
    if (inChar == 5) {
      // convert the incoming byte to a char and add it to the string:
      Serial.println("teste medicament 4");
      delivre_pilule(4);
    }
  }

//lecture des tâches restant a faire  
  lecture_des_tache();


}

void lecture_des_tache() {
  if (position_beffer != 0) {
    //on doit faire une tâche
    Serial.print("la tache a faire = ");
    Serial.println(tab_beffer[position_beffer]);

    if (tab_beffer[position_beffer] >= 11) { //prise medicament
      Serial.println("go travaiellee");
      delivre_pilule(tab_beffer[position_beffer] - 11);

    }
    position_beffer = position_beffer - 1;
  }
}
