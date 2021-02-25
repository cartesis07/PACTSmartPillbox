void initialisation_roue() {
  // 1 rotation counterclockwise:
  Serial.println("go initialisation");
  g_time = millis();

  while ((millis() - g_time) <= temps_max_initialisation) {
    myStepper.step(1);
    // Serial.println(digitalRead(pin_hall));
    if (digitalRead(pin_hall) == HIGH) {
      Serial.println("position zero");
      position_roue = 1;
      //renvoille un ok a esp32
      return;
    }
  }

  Serial.println("Erreur time");
  //renvoyer erreur
}
