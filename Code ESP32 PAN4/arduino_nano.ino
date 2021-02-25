void message_to_send(int message) {
  Wire.beginTransmission(SLAVE_ADDR);
  tab_message[0] = (message >> 8) & 0xFF;
  tab_message[1] = message & 0xFF;
  Wire.write(tab_message, 2);
  Wire.endTransmission();

  //partie acquisition

  Wire.requestFrom(SLAVE_ADDR, 1);

  // Add characters to string
  int aquisition = 0;
  unsigned long  qualiti = micros();
  while (aquisition == 0 && (micros() - qualiti < temps_max_envoil)) {


    while (Wire.available()) {
      char b = Wire.read();
      if (b == tcp_confirmation) {
        //on a la confirmation
        aquisition = 1;
#ifdef GG_DEBUG
        int v_qualiti = micros() - qualiti;
        Serial.println("Temps devois de tout en micro ici : ");
        Serial.println(v_qualiti);
#endif
        return;
      }
    }
  }

#ifdef GG_DEBUG
  Serial.println("Trop de temps");
  //son erreur de trame
#endif


}
